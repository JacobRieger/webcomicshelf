package services.utilities;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;

/**
 * Created by Jacob on 4/23/14.
 */
public class AddComicOnTouchListener implements OnTouchListener {

    private String Imageurl;

    public AddComicOnTouchListener(String imageurl)
    {
        Imageurl = imageurl;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        WebView.HitTestResult hitTestResult = ((WebView)v).getHitTestResult();
        try
        {
            if(hitTestResult != null)
            {
                if(hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE)
                {
                    Imageurl = hitTestResult.getExtra();
                }
                else
                {
                    Imageurl = hitTestResult.getExtra();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }



    public String getImageUrl()
    {
        return Imageurl;
    }
    public boolean canAddComic()
    {
        if(Imageurl != "Notset") return true;

        return false;
    }



}
