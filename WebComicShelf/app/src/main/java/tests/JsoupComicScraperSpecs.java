package tests;

import android.util.Log;

import java.util.List;

import domain.HtmlImage;
import services.network.JsoupComicScraper;

/**
 * Created by Jacob on 3/12/14.
 */
public class JsoupComicScraperSpecs {

    public void TestScrapeOfDrMcNinja()
    {
        JsoupComicScraper scraper = new JsoupComicScraper();

        String drMcNinjaUrl = "http://www.drmcninja.com";

        List<HtmlImage> images = scraper.GetAllImageUrlsFrom (drMcNinjaUrl);

        for(HtmlImage image : images)
        {
            Log.d("Images", image.getSource());
        }
    }
}
