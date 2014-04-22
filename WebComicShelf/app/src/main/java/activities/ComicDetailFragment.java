package activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app.R;

import domain.Comic;
import services.database.ComicDataService;

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
            ComicDataService dataService = new ComicDataService(getActivity(), true);

            long comicId = Long.parseLong(getArguments().getString(COMIC_ID));

            selectedComic = dataService.getComic(comicId);
            dataService.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (selectedComic != null) {
            ((TextView) rootView.findViewById(R.id.comic_detail)).setText(selectedComic.get_name());
        }

        return rootView;
    }
}
