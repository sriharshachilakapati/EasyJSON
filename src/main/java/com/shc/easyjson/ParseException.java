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

    ParseException(Tokenizer tokenizer, String description)
    {
        this(tokenizer.getSourceStream(), description + " Got " + tokenizer.getCurrentToken().getType());
    }

    @SuppressWarnings("unused")
    private ParseException()
    {
    }
}
