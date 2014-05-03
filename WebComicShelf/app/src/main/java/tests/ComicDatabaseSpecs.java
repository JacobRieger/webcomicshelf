package tests;

import android.content.Context;

import services.database.ComicService;

/**
 * Created by Jacob on 3/15/14.
 */
public class ComicDatabaseSpecs {

    public void crudTests(Context context)
    {
        ComicService service = new ComicService(context, true);
        service.close();
    }
}
