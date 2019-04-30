package com.ace.xiatom.ace_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class mySQLite extends SQLiteOpenHelper {
    private final String CREATE_Image = "create table image(id integer primary key autoincrement,photo text,filename text)";
    private Context myContext;

    public mySQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        myContext = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists image");
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_Image);
        Toast.makeText(myContext,"create SUCCESSFULLY",Toast.LENGTH_SHORT).show();
    }
}
