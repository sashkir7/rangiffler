package auth.model;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseModel {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    public abstract String toJson();

}
