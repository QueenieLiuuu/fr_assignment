from app import db


class PaymentMethod(db.Document):
    method = db.StringField(required=False)
