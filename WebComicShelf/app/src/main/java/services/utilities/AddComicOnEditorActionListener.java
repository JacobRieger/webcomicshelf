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

        if(event != null)
        {
            Log.d("CustomOnEditorActionListener", "Event was not Null");
            webview.loadUrl((edittext.getText().toString()));
        }
        Log.d("CustomOnEditorActionListener", "Event was Null");
        webview.loadUrl((edittext.getText().toString()));
        return false;
    }

}

