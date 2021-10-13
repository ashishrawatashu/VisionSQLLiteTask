package com.example.visionstask;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.visionstask.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        activityMainBinding.addDataBT.setOnClickListener(this);
        activityMainBinding.showDataBT.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addData_BT:
                Intent intent = new Intent(this,AddDataActivity.class);
                startActivity(intent);
                break;

            case R.id.showData_BT:
                Intent intent2 = new Intent(this,ShowDataActivity.class);
                startActivity(intent2);
                break;
        }

    }
}