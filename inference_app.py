import modal, subprocess, os, shutil, uuid
from pathlib import Path
from fastapi import UploadFile, File
from fastapi.responses import StreamingResponse

##############################################################################
# 1.  Persistent volume for outputs
##############################################################################
vol = modal.Volume.from_name("stytr-outputs", create_if_missing=True)  # stored in Modal

##############################################################################
# 2.  Container image (unchanged except added SciPy/FastAPI)
##############################################################################
image = (
    modal.Image.debian_slim()
         .pip_install(
             "torch", "torchvision", "pillow", "numpy",
             "scipy", "matplotlib", "fastapi[standard]",
         )
         .add_local_dir(".", remote_path="/app")   # code + models
)

##############################################################################
# 3.  GPU inference function
##############################################################################
app = modal.App("stytr-stytr-inference")


def run_inference(content_path: str, style_path: str):
    out_dir = Path("/outputs") / uuid.uuid4().hex
    out_dir.mkdir(parents=True, exist_ok=True)
    subprocess.run(
        [
            "python3", "test.py",
            "--content", content_path,
            "--style",   style_path,
            "--output",  str(out_dir),
        ],
        check=True,
        cwd="/app",
    )
    vol.commit()
    # return a recursive listing so you can see nested files/dirs
    return next(out_dir.iterdir())

##############################################################################
# 4.  Optional FastAPI endpoint (unchanged)
##############################################################################
@app.function(
    image=image,
    gpu = "A100",
    volumes={"/outputs": vol},      # <— writable, persistent
    min_containers=0,
)

@modal.fastapi_endpoint(method="POST")
def serve(
    content: UploadFile = File(...),
    style:   UploadFile = File(...),
):
    """Accepts 2 images and streams the stylised result."""

    # 1  Save uploads to /tmp inside this container
    tmp_content = f"/tmp/{uuid.uuid4()}_content.jpg"
    tmp_style   = f"/tmp/{uuid.uuid4()}_style.jpg"
    with open(tmp_content, "wb") as f:
        shutil.copyfileobj(content.file, f)
    with open(tmp_style, "wb") as f:
        shutil.copyfileobj(style.file, f)

    # 2  Run the GPU heavy‑lifting (synchronous call)
    output_path = Path(run_inference(tmp_content, tmp_style))   # waits
    vol.reload()

    mime = "image/png" if output_path.suffix.lower() == ".png" else "image/jpeg"
    # 4  Stream the bytes back to the caller
    return StreamingResponse(
        open(output_path, "rb"),
        media_type=mime,
        headers={"Content-Disposition": f'attachment; filename="{output_path.name}"'},
    )