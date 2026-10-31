
from fastapi import FastAPI
from pydantic import BaseModel
import re

app = FastAPI(title="CampusCare AI Support", version="1.0.0")

class ComplaintInput(BaseModel):
    description: str

RULES = [
    (["wifi", "internet", "network", "router", "lan"], "NETWORK", "IT SUPPORT"),
    (["water", "tap", "leak", "pipe", "drain"], "PLUMBING", "MAINTENANCE"),
    (["electric", "power", "wire", "switch", "shock", "light"], "ELECTRICAL", "ELECTRICAL"),
    (["projector", "computer", "lab", "printer", "screen"], "LAB_EQUIPMENT", "LAB SUPPORT"),
    (["clean", "garbage", "dust", "toilet", "washroom"], "CLEANLINESS", "HOUSEKEEPING"),
    (["security", "theft", "unsafe", "threat"], "SECURITY", "SECURITY"),
    (["chair", "desk", "bench", "table", "furniture"], "FURNITURE", "MAINTENANCE"),
]

@app.get("/health")
def health():
    return {"status": "ok", "service": "CampusCare AI"}

@app.post("/predict")
def predict(data: ComplaintInput):
    text = data.description.lower()
    category, department = "OTHER", "ADMINISTRATION"
    for keywords, cat, dept in RULES:
        if any(re.search(r"\b" + re.escape(word) + r"\b", text) for word in keywords):
            category, department = cat, dept
            break
    if any(w in text for w in ["fire", "smoke", "electric shock", "exposed wire", "gas leak", "life threatening"]):
        priority = "CRITICAL"
    elif any(w in text for w in ["not working", "leaking", "blocked", "no water", "no internet", "urgent"]):
        priority = "HIGH"
    elif any(w in text for w in ["broken", "damaged", "issue", "problem"]):
        priority = "MEDIUM"
    else:
        priority = "LOW"
    return {"category": category, "priority": priority, "department": department,
            "explanation": "Rule-based recommendation; staff/admin should verify before action."}
