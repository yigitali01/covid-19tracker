package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setTitle(R.string.about_string);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void openLinkedin(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/yi%C4%9Fit-ali-delibudak-11b5451a3/")));
    }

    public void openGit(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yigitali01?tab=repositories")));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}