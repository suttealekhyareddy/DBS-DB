# PRACTICAL 05: FASTAPI BACKEND & SECURITY SUITE
# Install: pip install fastapi uvicorn pydantic passlib bcrypt
# Run: uvicorn Practical_05_FastAPI_Backend:app --reload

from datetime import date
from pathlib import Path

from fastapi import FastAPI, BackgroundTasks, HTTPException
from pydantic import BaseModel, Field, field_validator
from passlib.context import CryptContext

app = FastAPI(title="BookFlow FastAPI Backend")
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
LOG_FILE = Path("book_events.log")

class BookSchema(BaseModel):
    title: str
    isbn: str = Field(min_length=13, max_length=13)
    price: float = Field(gt=0)
    published_date: date

    @field_validator("published_date")
    @classmethod
    def validate_date(cls, value):
        if value > date.today():
            raise ValueError("published_date cannot be in the future")
        return value

class RegisterSchema(BaseModel):
    username: str
    password: str = Field(min_length=6)

@app.get("/")
async def root():
    return {"message": "BookFlow FastAPI Backend is running"}

@app.post("/books")
async def create_book(book: BookSchema, background_tasks: BackgroundTasks):
    background_tasks.add_task(log_book_event, book.title)
    return {
        "message": "Book accepted successfully",
        "book": book.model_dump()
    }

def log_book_event(title: str):
    with LOG_FILE.open("a", encoding="utf-8") as file:
        file.write(f"New Book Added: {title}\n")

@app.get("/books/search")
async def search_books(q: str, limit: int = 10):
    if not q.strip():
        raise HTTPException(status_code=400, detail="Query cannot be empty")
    return {
        "query": q,
        "limit": limit,
        "results": []
    }

@app.post("/register")
async def register(user: RegisterSchema):
    password_hash = pwd_context.hash(user.password)
    return {
        "username": user.username,
        "password_hash": password_hash
    }
