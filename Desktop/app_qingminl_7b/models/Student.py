from app import db
from models.Order import Order
from models.PaymentMethod import PaymentMethod


class Student(db.Document):
    student_ID = db.StringField(required=True)

    name = db.StringField(required=True)
    email = db.StringField(required=True)
    phone = db.StringField(required=True)

    # paymentMethod = db.ReferrenceField(PaymentMethod, required=False)

    # order = db.ReferrenceField(Order, required=True)

    is_deleted = db.StringField(required=False)

