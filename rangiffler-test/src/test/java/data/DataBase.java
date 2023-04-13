package data;

public enum DataBase {
    USERDATA("jdbc:postgresql://127.0.0.1:5432/userdata"),
    AUTH("jdbc:postgresql://%s/niffler-auth"),
    SPEND("jdbc:postgresql://%s/niffler-spend"),
    CURRENCY("jdbc:postgresql://%s/niffler-currency");
    private final String url;

    DataBase(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
