package domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Jacob on 3/12/14.
 */
public class HtmlImage {

    private String _source;
    private String _altText;

    private Bitmap _bitmap;

    public HtmlImage(String source, String altText, Bitmap bitmap)
    {
        this._source = source;
        this._altText = altText;
        this._bitmap = bitmap;
    }

    public HtmlImage(String source, String altText, byte[] bitmapBytes)
    {
        this._source = source;
        this._altText = altText;
        this._bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }

    public String getSource() {
        return _source;
    }

    public void setSource(String source) {
        this._source = source;
    }

    public String getAltText() {
        return _altText;
    }

    public void setAltText(String altText) {
        this._altText = altText;
    }

    public Bitmap getBitmap() {
        return _bitmap;
    }

    public void setBitmap(Bitmap _bitmap) {
        this._bitmap = _bitmap;
    }

    public byte[] getBitmapBytes() {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, blob);
        byte[] bitmapdata = blob.toByteArray();

        if(bitmapdata == null)
        {
            Bitmap Default = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
            Default.compress(Bitmap.CompressFormat.PNG,  0, blob);
        }
        return bitmapdata;
    }
}
