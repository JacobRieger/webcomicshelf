package services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import domain.Comic;
import domain.HtmlImage;
import services.utilities.ComicBuilder;

/**
 * Created by Jacob on 3/8/14.
 */
public class ComicDataService extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "webcomicsManager";

    // Comics table name
    private static final String TABLE_WEBCOMICS = "Webcomics";

    // Comics Table Columns names
    private static final String KEY_ID              = "id";
    private static final String KEY_NAME            = "name";
    private static final String KEY_SITE_URL        = "url";
    private static final String KEY_SOURCE          = "source";
    private static final String KEY_ALT_TEXT        = "altText";
    private static final String KEY_BITMAP          = "bitmap";
    private static final String KEY_SEEN_BY_USER    = "seenByUser";
    private static final String KEY_LAST_UPDATED_AT = "lastUpdatedAt";

    private SQLiteDatabase database;

    public ComicDataService(Context context, boolean readOnly)
    {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        database = readOnly ? this.getReadableDatabase() : this.getWritableDatabase();
    }

    public void close()
    {
        database.close();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_WEBCOMICS_TABLE =
                "CREATE TABLE " + TABLE_WEBCOMICS +
                        "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_SITE_URL + " TEXT,"
                + KEY_SOURCE + " TEXT, "
                + KEY_ALT_TEXT + " TEXT,"
                + KEY_BITMAP + " BLOB,"
                + KEY_SEEN_BY_USER + " INTEGER,"
                + KEY_LAST_UPDATED_AT + " TEXT"
                        + ")";

        Log.d("Database Creation Script", CREATE_WEBCOMICS_TABLE);

        sqLiteDatabase.execSQL(CREATE_WEBCOMICS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WEBCOMICS);
        onCreate(sqLiteDatabase);
    }

    public Comic createComic(Comic comic)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues comicValues = getContentValuesFor(comic);

        long id;

        if (database != null)
        {
            id = database.insert(TABLE_WEBCOMICS, null, comicValues);
        }
        else
        {
            Log.d("Database", "Database came back null");
            return null;
        }

        Comic databaseComic =  getComic(id);

        List<Comic> comics = getAllComics();

        database.close();

        return databaseComic;
    }

    public List<Comic> getAllComics()
    {
        List<Comic> comics = new ArrayList<Comic>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = queryFor(KEY_ID, "*");

        while(cursor.moveToNext())
        {
            Comic comic = cursorToComic(cursor);
            comics.add(comic);
        }

        return comics;
    }

    public List<String> getAllComicNames()
    {
        SQLiteDatabase database = this.getReadableDatabase();

        if(database == null)
        {
            return new ArrayList<String>();
        }

        Cursor cursor = database.query(TABLE_WEBCOMICS, new String[]{KEY_NAME}, "",
                null, null, null, null);

        List<String> comicNames = new ArrayList<String>();

        while(cursor.moveToNext())
        {
            String name = cursor.getString(0);
            comicNames.add(name);
        }

        cursor.close();

        return comicNames;
    }


    public int count()
    {
        SQLiteDatabase database = this.getReadableDatabase();

        if(database == null)
        {
            return 0;
        }

        Cursor cursor = database.query(TABLE_WEBCOMICS, new String[]
                {KEY_ID}, "", null, null, null, null
                );

        database.close();
        return cursor.getCount();
    }

    public Comic getComic(long id)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        if(database == null)
        {
            return null;
        }

        Cursor cursor = queryFor(KEY_ID, Long.toString(id));

        if(cursor.getCount() == 0 || cursor == null)
        {
            Log.d("Database", "Comic does not exist");
            return null;
        }

        cursor.moveToFirst();

        ComicBuilder builder = new ComicBuilder();

        HtmlImage htmlImage = new HtmlImage(cursor.getString(3), cursor.getString(4), cursor.getBlob(5));

        builder.Id(cursor.getInt(0))
                .Name(cursor.getString(1))
                .Url(cursor.getString(2))
                .HtmlImage(htmlImage)
                .SeenByUser(cursor.getString(6).equals("1"))
                .LastUpdatedAt(cursor.getString(7));

        Comic comic = builder.BuildComic();

        return comic;
    }

    public Comic getComic(String name)
    {
        return null;
    }

    private ContentValues getContentValuesFor(Comic comic)
    {
        ContentValues comicValues = new ContentValues();

        comicValues.put(KEY_NAME, comic.get_name());
        comicValues.put(KEY_SITE_URL, comic.get_url());
        comicValues.put(KEY_SOURCE, comic.get_htmlImage().getSource());
        comicValues.put(KEY_ALT_TEXT, comic.get_htmlImage().getAltText());
        comicValues.put(KEY_BITMAP, comic.get_htmlImage().getBitmapBytes());
        comicValues.put(KEY_SEEN_BY_USER, comic.get_seenByUser());
        comicValues.put(KEY_LAST_UPDATED_AT, comic.get_lastUpdatedAt());

        return comicValues;
    }

    private Cursor queryFor(String columnName, String value)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        if(database == null)
        {
            return null;
        }

        Cursor cursor = database.query(TABLE_WEBCOMICS,
                null, columnName + "=?",
                new String[] { value }, null, null, null, null);

        return cursor;
    }

    private Comic cursorToComic(Cursor cursor)
    {
        ComicBuilder builder = new ComicBuilder();

        HtmlImage htmlImage = new HtmlImage(cursor.getString(3), cursor.getString(4), cursor.getBlob(5));

        builder.Id(cursor.getInt(0))
                .Name(cursor.getString(1))
                .Url(cursor.getString(2))
                .HtmlImage(htmlImage)
                .SeenByUser(cursor.getString(6).equals("1"))
                .LastUpdatedAt(cursor.getString(7));

        Comic comic = builder.BuildComic();
        return comic;
    }
}
