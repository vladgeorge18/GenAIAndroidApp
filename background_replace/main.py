import os
import time
from io import BytesIO
import base64
import requests
from flask import jsonify, request, send_file, make_response

def replace_background(request):
    """
    Expects a multipart/form-data request with:
      - file (the subject image)
      - prompt (background_prompt)
      
    Returns:
      - On success: the final image as a binary PNG (or WEBP) with appropriate MIME type.
    """

    # 1) Validate incoming form data
    if 'file' not in request.files:
        return jsonify({"error": "Missing 'file' parameter."}), 400
    if 'prompt' not in request.form:
        return jsonify({"error": "Missing 'prompt' parameter."}), 400

    file = request.files['file']
    prompt = request.form['prompt']
    if file.filename == "":
        return jsonify({"error": "No file selected."}), 400

    # 2) Build and send the first request (POST) to get a generation ID
    try:
        url = "https://api.stability.ai/v2beta/stable-image/edit/replace-background-and-relight"
        api_key = "sk-7GFwwSJYGx6MrJWmxzgNtnHSmcBpAvYxVqOEmdZaMeXNazFn"  # <--- read from environment in a real app
        headers = {
            "authorization": f"Bearer {api_key}",
            # The response from this endpoint includes JSON with the 'id'
            "accept": "image/*"
        }
        files_data = {
            "subject_image": (
                file.filename,
                file.read(),
                file.mimetype
            )
        }
        form_data = {
            "background_prompt": prompt,
            "output_format": "png",  # or "webp" if you prefer
        }

        response = requests.post(url, headers=headers, files=files_data, data=form_data)
        if response.status_code != 200:
            # Likely an error object from the Stability API
            return jsonify({"error": response.json()}), response.status_code

        resp_json = response.json()
        generation_id = resp_json.get("id")
        if not generation_id:
            return jsonify({"error": "No generation ID returned by Stability."}), 500

        # 3) Poll the result with a second request to /v2beta/results/{generation_id}
        #    We'll poll every 10 seconds, up to 3 attempts.
        #    This avoids hammering the endpoint too frequently.

        result_url = f"https://api.stability.ai/v2beta/results/{generation_id}"
        max_attempts = 3
        for attempt in range(1, max_attempts + 1):
            get_resp = requests.get(
                result_url,
                headers={
                    "authorization": f"Bearer {api_key}",
                    "accept": "application/json"
                }
            )

            if get_resp.status_code == 200:
                # Ready! Return the image
                data = get_resp.json()
                # data might look like:
                # {
                #   "result": "iVBORw0KGgoAAAANSUhEUgAABgAAAAQACAIAAA...",
                #   "finish_reason": "SUCCESS",
                #   "seed": 123456
                # }

                # If there's an error in the JSON, handle it:
                if "result" not in data:
                    return jsonify({"error": "No 'result' field in JSON"}), 500

                b64_image = data["result"]
                try:
                    image_bytes = base64.b64decode(b64_image)
                except Exception as e:
                    return jsonify({"error": f"Base64 decoding failed: {str(e)}"}), 500

                # Now serve the raw PNG bytes
                return send_file(
                    BytesIO(image_bytes),
                    mimetype="image/png"  # or "image/webp" if you used that format
                )
            elif get_resp.status_code == 202:
                # 202 means "in-progress." If this was our last attempt, bail out
                if attempt == max_attempts:
                    return jsonify({
                        "error": "Generation still in progress after multiple polls. Try again later."
                    }), 202
                # Otherwise, wait 10 seconds before polling again
                time.sleep(10)
            else:
                # Some other error occurred, try to return JSON body if available
                try:
                    return jsonify({"error": get_resp.json()}), get_resp.status_code
                except Exception:
                    # If not JSON, just pass the status code
                    return jsonify({"error": f"HTTP {get_resp.status_code}"}), get_resp.status_code

        # If we exit the loop without returning, we never received 200 or a final 202
        return jsonify({"error": "Unable to retrieve final image."}), 500

    except Exception as e:
        return jsonify({"error": f"Exception: {str(e)}"}), 500
