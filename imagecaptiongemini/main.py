from google import genai
from google.genai import types
#import google.generativeai as genai
#from google.generativeai import types
from flask import jsonify, request
import os
import io 
import requests
import base64

def generate_caption(request):
    """
    Expects a multipart/form-data request with:
      - file
      - platform
      - target
      - hashtags (y/n)
      - img_description (optional)
      
    Returns:
      - On success: the caption of the image.
    """
    
    # Verify that the arguments are provided.
    if 'file' not in request.files:
        return jsonify({"error": "Missing 'file' parameter."}), 400
    if 'platform' not in request.form:
        return jsonify({"error": "Missing 'platform' parameter."}), 400
    if 'target' not in request.form:
        return jsonify({"error": "Missing 'target' parameter."}), 400
    if 'hashtags' not in request.form:
        return jsonify({"error": "Missing 'hashtags' parameter."}), 400
    
    # Optional argument
    img_description = request.form.get('img_description', None)  # Default to None if not provided

    file = request.files['file']
    platform = request.form['platform']
    target = request.form['target']
    hashtags = request.form['hashtags']
    
    
    # Read the image file and encode it in Base64
    image_bytes = file.read() 
    

    client = genai.Client(
        vertexai=True,
        project="speedy-web-456812-s7",
        location="us-central1",
    )
    
    
    
    msg1_text1 = types.Part.from_text(text=f"""You are an expert social media content creator. Given an image, generate a compelling caption optimized for the specified social media platform and target audience. The caption should match the platform's tone and engage the intended audience effectively.
    Image Description (Optional): [{img_description}]
    Social Media Platform: [{platform}]
    Target Audience: [{target}]
    Include hashtags: [{hashtags}]
    Ensure that:
    The tone aligns with the platform.
    The length fits the platform’s best practices
    Provide only the caption as the output.""")
    msg1_image1 = types.Part.from_bytes(
        data=image_bytes,
        mime_type="image/jpeg",
    )

    model = "gemini-2.0-flash-001"
    contents = [
        types.Content(
        role="user",
        parts=[
            msg1_text1,
            msg1_image1
        ]
        ),
    ]
    try:
        generate_content_config = types.GenerateContentConfig(
            temperature = 1,
            top_p = 0.95,
            max_output_tokens = 8192,
            response_modalities = ["TEXT"],
        )
        caption= ""
        for chunk in client.models.generate_content_stream(
            model = model,
            contents = contents,
            config = generate_content_config,
            ):
            caption += chunk.text
    except Exception as e:
        return jsonify({"error": f"Exception: {str(e)}"}), 500
    return jsonify({"caption": caption})
    
    
