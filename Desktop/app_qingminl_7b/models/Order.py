from app import db


class Order(db.Document):
    orderNumber = db.StringField(required=True)
    total = db.StringField(required=True)
    paymentMethod = db.StringField(required=True)
