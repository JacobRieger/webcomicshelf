package services.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import domain.Comic;

/**
 * Created by Jacob on 4/26/14.
 */
public class ComicImageViewLoader extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private ComicService dataService;
    private boolean scaled = false;

    public ComicImageViewLoader(ImageView imageView, Context context) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);


        dataService = new ComicService(context, true);
    }

    public ComicImageViewLoader(ImageView imageView, Context context, boolean scale) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);

        scaled = scale;

        dataService = new ComicService(context, true);
    }


    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {

        if(scaled)
        {
            Comic current = dataService.getComic(params[0]);
            Bitmap image = current.get_htmlImage().getBitmap();
            byte[] bytes = ComicService.getBytesFromBitmap(image);

            return Bitmap.createBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length),
                    0, 0, 75, 75);
        }

        Comic comic = dataService.getComic(params[0]);
        Bitmap bitmap = comic.get_htmlImage().getBitmap();

        return bitmap;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}