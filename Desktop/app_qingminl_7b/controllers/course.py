from app import app
from app import db
from flask import Flask, jsonify, request, abort
from datetime import datetime
from models.Course import Course


@app.route('/teachers/teacherId/create_course', methods=['POST'])
def create_course():
    course = Course(course_ID=request.json['course_ID'],
                    name=request.json['name'],
                    description=request.json['description'],
                    price=request.json['price'],
                    ).save()
    return jsonify({'course': Course.objects.all()}), 201


@app.route('/courses', methods=['GET'])
def get_courses():
    return jsonify({'courses': Course.objects.all()}), 201


@app.route('/get_course_sort_pagination', methods=['GET'])
def get_course_sort_pagination():
    sortby = request.args.get("sortby", type=str, default="name")
    begin = request.args.get("begin", type=int, default=1)
    count = request.args.get("count", type=int, default=5)
    offset = (begin - 1) * count
    return jsonify({'course': Course.objects.limit(count).skip(offset).order_by(sortby)}), 201


@app.route('/get_course/<string:course_ID>', methods=['GET'])
def get_course(course_ID):
    print(course_ID)
    return jsonify({'students': Course.objects(course_ID=course_ID)}), 201


@app.route('/delete_courses/<string:course_ID>', methods=['DELETE'])
def delete_course(course_ID):
    Course.objects(course_ID=course_ID).update(is_deleted="Yes")
    return jsonify({'course': Course.objects.all()}), 201


@app.route('/update_course/<string:course_ID>', methods=['PATCH'])
def update_course(course_ID):
    if 'name' in request.json:
        Course.objects(course_ID=course_ID).update(name=request.json['name'])
    if 'description' in request.json:
        Course.objects(course_ID=course_ID).update(description=request.json['description'])
    if 'price' in request.json:
        Course.objects(course_ID=course_ID).update(price=request.json['price'])

    return jsonify({'course': Course.objects.all()}), 201


@app.route('/find_course_by_teacher/<string:teacher>', methods=['GET'])
def find_course_by_teacher(teacher):
    return jsonify({'course': Course.objects(teacher=teacher)}), 201
