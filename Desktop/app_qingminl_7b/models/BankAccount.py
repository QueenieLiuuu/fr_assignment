from app import db


class BankAccount(db.Document):
    routingNumber = db.StringField(required=False)
    accountNumber = db.StringField(required=False)
