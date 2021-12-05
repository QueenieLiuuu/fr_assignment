from app import app
from app import db
from flask import Flask, jsonify, request, abort
from datetime import datetime
from models.Student import Student


@app.route('/create_student', methods=['POST'])
def create_student():
    student = Student(student_ID=request.json['student_ID'],
                      name=request.json['name'],
                      email=request.json['email'],
                      phone=request.json['phone'],
                      # paymentMethods=request.json['paymentMethod']
                      ).save()
    return jsonify({'student': Student.objects.all()}), 201


@app.route('/students', methods=['GET'])
def get_students():
    return jsonify({'students': Student.objects.all()}), 201


@app.route('/get_student_sort_pagination', methods=['GET'])
def get_students_sort_pagination():
    sortby = request.args.get("sortby", type=str, default="name")
    begin = request.args.get("begin", type=int, default=1)
    count = request.args.get("count", type=int, default=5)
    offset = (begin - 1) * count
    return jsonify({'student': Student.objects.limit(count).skip(offset).order_by(sortby)}), 201


@app.route('/get_student/<string:student_ID>', methods=['GET'])
def get_student(student_ID):
    print(student_ID)
    return jsonify({'students': Student.objects(student_ID=student_ID)}), 201


@app.route('/delete_student/<string:student_ID>', methods=['DELETE'])
def delete_student(student_ID):
    Student.objects(student_ID=student_ID).update(is_deleted="Yes")
    return jsonify({'student': Student.objects.all()}), 201


@app.route('/update_student/<string:student_ID>', methods=['PATCH'])
def update_student(student_ID):
    if 'name' in request.json:
        Student.objects(student_ID=student_ID).update(name=request.json['name'])
    if 'email' in request.json:
        Student.objects(student_ID=student_ID).update(email=request.json['email'])
    if 'phone' in request.json:
        Student.objects(student_ID=student_ID).update(phone=request.json['phone'])

    return jsonify({'student': Student.objects.all()}), 201


@app.route('/find_student_by_email/<string:email>', methods=['GET'])
def find_student_by_email(email):
    return jsonify({'student': Student.objects(email=email)}), 201
