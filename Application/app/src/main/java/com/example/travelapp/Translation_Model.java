package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import org.tensorflow.lite.Interpreter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Translation_Model extends AppCompatActivity {
    private String output;
    private String input;
    private EditText input_text;
    private TextView output_text;
    private Button translate,online;
    private  MappedByteBuffer tflite_model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileInputStream f_input_stream= null;
        try {
            f_input_stream = new FileInputStream(new File("/Users/othmane zarai/Desktop/Projet_Android/tf_lite_model.tflite"));
            FileChannel f_channel = f_input_stream.getChannel();
             tflite_model = f_channel.map(FileChannel.MapMode.READ_ONLY, 0, f_channel .size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.translation__model);
        input_text = findViewById(R.id.input_text);
        output_text = findViewById(R.id.output_text);
        translate = findViewById(R.id.translate);
        online = findViewById(R.id.online);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = input_text.getText().toString();
                Interpreter interpreter = new Interpreter(tflite_model);
                interpreter.run(input,output);
                output_text.setText(output);
            }

        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Translation_Model.this,Translation_Activity.class);
                startActivity(i);
            }
        });
    }
}
