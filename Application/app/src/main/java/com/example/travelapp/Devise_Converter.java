package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Devise_Converter extends AppCompatActivity {
    public static BreakIterator data;
    List<String> keysList;
    private Button convert;
    private EditText input_value;
    private TextView output_value;
    private Spinner toCurrency;
    private Spinner Currency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devise__converter);
        convert = findViewById(R.id.convert);
        input_value = findViewById(R.id.input_value);
        output_value = findViewById(R.id.output_value);
        toCurrency = findViewById(R.id.toCurrency);
        Currency = findViewById(R.id.spinner2);
        try {
            loadConvTypes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!input_value.getText().toString().isEmpty())
                {
                    String Curr = Currency.getSelectedItem().toString();
                    String toCurr = toCurrency.getSelectedItem().toString();
                    double Value = Double.valueOf(input_value.getText().toString());
                    Toast.makeText(Devise_Converter.this, "Please Wait..", Toast.LENGTH_SHORT).show();
                    try {
                        convertCurrency(Curr,toCurr,Value);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Devise_Converter.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Devise_Converter.this, "Please Enter a Value to Convert..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void loadConvTypes() throws IOException {

        String url = "https://api.exchangeratesapi.io/latest";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                Toast.makeText(Devise_Converter.this, mMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String mMessage = response.body().string();


                Devise_Converter.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(mMessage);
                            JSONObject  b = obj.getJSONObject("rates");

                            Iterator keysToCopyIterator = b.keys();
                            keysList = new ArrayList<String>();

                            while(keysToCopyIterator.hasNext()) {
                                String key = (String) keysToCopyIterator.next();
                                keysList.add(key);
                            }


                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, keysList );
                            spinnerArrayAdapter.add("MAD");
                            spinnerArrayAdapter.add("EUR");
                            toCurrency.setAdapter(spinnerArrayAdapter);
                            Currency.setAdapter(spinnerArrayAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }




        });
    }

    public void convertCurrency(final String Curr,final String toCurr, final double Value) throws IOException {

        String url = "https://api.exchangeratesapi.io/latest";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                Toast.makeText(Devise_Converter.this, mMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String mMessage = response.body().string();
                Devise_Converter.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            double output;
                            JSONObject obj = new JSONObject(mMessage);
                            JSONObject b = obj.getJSONObject("rates");
                            String val_to_curr = b.getString(toCurr);
                            String val_curr = b.getString(Curr);
                            if(Curr.equals("EUR")){
                                if(toCurr.equals("MAD")){
                                    output = Value*(11.01f);
                                    output_value.setText(String.valueOf(output));
                                }
                                else {
                                    output = Value * Double.valueOf(toCurr);
                                    output_value.setText(String.valueOf(output));
                                }
                            }
                            if(Curr.equals("MAD")){
                                if(toCurr.equals("EUR")){
                                    output = Value/(11.01f);
                                    output_value.setText(String.valueOf(output));}
                                else{
                                    output = Value*(Double.valueOf(val_to_curr)/(11.01));
                                    output_value.setText(String.valueOf(output));
                                }
                            }
                            else{
                                output = Value*(Double.valueOf(val_to_curr)/Double.valueOf(val_curr));
                                output_value.setText(String.valueOf(output));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}