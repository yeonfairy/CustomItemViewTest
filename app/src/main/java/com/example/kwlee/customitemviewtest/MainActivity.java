package com.example.kwlee.customitemviewtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static MyAdapter adapter;
    static MyAdapter adapter2;
    static MyAdapter adapter3;
    ImageView imageView;
    private MainBackPressCloseHandler mainBackPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBackPressCloseHandler = new MainBackPressCloseHandler(this);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1) ;
        tabHost1.setup() ;

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.content1) ;
        ts1.setIndicator("강아지") ;
        tabHost1.addTab(ts1)  ;

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.content2) ;
        ts2.setIndicator("피자") ;
        tabHost1.addTab(ts2) ;

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3") ;
        ts3.setContent(R.id.content3) ;
        ts3.setIndicator("과일") ;
        tabHost1.addTab(ts3) ;


        // 데이터 원본 준비
        ArrayList<MyItem> data = new ArrayList<MyItem>();
        data.add(new MyItem(R.drawable.sample_0, "Bella", "1살"));
        data.add(new MyItem(R.drawable.sample_1, "Charlie", "2살"));
        data.add(new MyItem(R.drawable.sample_2, "Daisy", "1.5살"));
        data.add(new MyItem(R.drawable.sample_3, "Duke", "1살"));
        data.add(new MyItem(R.drawable.sample_4, "Max", "2살"));
        data.add(new MyItem(R.drawable.sample_5, "Happy", "4살"));
        data.add(new MyItem(R.drawable.sample_6, "Luna", "3살"));
        data.add(new MyItem(R.drawable.sample_7, "Bob", "2살"));
        ArrayList<MyItem> data2 = new ArrayList<MyItem>();
        data2.add(new MyItem(R.drawable.sample_8, "피자_1", "10000원"));
        data2.add(new MyItem(R.drawable.sample_9, "피자_2", "20000원"));
        data2.add(new MyItem(R.drawable.sample_10, "피자_3", "1.5000원"));
        data2.add(new MyItem(R.drawable.sample_11, "피자_4", "10000원"));
        data2.add(new MyItem(R.drawable.sample_12, "피자_5", "20000원"));
        data2.add(new MyItem(R.drawable.sample_13, "피자_6", "40000원"));
        data2.add(new MyItem(R.drawable.sample_14, "피자_7", "30000원"));
        data2.add(new MyItem(R.drawable.sample_15, "피자_8", "20000원"));
        data2.add(new MyItem(R.drawable.sample_16, "피자_9", "30000원"));
        data2.add(new MyItem(R.drawable.sample_17, "피자_10", "20000원"));
        ArrayList<MyItem> data3 = new ArrayList<MyItem>();
        data3.add(new MyItem(R.drawable.sample_20, "과일_1", "10000원"));
        data3.add(new MyItem(R.drawable.sample_21, "과일_2", "20000원"));
        data3.add(new MyItem(R.drawable.sample_22, "과일_3", "1.5000원"));
        data3.add(new MyItem(R.drawable.sample_23, "과일_4", "10000원"));
        data3.add(new MyItem(R.drawable.sample_24, "과일_5", "20000원"));
        data3.add(new MyItem(R.drawable.sample_25, "과일_6", "40000원"));
        data3.add(new MyItem(R.drawable.sample_26, "과일_7", "30000원"));
        data3.add(new MyItem(R.drawable.sample_27, "과일_8", "20000원"));
        data3.add(new MyItem(R.drawable.sample_28, "과일_9", "30000원"));
        data3.add(new MyItem(R.drawable.sample_29, "과일_10", "20000원"));
        data3.add(new MyItem(R.drawable.sample_30, "과일_11", "30000원"));
        data3.add(new MyItem(R.drawable.sample_31, "과일_11", "20000원"));
        // 어댑터 생성
        adapter = new MyAdapter(this, R.layout.item, data);
        adapter2 = new MyAdapter(this, R.layout.item, data2);
        adapter3 = new MyAdapter(this, R.layout.item, data3);
        // 어댑터 연결
        ListView listView = (ListView)findViewById(R.id.listView1);
        ListView listView2 = (ListView)findViewById(R.id.listView2);
        ListView listView3 = (ListView)findViewById(R.id.listView3);
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);

        // 항목 선택 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked,
                                    int position, long id) {
                //   String name = (String) ((TextView)vClicked.findViewById(R.id.textItem1)).getText();
                String name = ((MyItem)adapter.getItem(position)).nName;
                int img = ((MyItem)adapter.getItem(position)).mIcon;
                String age = ((MyItem)adapter.getItem(position)).nAge;
                /*imageView를 byteArray로 변환*/

                //toast로 알림창 보이기
                Toast.makeText(MainActivity.this, name + " selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("age", age);
                intent.putExtra("img", img);
                startActivityForResult(intent, 101);
            }
        });
        // 항목 선택 이벤트 처리
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked,
                                    int position, long id) {
                //   String name = (String) ((TextView)vClicked.findViewById(R.id.textItem1)).getText();
                String name = ((MyItem)adapter2.getItem(position)).nName;
                int img = ((MyItem)adapter2.getItem(position)).mIcon;
                String age = ((MyItem)adapter2.getItem(position)).nAge;
                /*imageView를 byteArray로 변환*/

                //toast로 알림창 보이기
                Toast.makeText(MainActivity.this, name + " selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("age", age);
                intent.putExtra("img", img);
                startActivityForResult(intent, 101);
            }
        });
        // 항목 선택 이벤트 처리
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked,
                                    int position, long id) {
                //   String name = (String) ((TextView)vClicked.findViewById(R.id.textItem1)).getText();
                String name = ((MyItem)adapter3.getItem(position)).nName;
                int img = ((MyItem)adapter3.getItem(position)).mIcon;
                String age = ((MyItem)adapter3.getItem(position)).nAge;
                /*imageView를 byteArray로 변환*/

                //toast로 알림창 보이기
                Toast.makeText(MainActivity.this, name + " selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("age", age);
                intent.putExtra("img", img);
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101) {
            String name = data.getStringExtra("nName");
            String image = data.getStringExtra("mIcon");
            String age = data.getStringExtra("nAge");
        }
    }

    @Override
    public void onBackPressed() {

         //   mainBackPressCloseHandler.onBackPressed();

            // AlertDialog 빌더를 이용해 종료시 발생시킬 창을 띄운다
            AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
            alBuilder.setMessage("종료하시겠습니까?");

            // "예" 버튼을 누르면 실행되는 리스너
            alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish(); // 현재 액티비티를 종료한다. (MainActivity에서 작동하기 때문에 애플리케이션을 종료한다.)
                }
            });
            // "아니오" 버튼을 누르면 실행되는 리스너
            alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return; // 아무런 작업도 하지 않고 돌아간다
                }
            });
            alBuilder.setTitle("프로그램 종료");
            alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
        }
    }

