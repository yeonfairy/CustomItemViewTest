package com.example.kwlee.customitemviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class AddPhotoActivity extends AppCompatActivity {
    private Button btnFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        btnFilter = (Button) findViewById(R.id.addphoto_btn_upload);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopup(v);
            }
        });
        // ...
        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //             FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.action_home: {
                        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    }
                    case R.id.action_search: {
                        //                     Intent photoIntent = new Intent(getApplicationContext(), AddPhotoActivity.class);
                        //                     startActivity(photoIntent);
                    }
                    case R.id.action_add_photo: {
                        Intent photoIntent = new Intent(getApplicationContext(), AddPhotoActivity.class);
                        startActivity(photoIntent);
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        // Inflate the menu from xml
        popup.inflate(R.menu.menu);
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_library:
                        Toast.makeText(AddPhotoActivity.this, "라이브러리 selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_camera:
                        Toast.makeText(AddPhotoActivity.this, "카메라 selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_url:
                        Toast.makeText(AddPhotoActivity.this, "URL selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
}






