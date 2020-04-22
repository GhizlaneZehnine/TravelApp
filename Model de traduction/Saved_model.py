import tensorflow as tf

converter = tf.lite.TFLiteConverter.from_saved_model("/model.h5")
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_quant_model = converter.convert
open("translation_model_converter.tflite", "wb").write(tflite_quantized_model)