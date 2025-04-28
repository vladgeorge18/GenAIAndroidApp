import requests
from flask import jsonify, request, send_file
from io import BytesIO

def extend_image(request):
    """
    Expects a multipart/form-data request with:
      - original (file): the original image file.
      - coordinate values (left,right,up,down)
    

    Returns:
      - On success: the image as a binary PNG with MIME type "image/png".
    """
    # Extract the form fields from the request
    
    original_file = request.files.get("original")
    left_str = request.form.get("left",'0')
    right_str = request.form.get("right",'0')
    up_str = request.form.get("up",'0')
    down_str = request.form.get("down",'0')


    # Verify that all required fields are provided
    if  not original_file or not left_str or not right_str or not up_str or not down_str:
        return jsonify({
            "error": "Missing required fields"
        }), 400

    try:
        left = float(left_str)
        right = float(right_str)
        up = float(up_str)
        down = float(down_str)
    except ValueError:
        jsonify({"error":"left/right/up/down must be valid integers."}), 400
        
    try:
        # Make the request to the Stability API
        url = "https://api.stability.ai/v2beta/stable-image/edit/outpaint"
        headers = {
            "authorization": "Bearer sk-7GFwwSJYGx6MrJWmxzgNtnHSmcBpAvYxVqOEmdZaMeXNazFn",  # Replace with your actual API key
            "accept": "image/*"
        }
        files = {
            "image": (
                original_file.filename,
                original_file.read(),
                original_file.mimetype
            )
            
        }
        data = {
            "left": left, # value for left
            "right": right, # value for right
            "up": up, # value for up
            "down": down, #value for down
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


