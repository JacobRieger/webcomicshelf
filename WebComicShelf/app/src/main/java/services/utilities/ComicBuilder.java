package services.utilities;

import domain.Comic;
import domain.HtmlImage;

/**
 * Created by Jacob on 3/15/14.
 */
public class ComicBuilder {

    private long _id;
    private String _name = "no name";
    private String _url;
    private HtmlImage _htmlImage;
    private Boolean _seenByUser = false;
    private String _lastUpdatedAt = "Unknown";

    public ComicBuilder()
    {

    }

    public Comic BuildComic()
    {
        return new Comic(_id,
                _name,
                _url,
                _htmlImage,
                _seenByUser,
                _lastUpdatedAt);
    }

    public ComicBuilder Id(long id)
    {
        _id = id;
        return this;
    }

    public ComicBuilder Name(String name)
    {
        _name = name;
        return this;
    }

    public ComicBuilder HtmlImage(HtmlImage htmlImage)
    {
        _htmlImage = htmlImage;
        return this;
    }

    public ComicBuilder SeenByUser(Boolean seenByUser)
    {
        _seenByUser = seenByUser;
        return this;
    }

    public ComicBuilder LastUpdatedAt(String lastUpdatedAt)
    {
        _lastUpdatedAt = lastUpdatedAt;
        return this;
    }

    public ComicBuilder Url(String url)
    {
        _url = url;
        return this;
    }

}
