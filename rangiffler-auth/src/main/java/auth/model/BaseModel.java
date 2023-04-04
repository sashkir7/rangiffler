package auth.model;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseModel {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public abstract String toJson();

}
