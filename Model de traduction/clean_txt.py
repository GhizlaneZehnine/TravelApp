import string
import re
from pickle import dump
from unicodedata import normalize
from numpy import array
#preparing data & cleaning text
#load data into memory
def load_doc(filename):
	file = open(filename,mode='rt',encoding='utf-8')
	text = file.read()
	file.close()
	return text
#split doc into sentences
def to_pairs(doc):
	lines = doc.strip().split('\n')
	pairs = [line.split('\t') for line in lines]
	return pairs
#clean a list of lines
def clean_pairs(lines):
	cleaned = list()
	# prepare regex for char filtering => used for text pattern
	re_print = re.compile('[^%s]' % re.escape(string.printable))
	#prepare translation table for removing punctuation
	table = str.maketrans('','',string.punctuation)
	for pair in lines:
		clean_pair = list()
		for line in pair: 
			#normalize unicode characters
			line = normalize('NFD',line).encode('ascii','ignore')
			line = line.decode('UTF-8')
			#tokenize on white space
			line = line.split()
			#convert to lowercase
			line = [word.lower() for word in line]
			#remove punctuation from each token
			line = [word.translate(table) for word in line]
			#remove tokens with numbers in them
			line = [word for word in line if word.isalpha()]
			#store as a  string
			clean_pair.append(''.join(line))
		cleaned.append(clean_pair)
	return array(cleaned)
#save a list of clean sentences to file
def save_clean_data(sentences, filename):
	dump(sentences,open(filename,'wb'))
	print('Saved: %s' % filename)
#loading data #####
filename ='deu.txt'
doc = load_doc(filename)
#split into english-german pairs
pairs = to_pairs(doc)
#clean sentences
clean_pairs = clean_pairs(pairs)
#save clean pairs to file
save_clean_data(clean_pairs,'english-german.pkl')













