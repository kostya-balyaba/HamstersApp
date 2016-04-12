package com.hamstersapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hamstersapp.api.GETHamstersRequest;
import com.hamstersapp.api.GETHamstersResponse;
import com.hamstersapp.api.RequestResultCallback;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        new GETHamstersRequest(this, new GETHamstersResponse()).execute();
    }

}
