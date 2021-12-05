from flask import Flask
from flask_mongoengine import MongoEngine

app = Flask(__name__)

connstr = "mongodb+srv://qingmin:Liu8888@cluster0.agciu.mongodb.net/myFirstDatabase?retryWrites=true&w=majority"

app.config['DEBUG'] = True
app.config['SECRET_KEY'] = "key"
app.config['JSON_AS_ASCII '] = False
app.config['MONGODB_HOST'] = connstr

# app.config.from_pyfile('config.json')
db = MongoEngine(app)

# 数据库对应的模型
import models

# api的业务逻辑
import controllers
