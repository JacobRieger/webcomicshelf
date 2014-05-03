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
    private boolean scaled = false;
    private Context context;

    public ComicImageViewLoader(ImageView imageView, Context _context) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        context = _context;
    }

    public ComicImageViewLoader(ImageView imageView, Context _context, boolean scale) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        scaled = scale;
        context = _context;
    }


    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {

        ComicService dataService = new ComicService(context);
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

        dataService.close();

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