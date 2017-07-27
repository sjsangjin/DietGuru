package com.example.pc.dietapp.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by pc on 2017-07-25.
 */

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {


    private ImageView dispImageView;

    public ImageLoaderTask(ImageView dispImgView) {
        this.dispImageView =dispImgView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        String imgUrl = params[0];

        Bitmap bmp = null;

        try {
            bmp = BitmapFactory.decodeStream(  (InputStream)new URL(imgUrl).getContent()  );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bmp;
    }//end doInBackground()

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null){
            //표시
            dispImageView.setImageBitmap(bitmap);
        }
    }

};

