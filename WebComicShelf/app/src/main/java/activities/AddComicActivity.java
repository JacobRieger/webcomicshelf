package activities;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app.R;

import services.utilities.BookmarkList;
import services.utilities.BookmarkLoader;

public class AddComicActivity extends ActionBarActivity {


    private WebView webview;
    private TextView name;
    private EditText edittext;
    private BookmarkList Bookmarks;
    private Button addComicButton;
    private String         comicName;
    private String         comicImageUrl = "Notset";
    private String         comicWebsite;

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AddComicFragment extends Fragment {

        public AddComicFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_comic, container, false);
            return rootView;
        }
    }

}
