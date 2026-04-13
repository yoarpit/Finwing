import os
import json
import logging
from groq import Groq
from dotenv import load_dotenv

load_dotenv()

# 🔥 logging setup
logging.basicConfig(level=logging.INFO)

# 🔥 init client safely
GROQ_API_KEY = os.getenv("GROQ_API_KEY")

if not GROQ_API_KEY:
    raise ValueError("GROQ_API_KEY is missing in .env")

client = Groq(api_key=GROQ_API_KEY)


# ✅ CLEAN PROMPT (STRICT OUTPUT)
def build_prompt(data):
    return f"""
You are a fintech AI.

Classify the transaction into EXACTLY ONE category from:
Food, Travel, Bills, Shopping, Groceries, Fuel, Fitness,
Entertainment, Electronics, Education, Health, Insurance,
Investment, Rent, Subscriptions, Personal Care, Gifts, Pets,
Misc, Income

Transaction:
Description: {data.description}
Type: {data.type}
Payment: {data.payment_mode}
Amount: {data.expense}

STRICT RULES:
- Return ONLY valid JSON
- No explanation
- No extra text

FORMAT:
{{"category": "CategoryName"}}
"""


# ✅ SAFE JSON EXTRACTION (LLM-proof)
def extract_json(text: str):
    try:
        start = text.find("{")
        end = text.rfind("}") + 1
        json_str = text[start:end]
        return json.loads(json_str)
    except Exception:
        logging.error(f"JSON parsing failed: {text}")
        return None


# ✅ MAIN AI FUNCTION (PRODUCTION)
def get_ai_result(prompt, retries=2):
    for attempt in range(retries):

        try:
            response = client.chat.completions.create(
                model="llama-3.1-8b-instant",
                messages=[{"role": "user", "content": prompt}],
                temperature=0,
                max_tokens=50
            )

            text = response.choices[0].message.content.strip()

            logging.info(f"AI Raw Response: {text}")

            parsed = extract_json(text)

            if parsed and "category" in parsed:
                return {"category": parsed["category"]}

        except Exception as e:
            logging.error(f"Attempt {attempt+1} failed: {str(e)}")

    # 🔥 FINAL FALLBACK
    return {"category": "Misc"}