package com.example.admin.mywork;

import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    Database database;
    ListView lvCongViec;
    ArrayList<CongViec> arrayCongViec;
    CongViecAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvCongViec = (ListView) findViewById(R.id.listviewCongViec);
        arrayCongViec = new ArrayList<>();

        adapter = new CongViecAdapter(this, R.layout.dong_cong_viec, arrayCongViec);
        lvCongViec.setAdapter(adapter);

        //tạo database
        database = new Database(this, "MyWork.sqlite", null, 1);

        //tạo bảng
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR(200))");

        //INSERT database
//        database.QueryData("INSERT INTO CongViec VALUES(null,'Đi Học') ");



        GetdataCongViec();
    }

    private void GetdataCongViec(){
        //select data
        Cursor dataCongViec = database.Getdata("SELECT * FROM CongViec");
        arrayCongViec.clear();     //ngắt lặp lại dữ liệu
        while (dataCongViec.moveToNext()){
            String ten = dataCongViec.getString(1);
//            Toast.makeText(this, ten,Toast.LENGTH_SHORT).show();
            int id = dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id, ten));
        }
        adapter.notifyDataSetChanged();
    }

    //icon add cong viec
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //gọi dialog thêm
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuAdd){
            DialogThem();
        }

       return super.onOptionsItemSelected(item);

    }
    private void DialogThem(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_them_cong_viec);

        final EditText edtTen = (EditText) dialog.findViewById(R.id.editTextCV);
        Button nutThem = (Button) dialog.findViewById(R.id.btnThem);
        Button nutHuy = (Button) dialog.findViewById(R.id.btnHuy);
         //nut huy
        nutHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        nutThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tencv =edtTen.getText().toString();
                if (tencv.equals("")){
                    Toast.makeText(Home.this,"Vui lòng nhập tên công việc!", Toast.LENGTH_SHORT).show();
                }else {
                    database.QueryData("INSERT INTO CongViec VALUES(null,'"+tencv+"') ");  //tách chuỗi để nối biến tencv
                    Toast.makeText(Home.this,"Vui lòng nhập tên công việc!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetdataCongViec();
                }
            }
        });


        dialog.show();
    }


}
