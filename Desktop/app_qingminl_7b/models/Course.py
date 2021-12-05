from app import db
from models.Rating import Rating


class Course(db.Document):
    course_ID = db.StringField(required=True)

    name = db.StringField(required=True)
    description = db.StringField(required=True)
    price = db.StringField(required=True)
    image = db.StringField(required=False)

    # rating = db.ReferrenceField(Rating, required=False)

    is_deleted = db.StringField(required=False)
