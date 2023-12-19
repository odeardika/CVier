from flask import Flask, request, jsonify, flash, redirect
from pdf_converter import pdf_to_text
import os
import tensorflow as tf
from tensorflow.keras.preprocessing.sequence import pad_sequences
from tensorflow.keras.models import load_model
import pickle
from preprocessing import cleanResume,removingStopWords
from quick_sort import quickSort
import pandas as pd

app = Flask(__name__)
app.config['ALLOWED_EXTENSIONS'] = set(['pdf'])
file = pd.read_csv('label.csv')
labels = file['label']

list_job = {
    '0' : {
        'skill' : {
            'Mathematics' : 5,
            'Data Science' : 15,
            'Data Engineer' : 15,
            'Machine Learning' : 65
        }
    },
    '1' : {
        'skill' : {
            'Software Developer' : 10,
            'Software Engineer' : 10,
            'Backend Engineer' : 10,
            'Frontend Developer' : 10,
            'Web Developer' : 60
        }
    },
    "2" : {
        'skill' : {
            'Software Developer' : 10,
            'Software Engineer' : 10,
            'Backend Engineer' : 10,
            'Frontend Developer' : 10,
            'Mobile Developer' : 60
        }
    },
    "3" : {
        'skill' : {
            'Cloud Computing' : 50,
            'Cloud Architect' : 50
        }
    }
}


def allowed_file(filename):
    return '.' in filename and filename.split('.', 1)[1] in app.config['ALLOWED_EXTENSIONS']

# load model and tokenizer
model_path = os.path.join(os.getcwd(),'cv_model.h5')
tokenizer_path = os.path.join(os.getcwd(),'tokenizer_json.pickle')
model = load_model(model_path)
with open(tokenizer_path, 'rb') as handle:
    tokenizer_json = pickle.load(handle)
tokenizer = tf.keras.preprocessing.text.tokenizer_from_json(
    tokenizer_json
)

@app.route('/')
def index():
    return jsonify({
        'status': {
            'code': 200,
            'message': 'Success conected to API'
        },
        'data': None
    }), 200

@app.route("/cv-predict", methods=['POST'])
def cv_predict():
    if 'file' not in request.files:
        return jsonify({
            'status': {
                'code' : 400,
                'message' : 'No file part in the request'
            }, 
            'data' : None
        }), 400
    if 'job' not in request.form:
        return jsonify({
            'status' : {
                'code' : 400,
                'message' : 'Job not selected yet'
            },
            'data' : None
        }), 400
    file = request.files['file']
    job = request.form['job']
    if file.filename == '':
        return jsonify({
            'status' : {
                'code' : 400,
                'message' : 'No file selected for uploading'
            },
            'data' : None
        }), 400
    
    # save file temporary    
    temp_cv = f'{os.getcwd()}'
    file_path = os.path.join(temp_cv,'temp.pdf')
    file.save(file_path)
    
    # convert pdf to text
    text = pdf_to_text(file_path)

    # preprocessing text
    text = cleanResume(removingStopWords(text))
    text = tokenizer.texts_to_sequences(text)
    text = pad_sequences(text)
    
    predict = model.predict(text)
    predict = predict[-1]
    predict_dict = []
    for i in range(len(predict)):
        predict_dict.append({
        'label index' : i,
        'label chance' : predict[i]
    })
    quickSort(predict_dict,0,len(predict_dict)-1)
    
    # calculate percentage
    skill = list_job[job]['skill']
    
    top_labels = predict_dict[:len(skill)]
    result = []
    for i in top_labels:
        result.append(labels[i['label index']])
    improve = []
    percentage = 0
    for i in skill:
        if i not in result:
            improve.append(i)
        else:
            percentage += skill[i]
    
    return jsonify({
        'status' : {
            'code' : 201,
            'massage' : 'Success predicting'
        },
        'data' : {
            'percentage' : percentage,
            'improve' : improve
        }
    }), 201

if __name__ == "__main__":
    app.run(debug=True,
            host="0.0.0.0",
            port=int(os.environ.get("PORT", 8080)))