from app import db


class Rating(db.Document):
    ratingValue = db.StringField(required=True)
    ratingComments = db.StringField(required=False)
