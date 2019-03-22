package com.example.myapplication;

import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class connexion extends AppCompatActivity {

    EditText mail;
    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        mail = findViewById(R.id.mailC);
    }

    //lit le fichier liste.txt en mémoire interne du tel si déja créer
    public void connexion_serveur() throws IOException {
        String url = "http://192.168.43.115:8080/customer/findbyemail?email=" + mail.getText().toString();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().string().equals("true")) {
                    Intent setting = new Intent(getApplicationContext(), Setting.class);
                    setting.putExtra("mail", mail.getText().toString());
                    startActivity(setting);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Information de connexion incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    public void connexion_customer(View view) throws IOException {
        connexion_serveur();
    }

    public void goToInscription(View view) {
        Intent inscription = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(inscription);
        this.finish();
    }

    public void goToMaps(View view) {
        Intent Maps = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(Maps);
        this.finish();
    }
}
