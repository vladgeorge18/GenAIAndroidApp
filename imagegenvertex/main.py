import os
import base64
import random
import vertexai
from io import BytesIO
from flask import jsonify, request
from vertexai.preview.vision_models import ImageGenerationModel

def generate_image_vertex(request):
    """
    Expects query parameter:
      - prompt
    """
    PROJECT_ID = "speedy-web-456812-s7"
    LOCATION = "us-central1"
    vertexai.init(project = PROJECT_ID, location = LOCATION)
    generation_model = ImageGenerationModel.from_pretrained("imagegeneration@006")

    req_json = request.get_json(silent=True)
    req_args = request.args

    prompt = ""
    if req_json and 'prompt' in req_json:
        prompt = req_json['prompt']
    elif req_args and 'prompt' in req_args:
        prompt = req_args.get('prompt')
    else:
        return jsonify({"error": "Missing 'prompt' parameter."}), 400

    try:
        # Generate the image using the provided prompt.
        response = generation_model.generate_images(prompt=prompt,)
        image = response.images[0]
        # Save image to a temporary file.
        temp_filename = f"./temp_{random.randint(1, 999)}.png"
        image.save(temp_filename, include_generation_parameters=False)
        
        # Read the image bytes and encode them in base64.
        with open(temp_filename, "rb") as f:
            img_bytes = f.read()
        os.remove(temp_filename)
        
        img_str = base64.b64encode(img_bytes).decode("utf-8")
        img_data = f"data:image/png;base64,{img_str}"
    except Exception as e:
        return jsonify({"error": f"Exception: {str(e)}"}), 500

    return jsonify({"image": img_data})