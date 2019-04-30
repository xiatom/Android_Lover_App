package com.ace.xiatom.ace_project;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ChoosephotoActivity extends AppCompatActivity {

    Bitmap bitmap ;
    private ImageView picture;
    public static final int CHOOSE_PHOTO=2;
    SQLiteDatabase db;
    mySQLite dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosephoto);
        dbHelper = new mySQLite(this,"image.db",null,2);
        db = dbHelper.getWritableDatabase();
        picture=(ImageView)findViewById(R.id.picture);
        Button b1=(Button)findViewById(R.id.b1);
        Button b2=(Button)findViewById(R.id.b2);

        if(bitmap == null){
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ChoosephotoActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ChoosephotoActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap == null){
                    bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
                }
                String h="T";
                Date date = new Date();
                date.getTime();
                File fileName = new File(getFilesDir().getAbsolutePath()+"/image//"+date.getTime()+".png");
                if(bitmap == null){
                    Log.i("msg111","null");
                }
                try{
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
                }catch (IOException e){
                    e.printStackTrace();
                }
                ContentValues values;
                values = new ContentValues();
                values.put("photo",fileName.getPath());
                values.put("filename",fileName.toString());
                db.insert("image",null,values);
                finish();
            }
        });
    }
    private  void openAlbum(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_LONG).show();

                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK);{
                //判断手机版本号
                if(Build.VERSION.SDK_INT>=19){
                    Log.i("msg","hhh");
                    handleImageOnKitKat(data);
                }else{
                    Log.i("msg","hh,h");
                    handleImageBeforeKitKat(data);
                }
            }
            break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        Log.i("mm",uri.toString());
        if(DocumentsContract.isDocumentUri(this,uri)){

            Log.i("mm","111");
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection =MediaStore.Images.Media._ID + "=" + id;
                imagePath =getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath =getImagePath(contentUri,null);
            }

        }else if("content".equalsIgnoreCase(uri.getScheme())){
            Log.i("mm","222");
            imagePath =getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            Log.i("mm","333");
            imagePath =uri.getPath();
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor= getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }

        Log.i("mm",path);
        return path;
    }
    private void displayImage(String imagePath){
        if (imagePath!=null){
            Log.i("mm","4");
            bitmap=BitmapFactory.decodeFile(imagePath);
//            bitmap=rotateImage(bitmap,90);
            picture.setImageBitmap(bitmap);
            Log.i("mm","6");
        }else{
            Log.i("mm","5");
            Toast.makeText(this,"failed to get image",Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 对图片进行旋转，拍照后应用老是显示图片横向，而且是逆时针90度，现在给他设置成显示顺时针90度
     *
     * @param bitmap    图片
     * @param degree 顺时针旋转的角度
     * @return 返回旋转后的位图
     */
    public Bitmap rotateImage(Bitmap bitmap, float degree) {
        //create new matrix
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }
}
