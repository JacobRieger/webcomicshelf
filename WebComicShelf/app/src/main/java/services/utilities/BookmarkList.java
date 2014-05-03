package services.utilities;

import java.util.ArrayList;

import domain.Bookmark;

/**
 * Created by Jacob on 4/22/14.
 */
public class BookmarkList {

    private ArrayList<Bookmark> bookmarks;

    public BookmarkList()
    {
        bookmarks = new ArrayList<Bookmark>();
    }

    public void add(Bookmark bookmark)
    {
        bookmarks.add(bookmark);
    }
    public int size()
    {
        return bookmarks.size();
    }
    public Bookmark find(String bookmarkName)
    {
        for(int i = 0; i < bookmarks.size(); i++)
        {
            if(bookmarks.get(i).getName().equals(bookmarkName))
            {
                return bookmarks.get(i);
            }
        }
        return null;
    }
    public Bookmark get(int i)
    {
        return bookmarks.get(i);
    }

}