from fastapi import FastAPI
from pydantic import BaseModel
from groq import Groq
import os
from dotenv import load_dotenv

# load env
load_dotenv()

# init groq client
client = Groq(api_key=os.getenv("GROQ_API_KEY"))

app = FastAPI(title="Transaction Categorization API 🚀")

# request model
class Transaction(BaseModel):
    description: str
    type: str
    payment_mode: str
    expense: float

# core function
def categorize_transaction(data: Transaction):

    prompt = f"""
You are a fintech AI.

Classify transaction into ONE category from:
Food, Travel, Bills, Shopping, Groceries, Fuel, Fitness,
Entertainment, Electronics, Education, Health, Insurance,
Investment, Rent, Subscriptions, Personal Care, Gifts, Pets,
Misc, Income

Rules:
- Salary, bonus, credit → Income
- Bills → Bills
- Food apps → Food
- Transport → Travel

Transaction:
Description: {data.description}
Type: {data.type}
Payment: {data.payment_mode}
Amount: {data.expense}

Return ONLY the category name. No explanation.
"""

    response = client.chat.completions.create(
        model="llama-3.1-8b-instant",
        messages=[{"role": "user", "content": prompt}],
        temperature=0
    )

    return response.choices[0].message.content.strip()


# API endpoint
@app.post("/predict")
def predict(data: Transaction):
    try:
        category = categorize_transaction(data)
        return {"category": category}
    except Exception as e:
        return {"error": str(e)}