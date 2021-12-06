from mongoengine import *
from flask import Response
from flask import Flask
from flask_restful import Api, Resource, reqparse

app = Flask(__name__)
api = Api(app)

#data
books = \
    [
        { "id" : "A234", "name" : "The 101 Dalmations", "author" : "Dodie Smith"},
        { "id" : "A675", "name" : "The Adventures of Huckleberry Finn", "author" : "Mark Twain"},
        { "id" : "A212", "name" : "Bag of Bones", "author" : "Stephen King"},
        { "id" : "B671", "name" : "Charlie and the Chocolate Factory", "author" : "Roald Dahl"},
        { "id" : "B534", "name" : "Charlotte's Web", "author" : "E.B.White"},
        { "id" : "B777", "name" : "A Christmas Carol", "author" : "Charles Dickens"},
        { "id" : "B778", "name" : "Dracula", "author" : "Bram Stoker"},
        { "id" : "B812", "name" : "A Farewell to Arms", "author" : "Ernest Hemingway"},
        { "id" : "C101", "name" : "The Firm", "author" : "John Grisham"}
    ]

borrowers = \
    [
        {"id" : "L34", "name" : "Andrea Selleck", "phone" : "6395551239"},
        {"id" : "L22", "name" : "Lucas Hyatt", "phone" : "4085552365"},
        {"id" : "L19", "name" : "Carol Leonard", "phone" : "6505558921"},
        {"id" : "L84", "name" : "Ayesha Ford", "phone" : "4155552120"},
        {"id" : "L77", "name" : "Kenneth Trout", "phone" : "5105551982"}
    ]

#command
actions = ["checkout", "return", "exit", "books", "reset"]


#database
db_name = "app-qingminl"
db = ""



class Checkout(Resource):
    def get(self):
        get_parser = reqparse.RequestParser()
        get_parser.add_argument('bookId', type=str, required=True)
        get_parser.add_argument('borrowerId', type=str, required=True)
        args = get_parser.parse_args()
        res = process_action("checkout", args.borrowerId, args.bookId)
        return res

class Return(Resource):
    def get(self):
        get_parser = reqparse.RequestParser()
        get_parser.add_argument('bookId', type=str, required=True)
        get_parser.add_argument('borrowerId', type=str, required=True)
        args = get_parser.parse_args()
        res = process_action("return", args.borrowerId, args.bookId)
        return res

class Books(Resource):
    def get(self):
        res = process_action("books")
        return res

class Reset(Resource):
    def get(self):
        res = process_action("reset")
        return res



#create database, I use the cloud database
def create_db():
    db = connect(host = "mongodb+srv://qingmin:Liu8888@cluster0.vzvxo.mongodb.net/myFirstDatabase?retryWrites=true&w=majority")
    return db


# Identify database
class Borrower(Document):
    userid = StringField(required=True)
    name = StringField(max_length=100)
    phone = StringField(max_length=10)

class Book(Document):
    bookid = StringField(required=True)
    name = StringField(max_length=100)
    author = StringField(max_length=50)
    check_status = StringField(max_length=1)
    br = StringField(max_length=10)

def reset_db():
    global db
    global db_name
    db.drop_database(db_name)
    for i in range(0, len(books)):
        data = Book(bookid=books[i].get('id'), name=books[i].get('name'), author=books[i].get('author'), check_status='N', br = '')
        data.save()

    for i in range(0, len(borrowers)):
        data = Borrower(userid=borrowers[i].get('id'), name=borrowers[i].get('name'), phone=borrowers[i].get('phone'))
        data.save()

    return "Data has been reset"

def display_books_status():
    res = "book_id,title,author,checked_out,borrower_id,borrower_name\n"
    for data in Book.objects():
        br_name = ''
        if data.br:
            br_name = Borrower.objects(userid=data.br).first().name
        res = res + "%s, %s, %s, %s, %s, %s\n" %(data.bookid, data.name, data.author, data.check_status, data.br, br_name)

    return Response(res, mimetype='text/plain')


def process_action(action, borrower_id = "", book_id = ""):

    if action == "reset":
        res = reset_db()
        return res

    if action == "books":
        res = display_books_status()
        return res

    if not Borrower.objects(userid = borrower_id):
        res = "Borrower with Id %s does not exist" %str(borrower_id)
        return res

    if not Book.objects(bookid = book_id):
        res = "Book with Id %s does not exist" %(str(book_id))
        return res

    book_rec = Book.objects(bookid = book_id).first()
    borrower_rec = Borrower.objects(userid = borrower_id).first()



    if action == "checkout":
        if (book_rec.check_status =='Y'):
            res = ("\'%s\' is already checked out by someone." %(book_rec.name))
        else:
                book_rec.chk_status = 'Y'
                book_rec.br = borrower_id
                book_rec.save()
                res = ("\'%s\' has checked out \'%s\'" %(borrower_rec.name, book_rec.name))
        return res



    if action == "return":
        if not (book_rec.br == borrower_id):
            res = "\'%s\' has not currently checked out \'%s\'" %(borrower_rec.name, book_rec.name)
        else:
                book_rec.check_status = 'N'
                book_rec.br = ''
                book_rec.save()
                res = "\'%s\' has returned \'%s\'" %(borrower_rec.name, book_rec.name)
        return res


def main():
    global db
    api.add_resource(Checkout, '/checkout')
    api.add_resource(Return, '/return')
    api.add_resource(Reset, '/reset')
    api.add_resource(Books, '/books')
    db = create_db()

if __name__ == "__main__":
    main()
    app.run()
