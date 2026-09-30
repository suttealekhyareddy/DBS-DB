from datetime import date
from pathlib import Path

from fastapi import BackgroundTasks, FastAPI, HTTPException
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
    def date_not_future(cls, value: date):
        if value > date.today():
            raise ValueError("published_date cannot be in the future")
        return value

class UserRegistration(BaseModel):
    username: str
    password: str = Field(min_length=6)

def log_new_book(title: str):
    with LOG_FILE.open("a", encoding="utf-8") as f:
        f.write(f"New Book Added: {title}\n")

@app.get("/")
async def root():
    return {"message": "BookFlow API is running"}

@app.post("/books")
async def create_book(book: BookSchema, background_tasks: BackgroundTasks):
    # In the full PostgreSQL version, save through SQLAlchemy AsyncSession here.
    background_tasks.add_task(log_new_book, book.title)
    return {"message": "Book accepted", "book": book}

@app.post("/register")
async def register(user: UserRegistration):
    hashed_password = pwd_context.hash(user.password)
    return {
        "username": user.username,
        "hashed_password": hashed_password
    }

@app.get("/books/search")
async def search_books(q: str):
    # Replace this placeholder with an async pgvector query using <=>.
    if not q.strip():
        raise HTTPException(status_code=400, detail="Query cannot be empty")
    return {"query": q, "results": []}
