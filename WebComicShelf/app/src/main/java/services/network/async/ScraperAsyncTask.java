package services.network.async;

import android.os.AsyncTask;

import java.util.List;

import domain.HtmlImage;
import services.network.JsoupComicScraper;
import tests.JsoupComicScraperSpecs;

/**
 * Created by Jacob on 3/15/14.
 */
public class ScraperAsyncTask extends AsyncTask<String, Void, List<HtmlImage>> {

    @Override
    protected List<HtmlImage> doInBackground(String... strings) {

        JsoupComicScraper scraper = new JsoupComicScraper();

        return scraper.GetAllImageUrlsFrom(strings[0]);
    }
}
