import re
import spacy
from spacy.cli import download
download("en_core_web_sm")

en = spacy.load('en_core_web_sm')
sw_spacy = en.Defaults.stop_words

def removingStopWords(sentence):
    words = [word for word in sentence.split() if word.lower() not in sw_spacy]
    new_text = " ".join(words)
    return new_text

def cleanResume(sentence):
    sentence = re.sub(r'[^\x00-\x7f]',r' ', sentence)
    sentence = re.sub('#\S+', '', sentence)  # remove hashtags
    sentence = re.sub('@\S+', '  ', sentence)  # remove mentions
    sentence = re.sub('[%s]' % re.escape("""!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~"""), ' ', sentence)
    sentence = removingStopWords(sentence)
    sentence = re.sub('\s+', ' ', sentence)  # remove extra whitespace
    return sentence


