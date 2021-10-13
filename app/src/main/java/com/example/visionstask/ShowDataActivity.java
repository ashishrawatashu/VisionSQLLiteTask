package com.example.visionstask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.visionstask.databinding.ActivityAddDataBinding;
import com.example.visionstask.databinding.ActivityShowDataBinding;

import java.util.ArrayList;

public class ShowDataActivity extends AppCompatActivity implements ItemLongClick, TextWatcher, View.OnClickListener {

    ActivityShowDataBinding activityShowDataBinding;
    DBHandler dbHandler;
    ArrayList<UserDataPojo> userDataPojoArrayList;
    ShowDataAdapter showDataAdapter;
    ArrayList<UserDataPojo> selectedUserForDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityShowDataBinding = ActivityShowDataBinding.inflate(getLayoutInflater());
        View view = activityShowDataBinding.getRoot();
        setContentView(view);

        getUserData();
        activityShowDataBinding.searchET.addTextChangedListener(this);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getUserData() {
        dbHandler = new DBHandler(ShowDataActivity.this);
        activityShowDataBinding.deleteIV.setOnClickListener(this);
        userDataPojoArrayList = new ArrayList<>();
        userDataPojoArrayList = dbHandler.fetchUserData();
        if (userDataPojoArrayList.isEmpty()) {
            finish();
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("userDataPojoArrayList", "" + userDataPojoArrayList.size());
            showDataAdapter = new ShowDataAdapter(ShowDataActivity.this, userDataPojoArrayList, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowDataActivity.this, RecyclerView.VERTICAL, false);
            activityShowDataBinding.userListRV.setLayoutManager(linearLayoutManager);
            activityShowDataBinding.userListRV.setAdapter(showDataAdapter);
            showDataAdapter.notifyDataSetChanged();

        }

        activityShowDataBinding.deleteIV.setVisibility(View.GONE);
        activityShowDataBinding.searchET.setVisibility(View.VISIBLE);
        activityShowDataBinding.searchET.getText().clear();
        selectedUserForDelete.clear();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void itemLongClick(ArrayList<UserDataPojo> userDataPojoArrayList) {
//        dbHandler.deleteUser(userDataPojoArrayList.get(position).getName());
//        userDataPojoArrayList.remove(position);
//        showDataAdapter.notifyDataSetChanged();
//        Toast.makeText(this, "User Deleted !", Toast.LENGTH_SHORT).show();
//        if (userDataPojoArrayList.isEmpty()){
//            Toast.makeText(this, "No Data Found !", Toast.LENGTH_SHORT).show();
//        }
        selectedUserForDelete = userDataPojoArrayList;
        if (!selectedUserForDelete.isEmpty()) {
//            Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
            activityShowDataBinding.searchET.setVisibility(View.GONE);
            activityShowDataBinding.deleteIV.setVisibility(View.VISIBLE);
        } else {
            activityShowDataBinding.deleteIV.setVisibility(View.GONE);
            activityShowDataBinding.searchET.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        showDataAdapter.getFilter().filter(s);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete_IV) {
            Log.d("selectedUserForDelete", "" + selectedUserForDelete.size());
            dbHandler.deleteUser(selectedUserForDelete);
            Toast.makeText(this, "Deleted !", Toast.LENGTH_SHORT).show();
            getUserData();

        }
    }
}