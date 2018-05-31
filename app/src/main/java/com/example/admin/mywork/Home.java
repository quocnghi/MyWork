package com.example.admin.mywork;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

    @Override   //database
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

        // INSERT database code
//        database.QueryData("INSERT INTO CongViec VALUES(null,'Đi Học') ");



        GetdataCongViec();
    }
    //dialog xóa
    public void DialogXoaCV(final String tencv, final int id){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa" +tencv+ "không ?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM CongViec WHERE Id = '"+ id +"'");
                Toast.makeText(Home.this, "Đã Xóa" + tencv, Toast.LENGTH_SHORT).show();
                GetdataCongViec(); //load lại dữ liệu
            }
        });

        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogXoa.show();
    }

    //function dialog sửa
    public void DialogSuaCV(String ten, final int id){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua);

        final EditText edtTenCv = (EditText) dialog.findViewById(R.id.editTextTenCvEdit);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.buttonXacNhanEdit);
        Button btnHuy = (Button) dialog.findViewById(R.id.buttonHuyEdit);

        edtTenCv.setText(ten);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenmoi = edtTenCv.getText().toString();
                database.QueryData("UPDATE CongViec SET TenCV = '"+ tenmoi +"' WHERE Id = '"+ id +"' ");
                Toast.makeText(Home.this,"Đã cập nhật", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetdataCongViec(); //thấy dữ liệu đã cập nhật
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();
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
                    Toast.makeText(Home.this,"Đã thêm "+ tencv +"", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetdataCongViec();
                }
            }
        });


        dialog.show();
    }


}
