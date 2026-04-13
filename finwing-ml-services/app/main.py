from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import logging

from app.schemas import Transaction
from app.services.ai_service import build_prompt, get_ai_result
from app.services.rule_engine import apply_rules

app = FastAPI(title="Finwing AI API 🚀")

# CORS (for frontend / Spring Boot)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# logging
logging.basicConfig(level=logging.INFO)


@app.get("/")
def home():
    return {"message": "Finwing AI Running 🚀"}


@app.post("/predict")
def predict(data: Transaction):

    logging.info(f"Incoming: {data}")

    # 🔥 1. RULE ENGINE (FAST)
    rule_result = apply_rules(data)
    if rule_result:
        return rule_result

    # 🔥 2. AI MODEL
    prompt = build_prompt(data)
    result = get_ai_result(prompt)

    return result