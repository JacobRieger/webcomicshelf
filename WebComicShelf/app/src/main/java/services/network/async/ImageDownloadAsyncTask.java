package services.network.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import activities.ComicListActivity;
import domain.Comic;
import domain.HtmlImage;
import services.database.ComicService;
import services.network.JsoupComicScraper;
import services.utilities.ComicBuilder;
import services.utilities.StringComparer;

/**
 * Created by Jacob on 3/16/14.
 */
public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ProgressDialog pdialog;
    private Context context;
    private ComicBuilder comicBuilder;
    private HtmlImage htmlImage;
    private String altText;

    public ImageDownloadAsyncTask(Context _context, ComicBuilder _comicBuilder, HtmlImage _htmlImage)
    {
        context = _context;
        comicBuilder = _comicBuilder;
        htmlImage = _htmlImage;
    }

    @Override
    protected void onPreExecute()
    {
        pdialog = new ProgressDialog(context);
        pdialog.setCancelable(false);
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Adding Comic...");
        pdialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        boolean DEBUG = true;

        String website = comicBuilder.BuildComic().get_url();

        altText = getImageAltText(strings[0], website);

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

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        htmlImage.setBitmap(bitmap);
        htmlImage.setAltText(altText);
        comicBuilder.HtmlImage(htmlImage);
        Comic comic = comicBuilder.BuildComic();

        ComicService dataService = new ComicService(context);
        dataService.createComic(comic);

        pdialog.dismiss();

        Intent intent = new Intent(context, ComicListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected String getImageAltText(String imageUrl, String website)
    {
        JsoupComicScraper scraper = new JsoupComicScraper();

        List<HtmlImage> htmlImageList = scraper.GetAllImageUrlsFrom(website);

        int minScore = 9999;
        HtmlImage minScoredHtmlImage = htmlImageList.get(0);
        for (HtmlImage listItem : htmlImageList)
        {
            int result = StringComparer.computeLevenshteinDistance(listItem.getSource(), imageUrl);
            if(result < minScore)
            {
                minScoredHtmlImage = listItem;
                minScore = result;
            }
        }

        return minScoredHtmlImage.getAltText();
    }

}
