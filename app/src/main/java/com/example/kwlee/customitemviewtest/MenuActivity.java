package com.example.kwlee.customitemviewtest;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item2);
        //메뉴 추가
        BottomNavigationView bottomNavigationView2 = findViewById(R.id.bottom_navigation_detail);
        bottomNavigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //             FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.action_update: {
                        Toast.makeText(MenuActivity.this, "수정 selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.action_delete: {
                        Toast.makeText(MenuActivity.this, "삭제 selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return false;
            }
        });
        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기

        actionBar.setTitle("상세조회");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        Intent intent = getIntent(); //이전 액티비티에서 수신할 데이터를 받기위해 선언

        String Receive = intent.getStringExtra("name");       //호출한 액티비티에서 String 이라는 키 값을 가진 데이터 로드
        String Receive2 = intent.getStringExtra("age");       //호출한 액티비티에서 String 이라는 키 값을 가진 데이터 로드
        String Receive3 = intent.getStringExtra("img");       //호출한 액티비티에서 String 이라는 키 값을 가진 데이터 로드

        TextView textView = (TextView)findViewById(R.id.textItem1);
        textView.setText(Receive);              //텍스트 뷰에 받은 데이터 출력
        TextView textView2 = (TextView)findViewById(R.id.textItem2);
        textView2.setText(Receive2);        //텍스트 뷰에 받은 데이터 출력
        ImageView imageview = (ImageView)findViewById(R.id.iconItem);
        imageview.setImageResource(intent.getIntExtra("img", 0));

        Log.i("Receive Data : ",Receive);
        Log.i("Receive Data : ",Receive2);
    }
    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_navigation_detail, menu);
        return true;
    }
}

