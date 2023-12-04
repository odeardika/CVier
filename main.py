from flask import Flask, request, jsonify, flash, redirect
from pdf_converter import pdf_to_text
import os

app = Flask(__name__)

@app.route('/')
def home():
    return 'Cvier'

@app.route("/cv-converter", methods=['POST'])
def cv_converter():
    if 'file' not in request.files:
        return jsonify({
            'message' : 'No file part in the request'
        }), 400
    file = request.files['file']
    if file.filename == '':
        return jsonify({
            'message' : 'No file selected for uploading'
        }), 400
    temp_cv = f'{os.getcwd()}'
    file_path = os.path.join(temp_cv,'temp.pdf')
    file.save(file_path)
    
    text = pdf_to_text(file_path)
    
    return jsonify({
        'text' : text
    }), 201

if __name__ == "__main__":
    app.run(debug=True)