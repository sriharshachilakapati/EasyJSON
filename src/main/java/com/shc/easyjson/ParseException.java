package com.shc.easyjson;

/**
 * @author Sri Harsha Chilakapati
 */
public class ParseException extends Exception
{
    ParseException(StringStream source, String description)
    {
        super("Error at line " + source.getLineNumber() + " column " + source.getCharColumn() + ": " + description);
    }

    ParseException()
    {
        this(null, "Error");
    }
}
