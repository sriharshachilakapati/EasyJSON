package com.shc.easyjson;

/**
 * @author Sri Harsha Chilakapati
 */
public class JSONValue
{
    private Object value;
    private Type   type;

    public JSONValue(double value)
    {
        this.value = value;
        type = Type.NUMBER;
    }

    public JSONValue(String value)
    {
        this.value = value;
        type = Type.STRING;
    }

    public JSONValue(boolean value)
    {
        this.value = value;
        type = Type.BOOLEAN;
    }

    public JSONValue(JSONObject value)
    {
        this.value = value;
        type = Type.OBJECT;
    }

    public JSONValue(JSONArray value)
    {
        this.value = value;
        type = Type.ARRAY;
    }

    public JSONValue(Object value)
    {
        this.value = value;
        type = Type.UNKNOWN;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue()
    {
        return (T) value;
    }

    public Type getType()
    {
        return type;
    }

    public enum Type
    {
        NUMBER,
        STRING,
        BOOLEAN,
        ARRAY,
        OBJECT,
        UNKNOWN
    }
}
