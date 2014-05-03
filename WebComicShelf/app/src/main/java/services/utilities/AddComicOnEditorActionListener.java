package services.utilities;

import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jacob on 4/27/14.
 */

public class AddComicOnEditorActionListener implements TextView.OnEditorActionListener {

    private WebView webview;
    private EditText edittext;

    public AddComicOnEditorActionListener(WebView Webview, EditText Edittext)
    {
        webview  = Webview;
        edittext = Edittext;
    }

    @Override
    public boolean onEditorAction(TextView textview, int actionId, KeyEvent event) {

        if(edittext == null || edittext.getText() == null || edittext.getText().toString() == null)
        {
            if(textview != null && textview.getText() != null)
            {
                String url = textview.getText().toString();

                webview.loadUrl(url);
            }
        }
        else
        {
            webview.loadUrl((edittext.getText().toString()));
        }
        
        return false;
    }

}

