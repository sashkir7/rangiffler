package data;

import config.DatabaseProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DataBase {
    AUTH(DatabaseProperties.AUTH_DATABASE_URL),
    USERDATA(DatabaseProperties.USERDATA_DATABASE_URL),
    GEO(DatabaseProperties.GEO_DATABASE_URL),
    PHOTO(DatabaseProperties.PHOTO_DATABASE_URL);

    private final String url;

    @Override
    public String toString() {
        return url;
    }

}
