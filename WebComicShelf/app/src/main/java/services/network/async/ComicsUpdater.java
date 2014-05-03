package services.network.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import domain.Comic;
import domain.HtmlImage;
import services.database.ComicService;
import services.network.JsoupComicScraper;
import services.network.SerialImageDownloader;
import services.utilities.StringComparer;

/**
 * Created by Jacob on 5/2/14.
 */
public class ComicsUpdater extends AsyncTask<Void, Void, Void> {

    Context context;
    ProgressDialog progressDialog;

    public ComicsUpdater(Context ourcontext)
    {
        context = ourcontext;
    }

    @Override
    protected void onPreExecute()
    {
        ComicService comicService = new ComicService(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(comicService.getCount());
        progressDialog.setMessage("Updating Comics");
        progressDialog.show();
        comicService.close();
    }

    @Override
    protected Void doInBackground(Void... params) {

        ComicService comicService = new ComicService(context);

        List<String> comicNames = comicService.getAllComicNames();
        for(int i = 0; i < comicNames.size(); i++)
        {
            Comic comicToUpdate = comicService.getComic(comicNames.get(i));
            //Todo: Update only comics that need it

            String websiteURL = comicToUpdate.get_url();
            String imageURL = comicToUpdate.get_htmlImage().getSource();

            JsoupComicScraper scraper = new JsoupComicScraper();
            List<HtmlImage> existingHtmlImages = scraper.GetAllImageUrlsFrom(websiteURL);

            if(!doesUrlExistInHtmlList(existingHtmlImages, imageURL))
            {
                SerialImageDownloader imageDownloader = new SerialImageDownloader();

                HtmlImage htmlImage = StringComparer.calculateMostSimilarString(existingHtmlImages, imageURL);
                Bitmap comicBitmap = imageDownloader.DownloadImage(htmlImage.getSource());

                comicToUpdate.set_seenByUser(false);
                //Todo: Settle on a date format and last updated at times

                htmlImage.setBitmap(comicBitmap);
                comicToUpdate.set_htmlImage(htmlImage);

                comicService.updateComic(comicToUpdate);
            }

            progressDialog.incrementProgressBy(1);
        }
        comicService.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        try
        {
            progressDialog.dismiss();
        }
        catch(Exception e)
        {
            Toast alert = Toast.makeText(context, "Comic Updates Complete", Toast.LENGTH_LONG);
            alert.show();
        }
    }

    private boolean doesUrlExistInHtmlList(List<HtmlImage> htmlImages, String url)
    {
        for(HtmlImage htmlImage : htmlImages)
        {
            if(htmlImage.getSource().equals(url))
            {
                return true;
            }
        }
        return false;
    }
}

