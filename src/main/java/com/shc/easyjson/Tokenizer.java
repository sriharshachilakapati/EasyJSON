package com.shc.easyjson;

/**
 * @author Sri Harsha Chilakapati
 */
public class Tokenizer
{
    private StringStream source;
    private Token        currentToken;

    public Tokenizer(String source)
    {
        this.source = new StringStream(source);
    }

    public Token getCurrentToken()
    {
        return currentToken;
    }

    public Token getNextToken() throws ParseException
    {
        return currentToken = new Token(source);
    }

    StringStream getSourceStream()
    {
        return source;
    }
}
