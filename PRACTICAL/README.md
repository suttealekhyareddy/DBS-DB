# Practical 5 – FastAPI Backend & Security Suite

## Objective
Build the FastAPI gatekeeper with validation, asynchronous routes, background logging and password hashing.

## Requirements covered
- Pydantic BookSchema
- 13-character ISBN
- Positive price
- Future-date validation
- async route
- BackgroundTasks
- bcrypt password hashing
- pgvector search endpoint blueprint

## Run
```bash
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --reload
```

The PostgreSQL/SQLAlchemy integration point is clearly marked in `main.py`.
