from app import db
from models.BankAccount import BankAccount


class Teacher(db.Document):
    teacher_ID = db.StringField(required=True)

    first_name = db.StringField(required=True)
    last_name = db.StringField(required=True)

    email = db.StringField(required=True)
    phone = db.StringField(required=True)
    ratings = db.StringField(required=False)

    bankAccount = db.ReferenceField(BankAccount, required=False)

    is_deleted = db.StringField(required=False)
