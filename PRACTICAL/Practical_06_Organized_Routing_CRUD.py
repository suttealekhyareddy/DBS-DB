# PRACTICAL 06: ORGANIZED ROUTING & CRUD OPERATIONS
# Single-file implementation of the required FastAPI router structure.
# Install: pip install fastapi uvicorn
# Run: uvicorn Practical_06_Organized_Routing_CRUD:app --reload

from fastapi import FastAPI, APIRouter, HTTPException
from pydantic import BaseModel, Field

app = FastAPI(title="BookFlow Modular API")

catalog_router = APIRouter(prefix="/books", tags=["Public Catalog"])
admin_router = APIRouter(prefix="/admin", tags=["Librarian Admin"])

books = [
    {
        "book_id": 1,
        "title": "The Martian",
        "isbn": "9780553418026",
        "price": 499
    },
    {
        "book_id": 2,
        "title": "Dune",
        "isbn": "9780441172719",
        "price": 599
    }
]

class BookInput(BaseModel):
    title: str
    isbn: str
    price: float = Field(ge=0)

@catalog_router.get("/")
async def get_books():
    return books

@catalog_router.get("/{book_id}")
async def get_book(book_id: int):
    for book in books:
        if book["book_id"] == book_id:
            return book
    raise HTTPException(status_code=404, detail="Book not found")

@admin_router.post("/books")
async def add_book(book: BookInput):
    new_id = max((item["book_id"] for item in books), default=0) + 1
    new_book = {
        "book_id": new_id,
        **book.model_dump()
    }
    books.append(new_book)
    return new_book

@admin_router.put("/books/{book_id}")
async def update_book(book_id: int, book: BookInput):
    for index, existing in enumerate(books):
        if existing["book_id"] == book_id:
            updated = {
                "book_id": book_id,
                **book.model_dump()
            }
            books[index] = updated
            return updated
    raise HTTPException(status_code=404, detail="Book not found")

@admin_router.delete("/books/{book_id}")
async def delete_book(book_id: int):
    for index, book in enumerate(books):
        if book["book_id"] == book_id:
            deleted = books.pop(index)
            return {"message": "Book deleted", "book": deleted}
    raise HTTPException(status_code=404, detail="Book not found")

app.include_router(catalog_router)
app.include_router(admin_router)

@app.get("/")
async def root():
    return {"message": "BookFlow Modular API is running"}
