package com.example.travelapp;
import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
public class Translation_Activity extends AppCompatActivity {
    static String language_name;
    public final String[] language_names = {"Afrikaans","Arabic","Belarusian","Bulgarian","Czech","German",
            "English","Spanish","French","Italian","Japanese","Russian","Chinese"
    };
    public EditText input_text;
    public TextView output_text;
    public Button translate;
    public String source_text;
    private  Spinner spinner;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.off_line_model : //Return off_line model
                break;
            default : break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.translation_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_main);
        input_text = findViewById(R.id.input_text);
        output_text = findViewById(R.id.output_text);
        translate = findViewById(R.id.translate);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new SpinnerActivity());
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,language_names);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indentifyLanguage();
            }
        });
    }

    public void indentifyLanguage() {
        source_text = input_text.getText().toString();
        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        identifier.identifyLanguage(source_text).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String langCode_String) {
                int langCode = FirebaseTranslateLanguage.languageForLanguageCode(langCode_String);
                translateText(langCode);
            }
        });
    }
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
            language_name = language_names[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public void translateText(int langCode){
        FirebaseTranslatorOptions options;
        switch (language_name){
            case "Afrikaans" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.AF).build(); break;
            case "Arabic" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.AR).build(); break;
            case "Belarusian" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.BE).build(); break;
            case "Bulgarian" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.BG).build(); break;
            case "Czech" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.CS).build(); break;
            case "German" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.DE).build(); break;
            case "English" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.EN).build(); break;
            case "Spanish" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.ES).build(); break;
            case "French" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.FR).build(); break;
            case "Italian" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.IT).build(); break;
            case "Japanese" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.JA).build(); break;
            case "Russian" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.RU).build(); break;
            case "Chinese" :   options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.ZH).build(); break;
            default: options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(FirebaseTranslateLanguage.ID).build(); break;
        }
        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(source_text).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        System.out.println(""+s);
                        output_text.setText(s);
                    }
                });
            }
        });
    }
}