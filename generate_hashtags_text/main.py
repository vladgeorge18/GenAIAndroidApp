from google import genai
from google.genai import types
#import google.generativeai as genai
#from google.generativeai import types
from flask import jsonify, request
import os
import io 
import requests

def generate_hashtags_text(request):
    """
    Expects a multipart/form-data request with:
      - text
      - platform
      - target
      
    Returns:
      - On success: the hashtags.
    """
    
    # Verify that the arguments are provided.
    if 'text' not in request.form:
        return jsonify({"error": "Missing 'text' parameter."}), 400
    if 'platform' not in request.form:
        return jsonify({"error": "Missing 'platform' parameter."}), 400
    if 'target' not in request.form:
        return jsonify({"error": "Missing 'target' parameter."}), 400

    
   

    text = request.form['text']
    platform = request.form['platform']
    target = request.form['target']
    
    client = genai.Client(
        vertexai=True,
        project="speedy-web-456812-s7",
        location="us-central1",
    )
    
    
    
    msg1_text1 = types.Part.from_text(text=f"""You are an expert social media content creator. Given a text, generate compelling hashtags optimized for the specified social media platform and target audience. The caption should match the platform's tone and engage the intended audience effectively.
    Social Media Platform: [{platform}]
    Target Audience: [{target}]
    Text = [{text}]
    Ensure that:
    The tone aligns with the platform.
    The length fits the platform’s best practices
    Provide only the hashtags as the output.""")

    model = "gemini-2.0-flash-001"
    contents = [
        types.Content(
        role="user",
        parts=[
            msg1_text1
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