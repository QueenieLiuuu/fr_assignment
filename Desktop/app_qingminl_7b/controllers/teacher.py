from app import app
from app import db
from flask import Flask, jsonify, request, abort
from datetime import datetime
from models.Teacher import Teacher


@app.route('/create_teacher', methods=['POST'])
def create_teacher():
    teacher = Teacher(teacher_ID=request.json['teacher_ID'],
                      first_name=request.json['first_name'], last_name=request.json['last_name'],
                      email=request.json['email'], phone=request.json['phone'],
                      ratings=request.json['ratings'], bankAccount=request.json['bankAccount']
                      ).save();
    return jsonify({'teacher': Teacher.objects.all()}), 201


@app.route('/teachers', methods=['GET'])
def get_teachers():
    return jsonify({'teachers': Teacher.objects.all()}), 201


@app.route('/get_teachers_sort_pagination', methods=['GET'])
def get_teachers_sort_pagination():
    sortby = request.args.get("sortby", type=str, default="first_name")
    begin = request.args.get("begin", type=int, default=1)
    count = request.args.get("count", type=int, default=5)
    offset = (begin - 1) * count
    # return jsonify({'teachers': Teacher.objects.all().order_by(sortby)}), 201
    return jsonify({'teachers': Teacher.objects.limit(count).skip(offset).order_by(sortby)}), 201


@app.route('/get_teacher/<string:teacher_ID>', methods=['GET'])
def get_teacher(teacher_ID):
    print(teacher_ID)
    return jsonify({'teachers': Teacher.objects(teacher_ID=teacher_ID)}), 201


@app.route('/delete_teacher/<string:teacher_ID>', methods=['DELETE'])
def delete_teacher(teacher_ID):
    Teacher.objects(teacher_ID=teacher_ID).update(is_deleted="Yes")
    return jsonify({'teacher': Teacher.objects.all()}), 201


@app.route('/update_teacher/<string:teacher_ID>', methods=['PATCH'])
def update_teacher(teacher_ID):
    if 'first_name' in request.json:
        Teacher.objects(teacher_ID=teacher_ID).update(first_name=request.json['first_name'])
    if 'last_name' in request.json:
        Teacher.objects(teacher_ID=teacher_ID).update(last_name=request.json['last_name'])
    if 'email' in request.json:
        Teacher.objects(teacher_ID=teacher_ID).update(email=request.json['email'])
    if 'phone' in request.json:
        Teacher.objects(teacher_ID=teacher_ID).update(phone=request.json['phone'])
    if 'ratings' in request.json:
        Teacher.objects(teacher_ID=teacher_ID).update(ratings=request.json['ratings'])

    return jsonify({'teacher': Teacher.objects.all()}), 201


@app.route('/find_teacher_by_email/<string:email>', methods=['GET'])
def find_teacher_by_email(email):
    return jsonify({'teacher': Teacher.objects(email=email)}), 201
