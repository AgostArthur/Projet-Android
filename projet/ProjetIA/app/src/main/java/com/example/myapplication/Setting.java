package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Setting extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        radioGroup = findViewById(R.id.radioGroup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void goToMaps(View view) {
        radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        Intent map = new Intent(getApplicationContext(), MapsActivity.class);
        map.putExtra("mail", getIntent().getStringExtra("mail"));
        map.putExtra("setting", radioButton.getText().toString());
        startActivity(map);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}