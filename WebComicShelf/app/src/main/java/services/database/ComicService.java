package services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import domain.Comic;
import domain.HtmlImage;
import services.utilities.ComicBuilder;

/**
 * Created by Jacob on 3/8/14.
 */
public class ComicService extends SQLiteOpenHelper {


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

    public ComicService(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

        database.close();

        return databaseComic;
    }

    public long updateComic(Comic comic)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues comicValues = new ContentValues();
        HtmlImage htmlImage = comic.get_htmlImage();
        byte[] bitmapBytes = getBytesFromBitmap(htmlImage.getBitmap());

        comicValues.put(KEY_ID, comic.get_id());
        comicValues.put(KEY_NAME, comic.get_name());
        comicValues.put(KEY_SITE_URL, comic.get_url());
        comicValues.put(KEY_SOURCE, htmlImage.getSource());
        comicValues.put(KEY_ALT_TEXT, htmlImage.getAltText());
        comicValues.put(KEY_BITMAP, bitmapBytes);
        comicValues.put(KEY_LAST_UPDATED_AT, comic.get_lastUpdatedAt());
        comicValues.put(KEY_SEEN_BY_USER, comic.get_seenByUser());

        int value = database.update(TABLE_WEBCOMICS, comicValues, KEY_ID + " = ?",
                new String[] { String.valueOf(comic.get_id()) });

        database.close();

        return value;
    }

    public List<Comic> getAllComics()
    {
        List<Comic> comics = new ArrayList<Comic>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("select * from " + TABLE_WEBCOMICS, null);

        while(cursor.moveToNext())
        {
            Comic comic = cursorToComic(cursor);
            comics.add(comic);
        }

        return comics;
    }

    public int getCount()
    {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("select count(*) " + TABLE_WEBCOMICS, null);
        cursor.moveToFirst();

        int result = cursor.getInt(0);
        database.close();

        return result;
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

        database.close();

        return comicNames;
    }

    public Comic getComic(long id)
    {
        SQLiteDatabase database = this.getReadableDatabase();

        if(database == null)
        {
            return null;
        }

        Cursor cursor = database.query(TABLE_WEBCOMICS,
                null, KEY_ID + "=?",
                new String[] { Long.toString(id) }, null, null, null, null);

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

        database.close();

        return comic;
    }

    public Comic getComic(String name)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        if(database == null)
        {
            return null;
        }

        Cursor cursor = database.query(TABLE_WEBCOMICS,
                null, KEY_NAME + "=?",
                new String[] { name }, null, null, null, null);

        if(cursor.getCount() == 0 || cursor == null)
        {
            Log.d("Database", "Comic does not exist : " + name);
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

        database.close();

        return comic;
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

    public static byte[] getBytesFromBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}
