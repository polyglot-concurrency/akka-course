from typing import Any, Dict, List, Optional, Union
from os import environ, path

from pydantic import BaseSettings, PostgresDsn, validator, AnyUrl

PORT = 8000
BIND = '0.0.0.0'
WORKERS = 2
RELOAD = True
