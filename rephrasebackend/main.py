import os
import random
from flask import jsonify, request
from huggingface_hub import InferenceClient

# Define guidelines for each platform.
GUIDELINES = {
    "Instagram": {
        "tone": "casual, fun, and engaging",
        "style": "short, punchy sentences with emojis",
        "length": "brief and eye-catching",
        "extra": "use relevant hashtags"
    },
    "LinkedIn": {
        "tone": "professional, insightful",
        "style": "well-structured with valuable takeaways",
        "length": "a few sentences to a short paragraph",
        "extra": "use industry-relevant hashtags and offer a thought-provoking question"
    },
    "Facebook": {
        "tone": "friendly, conversational",
        "style": "medium-length with a story-telling approach",
        "length": "a few sentences, but not too long",
        "extra": "Encourage engagement by asking a question or inviting comments"
    }
}

def rephrase_text(request):
    """
    Expects query parameters:
      - text: The original text to rephrase.
      - platform: The target social media platform.
    """

    req_json = request.get_json(silent=True)
    req_args = request.args

    text = ""
    platform = "LinkedIn" 
    if req_json and 'text' in req_json:
        text = req_json['text']
        platform = req_json.get('platform', platform)
    elif req_args and 'text' in req_args:
        text = req_args.get('text')
        platform = req_args.get('platform', platform)
    else:
        return jsonify({"error": "Missing 'text' parameter."}), 400

    # Get basic guidelines 
    guidelines = GUIDELINES.get(platform, {
        "tone": "neutral",
        "style": "neutral",
        "length": "medium",
        "extra": "none"
    })

    # Build the prompt.
    prompt = f"""Rephrase the following text for a social media post on {platform}.
Follow these guidelines to match the style of {platform}:
- Tone: {guidelines['tone']}
- Style: {guidelines['style']}
- Length: {guidelines['length']}
- Additional Elements: {guidelines['extra']}

Original text:
"{text}"

"""
    
    os.environ["HF_API_KEY"] = "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"
    # Initialize the Hugging Face Inference Client.
    client = InferenceClient(
        provider="hf-inference",
        api_key=os.environ["HF_API_KEY"]  # Set Hugging Face API key.
    )

    # Use the chat API format.
    messages = [
        {"role": "user", "content": prompt}
    ]
    ran = random.uniform(0,0.05)
    try:
        completion = client.chat.completions.create(
            model="HuggingFaceTB/SmolLM2-1.7B-Instruct",
            messages=messages,
            max_tokens=500,
            temperature=0.7+ran
        )
        # Extract the content from the first choice.
        rephrased_text = completion.choices[0].message.get("content", "No content returned")
    except Exception as e:
        return jsonify({"error": f"Exception: {str(e)}"}), 500

    return jsonify({"rephrased_text": rephrased_text})