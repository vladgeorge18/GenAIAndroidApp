import requests
from flask import jsonify, request, send_file
from io import BytesIO

def insert_object(request):
    """
    Expects a multipart/form-data request with:
      - original (file): the original image file.
      - mask (file): the mask image file.
      - prompt (string): the text prompt.

    Returns:
      - On success: the image as a binary PNG with MIME type "image/png".
    """
    # Extract the form fields from the request
    prompt = request.form.get("prompt")
    original_file = request.files.get("original")
    mask_file = request.files.get("mask")

    # Verify that all required fields are provided
    if not prompt or not original_file or not mask_file:
        return jsonify({
            "error": "Missing required fields: prompt, original, and mask must be provided."
        }), 400

    try:
        # Make the request to the Stability API
        url = "https://api.stability.ai/v2beta/stable-image/edit/inpaint"
        headers = {
            "authorization": "Bearer sk-7GFwwSJYGx6MrJWmxzgNtnHSmcBpAvYxVqOEmdZaMeXNazFn",  # Replace with your actual API key
            "accept": "image/*"
        }
        files = {
            "image": (
                original_file.filename,
                original_file.read(),
                original_file.mimetype
            ),
            "mask": (
                mask_file.filename,
                mask_file.read(),
                mask_file.mimetype
            ),
        }
        data = {
            "prompt": prompt,
            "output_format": "png"  # Or "webp", if you prefer.
        }

        response = requests.post(url, headers=headers, files=files, data=data)

        if response.status_code == 200:
            # Return the resulting image to the client
            return send_file(
                BytesIO(response.content),
                mimetype="image/png"  # Match what we requested in `output_format`
            )
        else:
            # Return the JSON error message from the API
            return jsonify({"error": response.json()}), response.status_code

    except Exception as e:
        # Catch any exception and return an error response
        return jsonify({"error": f"Exception: {str(e)}"}), 500


