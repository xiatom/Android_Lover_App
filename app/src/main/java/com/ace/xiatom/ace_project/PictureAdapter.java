package com.ace.xiatom.ace_project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PictureAdapter extends ArrayAdapter<Picture> {
    private  int resourceId;
    public PictureAdapter(Context context, int textViewResourceId, List<Picture> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Picture picture = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView Img = view.findViewById(R.id.image1);
        TextView tv1=view.findViewById(R.id.tv1);
        Img.setImageBitmap(picture.getImg());
        tv1.setText(picture.getFilename());
        return  view;
    }
}
