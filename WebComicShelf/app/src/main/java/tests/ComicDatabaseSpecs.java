package tests;

import android.content.Context;

import services.database.ComicDataService;

/**
 * Created by Jacob on 3/15/14.
 */
public class ComicDatabaseSpecs {

    public void crudTests(Context context)
    {
        ComicDataService service = new ComicDataService(context);
    }
}
