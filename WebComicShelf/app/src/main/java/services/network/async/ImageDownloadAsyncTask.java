package services.network.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jacob on 3/16/14.
 */
public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... strings) {
        boolean DEBUG = true;

        try {
            // Opens a URL connection
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.connect();
            // Get the inputstream
            InputStream input = connection.getInputStream();
            // We know it's an image url so we decode it into a bitmap
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            //This is for when the bitmap is too big (aka SMBC)
            //TODO Change 4096
            //Tailored only for GSIII, 4096 is equal to GL max size, should find way to
            //get this
            if (myBitmap.getHeight() > 4096 || myBitmap.getWidth() > 4096) {
                System.out.println("Scaling image");

                if(DEBUG) Log.d("retrieveImageBitmap", "Scaling image");

                int origWidth = myBitmap.getWidth();
                int origHeight = myBitmap.getHeight();
                int newHeight = 4096;
                int newWidth = 4096;
                float scaleWidth;
                float scaleHeight;
                if (origWidth >= origHeight) {
                    scaleWidth = (float) newWidth / origWidth;
                    scaleHeight = scaleWidth;
                } else {
                    scaleHeight = (float) newHeight / origHeight;
                    scaleWidth = scaleHeight;
                }
                myBitmap = Bitmap.createScaledBitmap(myBitmap,
                        (int) (origWidth * scaleWidth), (int) (origHeight * scaleHeight), false);
            }

            if(DEBUG)Log.e("Comic", "Returned in retrieveImageBitmap");
            return myBitmap;
        } catch (IOException e) {

            if(e.getMessage() != null)
            {
                Log.e("Exception", e.getMessage());
            }
            Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
            return bitmap;
        }
    }
}
