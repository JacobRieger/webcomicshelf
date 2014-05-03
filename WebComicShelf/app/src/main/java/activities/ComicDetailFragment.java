package activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.R;

import domain.Comic;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import services.database.ComicService;

/**
 * A fragment representing a single Comic detail screen.
 * This fragment is either contained in a {@link ComicListActivity}
 * in two-pane mode (on tablets) or a {@link ComicDetailActivity}
 * on handsets.
 */
public class ComicDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String COMIC_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Comic selectedComic;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ComicDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(COMIC_ID)) {
            ComicService dataService = new ComicService(getActivity(), true);

            long comicId = Long.parseLong(getArguments().getString(COMIC_ID));

            selectedComic = dataService.getComic(comicId+1);
            dataService.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic_detail, container, false);

        if (selectedComic != null) {
            ImageViewTouch imageView = (ImageViewTouch) rootView.findViewById(R.id.ImageView01);
            imageView.setImageBitmap(selectedComic.get_htmlImage().getBitmap());
            imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        }

        return rootView;
    }

    public Comic getSelectedComic()
    {
        return selectedComic;
    }
}
