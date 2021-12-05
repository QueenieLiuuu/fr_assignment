#!/usr/bin/env python
# -*- coding: utf-8 -*-

from app import app
from flask import Flask, jsonify, request, abort
from datetime import datetime
from models.Device import Device

@app.route('/todo/api/v1/creat_task', methods=['POST'])
def create_device():
    if not request.json or not 'devicename' in request.json:
        print(request)
        abort(400)
    device = Device(devicename=request.json['devicename'], macaddr=request.json['macaddr']).save();
    return jsonify({'devices': Device.objects.all()}), 201

@app.route('/todo/api/v1/devices', methods=['GET'])
def get_devices():
    devices = Device.objects().all()
    print(devices)
    return jsonify({'tasks': devices}), 201



