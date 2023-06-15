from pydantic import BaseModel, Field
from typing import Generic, ParamSpec, TypeVar

from fastapi import FastAPI
from fastapi.openapi.docs import (
    get_redoc_html,
    get_swagger_ui_html,
)
from fastapi.openapi.utils import get_openapi
from fastapi.staticfiles import StaticFiles
from datetime import datetime, date, time
# https://fastapi.tiangolo.com/advanced/extending-openapi/
import json

class Credit(BaseModel):
    amount: int

class Debit(BaseModel):
    amount: int

class State(BaseModel):
    credits: list[Credit]
    debits: list[Debit]

app = FastAPI(docs_url=None, redoc_url=None)

state = State(credits=[Credit(amount=201)], debits=[Debit(amount=1)])

@app.get("/api/v1/state", response_model=State)
async def get_state() -> State:
    global state
    return state

@app.post("/api/v1/state", response_model=State)
async def set_state(st: State) -> State:
    global state
    state = st
    return state

def custom_openapi():
    if app.openapi_schema:
        return app.openapi_schema
    openapi_schema = get_openapi(
        title="Service",
        version="0.1.0",
        description="OpenAPI schema",
        routes=app.routes,
    )
    app.openapi_schema = openapi_schema
    return app.openapi_schema


app.openapi = custom_openapi

app.mount("/static", StaticFiles(directory="static"), name="static")

@app.get("/docs", include_in_schema=False)
async def custom_swagger_ui_html():
    return get_swagger_ui_html(
        openapi_url=app.openapi_url,
        title="Swagger UI",
        # oauth2_redirect_url=app.swagger_ui_oauth2_redirect_url,
        swagger_js_url="/static/swagger-ui-bundle.js",
        swagger_css_url="/static/swagger-ui.css",
    )


