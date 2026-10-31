@echo off
cd ai-service
python -m venv .venv
call .venv\Scripts\activate
pip install -r requirements.txt
uvicorn app:app --reload --port 8001
