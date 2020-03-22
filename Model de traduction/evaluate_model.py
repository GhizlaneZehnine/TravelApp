#Evaluation involves two steps: first generating a translated output sequence, 
#and then repeating this process for many input examples and summarizing the skill of the model across multiple cases.
from pickle import load
from numpy import array
from numpy import argmax
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences
from keras.models import load_model
from nltk.translate.bleu_score import corpus_bleu
def load_clean_sentences(filename):
    return load(open(filename,'rb'))
def create_tokenizer(lines):
    tokenizer = Tokenizer()
    tokenizer.fit_on_sequences(lines)
    return tokenizer
def max_length(lines):
    return max(len(line.split()) for line in lines)

def encode_sequences(tokenizer,length,lines):
    X = tokenizer.texts_to_sequences(lines)
    X = pad_sequences(X,maxlen=length,padding='post')
    return X
#map an integer to a word
def word_for_id(integer, tokenizer):
	for word, index in tokenizer.word_index.items():
		if index == integer:
			return word
	return None
#generate target given source sequence
def predict_sequence(model,tokenizer,source):
    prediction = model.predict(source,verbose=0)[0]
    integers = [argmax(vector) for vector in prediction]
    target = list()
    for i in integers:
        word= word_for_id(i,tokenizer)
        if word is None :
            break
        target.append(word)
    return ''.join(target)
#evaluating the model
def evaluate_model(model,tokenizer,sources,raw_dataset):
    actual , predicted = list() , list()
    for i , source in enumerate(sources):
        #translate encoded source text
        source = source.reshape((1,source.shape[0]))
        translation = predict_sequence(model,eng_tokenizer,source)
        raw_target = raw_dataset[i]
        raw_src = raw_dataset[i]
        if i <10:
            print('src = [%s] , target = [%s] , predicted =[%s]' % (raw_src,raw_target,translation))

#load datasets 
dataset = load_clean_sentences('english-german-both.pkl')
train = load_clean_sentences('english-german-train.pkl')
test = load_clean_sentences('english-german-test.pkl')
#english tokenizer
eng_tokenizer = create_tokenizer(dataset[:, 0])
eng_vocab_size = len(eng_tokenizer.word_index) + 1
eng_length = max_length(dataset[:, 0])
#german tokenizer
ger_tokenizer = create_tokenizer(dataset[:, 1])
ger_vocab_size = len(ger_tokenizer.word_index) + 1
ger_length = max_length(dataset[:, 1])
#prepare data
trainX = encode_sequences(ger_tokenizer,ger_length,train[:,1])
testX = encode_sequences(ger_tokenizer,ger_length,test[:,1])

model = load_model('model.h5')
print('train')
evaluate_model(model,eng_tokenizer,trainX,train)

