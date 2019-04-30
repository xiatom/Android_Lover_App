package com.ace.xiatom.ace_project;

import android.graphics.Bitmap;

public class Picture {
    private Bitmap img;
    private String filename;
    public Picture(String filename, Bitmap img){
        this.img=img;
        this.filename=filename;
    }
    public Bitmap getImg() {
        return img;
    }
    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
