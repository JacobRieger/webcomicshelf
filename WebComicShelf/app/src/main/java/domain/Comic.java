package domain;

/**
 * Created by Jacob on 3/8/14.
 */
public class Comic {

    private long _id;
    private String _name;
    private String _url;
    private HtmlImage _htmlImage;
    private Boolean _seenByUser;
    private String _lastUpdatedAt;

    public Comic(long id, String name, String url, HtmlImage htmlImage, Boolean seenByUser, String lastUpdatedAt)
    {
        _id = id;
        _name = name;
        _url = url;
        _htmlImage = htmlImage;
        _seenByUser = seenByUser;
        _lastUpdatedAt = lastUpdatedAt;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_url() { return _url;}

    public void set_url(String _url){this._url = _url;}

    public HtmlImage get_htmlImage() {
        return _htmlImage;
    }

    public void set_htmlImage(HtmlImage _htmlImage) {
        this._htmlImage = _htmlImage;
    }

    public Boolean get_seenByUser() {
        return _seenByUser;
    }

    public void set_seenByUser(Boolean _seenByUser) {
        this._seenByUser = _seenByUser;
    }

    public String get_lastUpdatedAt() {
        return _lastUpdatedAt;
    }

    public void set_lastUpdatedAt(String _lastUpdatedAt) {
        this._lastUpdatedAt = _lastUpdatedAt;
    }

}
