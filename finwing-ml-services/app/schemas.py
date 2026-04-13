from pydantic import BaseModel

class Transaction(BaseModel):
    description: str
    type: str
    payment_mode: str
    expense: float


class AIResponse(BaseModel):
    category: str
    suggestion: str
    confidence: str