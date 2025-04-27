import os
import base64
import random
from io import BytesIO
from flask import jsonify, request
from huggingface_hub import InferenceClient

def generate_image(request):
    """
    Expects query parameter:
      - prompt: The text prompt for image generation.
    """
    # Try to get the 'prompt' parameter from JSON body or URL query parameters.
    req_json = request.get_json(silent=True)
    req_args = request.args

    prompt = ""
    if req_json and 'prompt' in req_json:
        prompt = req_json['prompt']
    elif req_args and 'prompt' in req_args:
        prompt = req_args.get('prompt')
    else:
        return jsonify({"error": "Missing 'prompt' parameter."}), 400

    # Set up the Hugging Face Inference Client.
    os.environ["HF_API_KEY"] = "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"
    client = InferenceClient(
        provider="hf-inference",
        api_key=os.environ["HF_API_KEY"]
    )
    rand = random.randint(0, 100)
    modified_prompt = f"{prompt} [random seed: {rand}]"
    try:
        # Generate the image using the provided prompt.
        image = client.text_to_image(
            modified_prompt,
            model="stabilityai/stable-diffusion-3.5-large"
        )
        # Convert the PIL image to a base64-encoded PNG.
        buffered = BytesIO()
        image.save(buffered, format="PNG")
        img_str = base64.b64encode(buffered.getvalue()).decode("utf-8")
        # Optional: include the data URI prefix.
        img_data = f"data:image/png;base64,{img_str}"
    except Exception as e:
        return jsonify({"error": f"Exception: {str(e)}"}), 500

    return jsonify({"image": img_data})