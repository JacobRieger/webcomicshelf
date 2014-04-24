package activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.R;

import domain.Bookmark;
import domain.Comic;
import domain.HtmlImage;
import services.database.ComicDataService;
import services.network.async.ImageDownloadAsyncTask;
import services.utilities.AddComicOnTouchListener;
import services.utilities.BookmarkList;
import services.utilities.BookmarkLoader;
import services.utilities.ComicBuilder;

public class AddComicActivity extends ActionBarActivity implements View.OnClickListener {

    private BookmarkList Bookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comic);

        if (Bookmarks == null) {

            Bookmarks = new BookmarkLoader(this).getBookmarks();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddComicFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_comic, menu);

        //This generates our bookmark submenu items
        MenuItem BookMark = (MenuItem) menu.findItem(R.id.addComicBookmarks);

        SubMenu BookMarkSub = null;
        if (BookMark != null) {
            BookMarkSub = BookMark.getSubMenu();
        }
        else
        {
            Log.d("AddComicActivity", "Bookmark submenu null");
            return false;
        }

        for(int x = 0; x < Bookmarks.size(); x++)
        {
            BookMarkSub.add(32, 0, 0, Bookmarks.get(x).getName());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //If our item is a bookmark, we load up it's data into the entry fields
        if(item.getGroupId() == 32)
        {
            Bookmark current = Bookmarks.find(item.getTitle().toString());

            AddComicFragment addComicFragment = (AddComicFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            addComicFragment.AddBookmarkInfo(current);

            return true;
        }

        return false;

    }
    @Override
    public void onClick(View v) {

        AddComicFragment addComicFragment = (AddComicFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        AddComicOnTouchListener  addComicOnTouchListener = addComicFragment.getAddComicOnTouchListener();

        switch(v.getId())
        {
            case R.id.AddComicWebButton:
                if(addComicOnTouchListener.canAddComic())
                {
                    ComicBuilder comicBuilder = new ComicBuilder();
                    //Setting comic fields

                    String comicname = addComicFragment.getComicName().getText().toString();
                    String website = addComicFragment.getComicEditText().getText().toString();

                    if(comicname.equals("")) comicname = "Unknown";

                    Time time = new Time();
                    time.setToNow();

                    String imageUrl = addComicOnTouchListener.getImageUrl();
                    String altText = "Null";

                    comicBuilder.Name(comicname)
                            .SeenByUser(false)
                            .LastUpdatedAt(time.toString())
                            .Url(website);

                    HtmlImage htmlImage = new HtmlImage(imageUrl, altText, Bitmap.createBitmap(1,1, Bitmap.Config.ALPHA_8));

                    ImageDownloadAsyncTask downloader = new ImageDownloadAsyncTask(this, comicBuilder, htmlImage);
                    downloader.execute(imageUrl);
                }
                else
                {
                    Toast toast = Toast.makeText(this, "Incorrect Selection", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AddComicFragment extends Fragment {

        private WebView webview;
        private TextView name;
        private EditText edittext;
        private Button addComicButton;
        AddComicOnTouchListener addComicOnTouchListener;

        public AddComicFragment() {

        }

        public void AddBookmarkInfo(Bookmark bookmark)
        {
            webview.loadUrl(bookmark.getUrl());
            edittext.setText(bookmark.getUrl());
            name.setText(bookmark.getName());
        }

        public TextView getComicName()
        {
            return name;
        }

        public EditText getComicEditText()
        {
            return edittext;
        }

        public AddComicOnTouchListener getAddComicOnTouchListener()
        {
            return addComicOnTouchListener;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_comic, container, false);

            //Wire up the views to our code here
            webview        = (WebView)  rootView.findViewById(R.id.AddComicWebView);
            edittext       = (EditText) rootView.findViewById(R.id.AddComicWebEdit);
            addComicButton = (Button) rootView.findViewById(R.id.AddComicWebButton);
            name           = (EditText) rootView.findViewById(R.id.AddComicWebEditName);

            webview.setWebViewClient(new VideoWebViewClient());
            webview.getSettings().setUseWideViewPort(true);

            addComicOnTouchListener = new AddComicOnTouchListener("Not Set");
            webview.setOnTouchListener(addComicOnTouchListener);

            addComicButton.setOnClickListener((AddComicActivity) getActivity());

            return rootView;
        }

        private class VideoWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //view.loadUrl(url);
                edittext.setText(url);

                return false;
            }

        }
    }



}
