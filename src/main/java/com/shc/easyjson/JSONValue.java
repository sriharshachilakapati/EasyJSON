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
        set(value);
    }

    public JSONValue(String value)
    {
        set(value);
    }

    public JSONValue(boolean value)
    {
        set(value);
    }

    public JSONValue(JSONObject value)
    {
        set(value);
    }

    public JSONValue(JSONArray value)
    {
        set(value);
    }

    public JSONValue()
    {
        set();
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

    public JSONValue set(double value)
    {
        this.value = value;
        type = Type.NUMBER;
        return this;
    }

    public JSONValue set(String value)
    {
        this.value = value;
        type = Type.STRING;
        return this;
    }

    public JSONValue set(boolean value)
    {
        this.value = value;
        type = Type.BOOLEAN;
        return this;
    }

    public JSONValue set(JSONObject value)
    {
        this.value = value;
        type = Type.OBJECT;
        return this;
    }

    public JSONValue set(JSONArray value)
    {
        this.value = value;
        type = Type.ARRAY;
        return this;
    }

    public JSONValue set()
    {
        this.value = null;
        type = Type.NULL;
        return this;
    }

    public enum Type
    {
        NUMBER,
        STRING,
        BOOLEAN,
        ARRAY,
        OBJECT,
        NULL
    }
}
