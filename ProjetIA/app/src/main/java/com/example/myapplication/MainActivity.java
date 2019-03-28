package com.example.myapplication;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button submit;
    EditText nom, prenom, age, mail;
    String list = "liste.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        mail = findViewById(R.id.mail);
        age = findViewById(R.id.age);
        submit = findViewById(R.id.envoyer);
        radioGroup = findViewById(R.id.radioGroup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void ecrireFichier(String inscription) {
        FileOutputStream file = null;
        try {
            file = openFileOutput(list, Context.MODE_PRIVATE);
            file.write(inscription.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Validation(View view) {
        if (nom.getText().toString().isEmpty()
                || mail.getText().toString().isEmpty()
                || prenom.getText().toString().isEmpty()
                || age.getText().toString().isEmpty()
                || (radioGroup.getCheckedRadioButtonId() != R.id.genre_m && radioGroup.getCheckedRadioButtonId() != R.id.genre_mme)) {
            Toast.makeText(getApplicationContext(), "Entrer toutes les informations.", Toast.LENGTH_SHORT).show();
        } else {
            radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
            OkHttpClient client = new OkHttpClient();
            String url = "http://82.231.49.191:8080/customer/add?"
                    + "genre=" + radioButton.getText()
                    + "&prenom=" + prenom.getText().toString()
                    + "&nom=" + nom.getText().toString()
                    + "&email=" + mail.getText().toString()
                    + "&age=" + age.getText().toString();
            final Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "connection au server impossible.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.body().string().equals("true")) {
                        Intent setting = new Intent(getApplicationContext(), Setting.class);
                        setting.putExtra("mail", mail.getText().toString());
                        startActivity(setting);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Compte déjà existant.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent connexion = new Intent(getApplicationContext(), connexion.class);
        startActivity(connexion);
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
