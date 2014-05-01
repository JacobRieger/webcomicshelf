package activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.app.R;

import java.util.ArrayList;
import java.util.List;

import domain.Comic;
import services.database.ComicDataService;
import services.database.ComicImageViewLoader;
import services.utilities.BookmarkList;

public class ComicGalleryActivity extends FragmentActivity implements View.OnLongClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    static SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    public static List<Comic> Comics;
    //public static ArrayList<String> ComicNames;
    public BookmarkList Bookmarks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_gallery);
        //Our database on the  phone
        ComicDataService database = new ComicDataService(this, true);
        Comics = database.getAllComics();

        // Create the adapter that will return a fragment
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //Here we set some of the Title colors / sizes
        PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        pagerTitleStrip.setTextSize(1, 25);
        pagerTitleStrip.setTextColor(Color.WHITE);
        pagerTitleStrip.setBackgroundColor(Color.BLACK);


        //This is for when the app is destroyed
        //Loads all comics from the database to the current activity
        //Comics = db.getAllComics();

        //Log.d("OnCreateFinished", Integer.toString(Comics.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comic_gallery, menu);

        // SubMenu GoTo = menu.addSubMenu("Select Comic");
        // MenuItem select = menu.findItem(R.id.selection);
        SubMenu GoTo = menu.findItem(R.id.selection).getSubMenu();
        for(int i = 0; i < Comics.size(); i++)
        {
            //Set the button names in the submenus
            //Can't have them both be the same, still need to investigate
            GoTo.add((Comics.get(i).get_name()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String Title = item.getTitle().toString();

        for(int i = 0; i < Comics.size(); i++)
        {
            if(Title.equals((Comics.get(i).get_name())))
            {
                //Sets our view to the selected comic
                mViewPager.setCurrentItem(i);
            }
        }

        return super.onOptionsItemSelected(item);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            //Create our new fragment
            Fragment fragment = new ComicFragment();
            Bundle args = new Bundle();
            //We put into the bundle the section it's placed in
            args.putInt(ComicFragment.ARG_SECTION_NUMBER, i);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return Comics.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //This sets the top of the page to the comics name
            return Comics.get(position).get_name();
        }
    }

    public static class ComicFragment extends Fragment{
        public ComicFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //We create our new imageview to be displayed
            ImageView imageView = new ImageView(getActivity());

            Bundle args = getArguments();

            Bitmap comicBitmap = Comics.get(args.getInt(ARG_SECTION_NUMBER)).get_htmlImage().getBitmap();
            imageView.setImageBitmap(comicBitmap);

            ComicImageViewLoader loader = new ComicImageViewLoader(imageView, getActivity());
            loader.execute(Comics.get(args.getInt(ARG_SECTION_NUMBER)).get_name());

            //OnLong click for enabling zoom and pan
            imageView.setOnLongClickListener((ComicGalleryActivity) getActivity());
            return imageView;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        ComicDataService database = new ComicDataService(this, true);

        if (view.getClass() == ImageView.class) {
            //Our current comic
            Comic Selected = database.getComic(mSectionsPagerAdapter.getPageTitle(mViewPager.getCurrentItem()).toString());

            Intent detailIntent = new Intent(this, ComicDetailActivity.class);
            detailIntent.putExtra(ComicDetailFragment.COMIC_ID, Long.toString(Selected.get_id() - 1));
            startActivity(detailIntent);
        }
        return false;
    }
}
