from app import db

class Device(db.Document):
    devicename = db.StringField()
    macaddr = db.StringField()

