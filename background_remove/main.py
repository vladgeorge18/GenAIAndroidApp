import os
import base64
from io import BytesIO
import tempfile
from flask import jsonify, request, send_file
from PIL import Image
from gradio_client import Client, handle_file


# Initialize the background removal model client.
client = Client("ECCV2022/dis-background-removal")

def remove_background(request):
    """
    Expects a file upload via multipart/form-data with key:
      - file: The image file to process.
      
    Returns:
      - On success: the foreground image as a binary PNG with MIME type "image/png".
      - On error: a JSON object with an error message.
    """
    # Verify that the file is provided.
    if 'file' not in request.files:
        return jsonify({"error": "Missing 'file' parameter."}), 400

    file = request.files['file']
    if file.filename == "":
        return jsonify({"error": "No file selected."}), 400

    # Save the uploaded file to a temporary location.
    with tempfile.NamedTemporaryFile(delete=False, suffix=".png") as tmp:
        file.save(tmp)
        tmp_path = tmp.name

    try:
        # Prepare the image for the model (handle_file expects a file path).
        image_input = handle_file(tmp_path)
        
        # Call the model's API to remove the background.
        result = client.predict(image=image_input, api_name="/predict")
        # The model returns a list where result[0] is the foreground image.
        foreground = result[0]

        # Convert the returned foreground into a PIL Image.
        if isinstance(foreground, str) and os.path.exists(foreground):
            image = Image.open(foreground)
        elif isinstance(foreground, bytes):
            image = Image.open(BytesIO(foreground))
        else:
            image = foreground  # Assume it's already a PIL Image.

        # Convert the image to a PNG byte stream.
        buffered = BytesIO()
        image.save(buffered, format="PNG")
        buffered.seek(0)

        # Return the binary image with the appropriate MIME type.
        return send_file(buffered, mimetype="image/png")
    except Exception as e:
        return jsonify({"error": f"Exception: {str(e)}"}), 500
    finally:
        # Clean up the temporary file.
        if os.path.exists(tmp_path):
            os.remove(tmp_path)