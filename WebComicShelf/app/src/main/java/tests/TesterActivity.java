package tests;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.R;

import domain.HtmlImage;
import services.database.ComicDataService;
import services.network.async.ScraperAsyncTask;
import services.utilities.ComicBuilder;

public class TesterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);

        ScraperAsyncTask scraperAsyncTask = new ScraperAsyncTask();
        scraperAsyncTask.execute();

        ComicDatabaseSpecs specs = new ComicDatabaseSpecs();

        ComicBuilder comicBuilder = new ComicBuilder();


        HtmlImage htmlImage = new HtmlImage("http://drmcninja.com/comics/2014-03-14-27p25.jpg",
                "Who cares", Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));

        comicBuilder.Name("Test Comic")
                .Url("http://drmcninja.com/")
                .HtmlImage(htmlImage)
                .SeenByUser(false)
                .LastUpdatedAt("Whenever");


        ComicDataService service = new ComicDataService(this);
        service.createComic(comicBuilder.BuildComic());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tester, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tester, container, false);
            return rootView;
        }
    }

}
