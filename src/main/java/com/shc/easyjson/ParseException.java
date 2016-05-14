package com.shc.easyjson;

/**
 * @author Sri Harsha Chilakapati
 */
public class ParseException extends Exception
{
    public ParseException(StringStream source, String description)
    {
        super("Error at line " + source.getLineNumber() + " column " + source.getCharColumn() + ": " + description);
    }

    public ParseException()
    {
        this(null, "Error");
    }
}
