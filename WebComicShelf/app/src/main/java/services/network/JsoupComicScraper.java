package services.network;

import android.graphics.Bitmap;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.HtmlImage;

/**
 * Created by Jacob on 3/8/14.
 * Scraper that returns all htmlImages on a website
 *
 *
 */
public class JsoupComicScraper {

    private HttpConnection _connection;

    public List<HtmlImage> GetAllImageUrlsFrom(String siteUrl){
        List<HtmlImage> htmlImages = new ArrayList<HtmlImage>();

        try
        {
            Connect(siteUrl);

            Elements imageElements = GetImageElements();

            htmlImages = TranslateImageElements(imageElements);

        }
        catch (MalformedURLException e)
        {

        }
        catch (ConnectException e)
        {

        }
        catch (IOException e)
        {

        }

        return htmlImages;
    }

    private void Connect(String siteUrl){
        _connection = (HttpConnection) Jsoup.connect(siteUrl).timeout(1000000);
        _connection.followRedirects(true);
    }

    private Elements GetImageElements() throws IOException {

        Document pageDocument = _connection.get();
        pageDocument = HandleMetaRefreshRedirect(pageDocument);

        Elements imageElements;
        imageElements = pageDocument.select("img");

        return imageElements;
    }

    private Document HandleMetaRefreshRedirect(Document pageDocument) throws IOException {
        Elements metaElements = pageDocument.select("html head meta");
        if(metaElements.attr("http-equiv").contains("REFRESH"))
        {
            _connection = (HttpConnection) Jsoup.connect(metaElements.attr("content").split("=")[1]);
            return _connection.get();
        }
        return pageDocument;
    }

    private List<HtmlImage> TranslateImageElements(Elements imageElements){
        ArrayList<HtmlImage> htmlImages = new ArrayList<HtmlImage>();

        for(Element imageElement : imageElements)
        {
            HtmlImage htmlImage = TranslateImageElement(imageElement);
            htmlImages.add(htmlImage);
        }

        return htmlImages;
    }

    private HtmlImage TranslateImageElement(Element imageElement){
        String connectionUrl = _connection.request().url().toString();

        String attr = imageElement.attr("style");
        if(attr.indexOf("http://") > 0)
        {
            String img = attr.substring( attr.indexOf("http://"), attr.indexOf(")"));
            String alt = imageElement.attr("title");
            System.out.println(alt);

            return new HtmlImage(img, alt, Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
        }

        String imageSource = imageElement.attr("src");
        String altText = imageElement.attr("title");

        if (imageSource.startsWith("/") || !imageSource.startsWith("http")) {
            // This was necessary for DrMcNinja, as the full url
            // was not posted, I suspect it's true for other sites
            // as well
            if(!imageSource.startsWith("/") && !connectionUrl.endsWith("/"))
            {
                imageSource = "/".concat(imageSource);
            }
            imageSource = connectionUrl.concat(imageSource);
        }
        if (!imageSource.startsWith("http")) {
            // Saves us from the malformed URL exception
            imageSource = "http://".concat(imageSource);
        }
        imageSource = imageSource.replaceAll(" " , "%20");

        return new HtmlImage(imageSource, altText, Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
    }
}
