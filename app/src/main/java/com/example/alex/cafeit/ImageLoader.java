package com.example.alex.cafeit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Chris Micek on 4/30/2017.
 */

public class ImageLoader extends AsyncTask<URL, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    public ImageLoader(ImageView image) {
        imageViewReference = new WeakReference<>(image);
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(URL... params) {
        try {
            final URLConnection conn = params[0].openConnection();
            conn.connect();
            final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            final Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            return bm;
        } catch (IOException|OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference.get() != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
