import re

# 🔥 keyword dictionary (easy to scale)
CATEGORY_KEYWORDS = {
    "Food": [
        "zomato", "zomoto", "swiggy", "swigy", "restaurant", "food", "pizza", "burger"
    ],
    "Travel": [
        "uber", "uberr", "ola", "cab", "ride", "taxi", "flight", "train"
    ],
    "Bills": [
        "bill", "electricity", "water", "gas", "internet", "wifi", "dth"
    ],
    "Recharge": [
        "recharge", "mobile recharge", "topup"
    ],
    "Shopping": [
        "amazon", "flipkart", "shopping", "purchase", "order"
    ],
    "Groceries": [
        "grocery", "vegetables", "milk", "fruits", "mart"
    ],
    "Fuel": [
        "petrol", "diesel", "fuel", "pump"
    ],
    "Health": [
        "hospital", "doctor", "medicine", "pharmacy"
    ],
    "Entertainment": [
        "movie", "netflix", "spotify", "subscription"
    ],
    "Education": [
        "fees", "college", "school", "course"
    ],
    "Investment": [
        "mutual fund", "sip", "stock", "investment"
    ],
    "Insurance": [
        "insurance", "premium"
    ],
    "Rent": [
        "rent", "house rent"
    ],
    "Personal Care": [
        "salon", "haircut", "spa", "grooming"
    ],
    "Gifts": [
        "gift", "birthday", "present"
    ],
    "Pets": [
        "pet", "dog food", "cat food"
    ]
}


def clean_text(text: str) -> str:
    """Lowercase + remove special chars"""
    text = text.lower()
    text = re.sub(r"[^a-z0-9\s]", " ", text)
    return text


def apply_rules(data):
    desc = clean_text(data.description)

    # 🔥 1. HIGH PRIORITY RULES
    if data.type.lower() == "credit":
        return {"category": "Income"}

    # 🔥 2. KEYWORD MATCHING
    for category, keywords in CATEGORY_KEYWORDS.items():
        for keyword in keywords:
            if keyword in desc:
                return {"category": category}

    # 🔥 3. EMPTY / UNKNOWN HANDLING
    if not desc.strip():
        return {"category": "Misc"}

    # 🔥 4. DEFAULT FALLBACK
    return None  # let AI handle