package data;

public enum DataBase {
    USERDATA("jdbc:postgresql://127.0.0.1:5432/userdata"),
    GEO("jdbc:postgresql://127.0.0.1:5432/geo"),
    PHOTO("jdbc:postgresql://127.0.0.1:5432/photo");

    private final String url;

    DataBase(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
