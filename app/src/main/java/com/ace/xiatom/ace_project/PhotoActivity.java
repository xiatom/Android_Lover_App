package com.ace.xiatom.ace_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private mySQLite dbHelper;
    List<Picture> data = new ArrayList<>();
    @Override
    protected void onResume() {
        data = new ArrayList<>();
        initContacter(data);
        PictureAdapter adapter = new PictureAdapter(PhotoActivity.this, R.layout.image, data);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Button btn1 = (Button) findViewById(R.id.btn1);
        dbHelper = new mySQLite(this, "image.db", null, 2);
        db = dbHelper.getWritableDatabase();
        File file = new File(getFilesDir().getAbsolutePath() + "/image");
        if (!file.exists())
            file.mkdir();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PhotoActivity.this, ChoosephotoActivity.class);
                startActivity(intent);
            }
        });
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView=(ImageView)findViewById(R.id.image1);
                Picture picture=data.get(position);
                imgMax(picture.getImg());

            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                ImageView imageView=(ImageView)findViewById(R.id.image1);
                final Picture picture=data.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoActivity.this);
                builder.setIcon(R.drawable.ic_delete);//设置图标
                builder.setTitle("删除照片");//设置对话框的标题
                builder.setMessage("你确定要删除这张照片嘛吗？");//设置对话框的内容
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(PhotoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        deleteSDFile(picture.getFilename());
                        db.delete("image","filename=?",new String[]{picture.getFilename()});
                        onResume();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  //取消按钮

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(PhotoActivity.this,"取消", Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog b = builder.create();
                b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
                return  true;
            }
        });

    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }
    private void initContacter(List data) {
        dbHelper = new mySQLite(this, "image.db", null, 2);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("image", null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                String imagepath = cursor.getString(cursor.getColumnIndex("photo"));
                String filename=cursor.getString(cursor.getColumnIndex("filename"));
                Bitmap photo = BitmapFactory.decodeFile(imagepath, getBitmapOption(2));
                Picture i = new Picture(filename,photo);
                data.add(i);
            } while (cursor.moveToNext());
    }
    public void imgMax(Bitmap bitmap) {

        LayoutInflater inflater = LayoutInflater.from(PhotoActivity.this);
        View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null);
        // 加载自定义的布局文件
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(PhotoActivity.this,R.style.edit_AlertDialog_style).create();
        ImageView img = (ImageView) imgEntryView.findViewById(R.id.large_image);
        img.setImageBitmap(bitmap);
        // 这个是加载网络图片的，可以是自己的图片设置方法
        // imageDownloader.download(imageBmList.get(0),img);
        dialog.setView(imgEntryView); // 自定义dialog
        dialog.show();
        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        imgEntryView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                dialog.cancel();
            }
        });
    }
    public boolean deleteSDFile(String fileName) {
        File file = new File(fileName);
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }
        return file.delete();
    }
}
