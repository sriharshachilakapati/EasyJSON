package com.shc.easyjson;

/**
 * @author Sri Harsha Chilakapati
 */
public final class JSON
{
    private JSON()
    {
    }

    public static JSONObject parse(String text) throws ParseException
    {
        Tokenizer tokenizer = new Tokenizer(text);
        tokenizer.getNextToken();

        return parseObject(tokenizer); // Root JSON is an object
    }

    private static JSONObject parseObject(Tokenizer tokenizer) throws ParseException
    {
        JSONObject object = new JSONObject();

        Token token = tokenizer.getCurrentToken();

        if (token.getType() != Token.Type.OBJECT_BEGIN)
            throw new ParseException(tokenizer.getSourceStream(), "Object notation must start with '{' symbol");

        tokenizer.getNextToken();

        parseProperties(object, tokenizer);

        if (tokenizer.getCurrentToken().getType() == Token.Type.OBJECT_END)
            tokenizer.getNextToken();

        return object;
    }

    private static void parseProperties(JSONObject jsonObject, Tokenizer tokenizer) throws ParseException
    {
        Token keyToken = tokenizer.getCurrentToken();

        if (keyToken.getType() != Token.Type.STRING)
            throw new ParseException(tokenizer.getSourceStream(), "Expected STRING keys.");

        if (tokenizer.getNextToken().getType() != Token.Type.COLON)
            throw new ParseException(tokenizer.getSourceStream(), "Expected COLON (:) symbol after property key.");

        Token valueToken = tokenizer.getNextToken();

        switch (valueToken.getType())
        {
            case STRING:
                jsonObject.put(keyToken.getValue(), new JSONValue(valueToken.getValue()));
                tokenizer.getNextToken();
                break;

            case NUMBER:
                jsonObject.put(keyToken.getValue(), new JSONValue(Double.parseDouble(valueToken.getValue())));
                tokenizer.getNextToken();
                break;

            case BOOLEAN:
                jsonObject.put(keyToken.getValue(), new JSONValue(Boolean.parseBoolean(valueToken.getValue())));
                tokenizer.getNextToken();
                break;

            case NULL:
                jsonObject.put(keyToken.getValue(), new JSONValue((Object) null));
                tokenizer.getNextToken();
                break;

            case ARRAY_BEGIN:
                jsonObject.put(keyToken.getValue(), new JSONValue(parseArray(tokenizer)));
                break;

            case OBJECT_BEGIN:
                jsonObject.put(keyToken.getValue(), new JSONValue(parseObject(tokenizer)));
                break;

            default:
                throw new ParseException(tokenizer.getSourceStream(), "Expected a value token.");
        }

        if (tokenizer.getCurrentToken().getType() == Token.Type.COMMA)
        {
            tokenizer.getNextToken();
            parseProperties(jsonObject, tokenizer);
        }
    }

    private static JSONArray parseArray(Tokenizer tokenizer) throws ParseException
    {
        JSONArray array = new JSONArray();

        Token currentToken = tokenizer.getCurrentToken();

        if (currentToken.getType() != Token.Type.ARRAY_BEGIN)
            throw new ParseException(tokenizer.getSourceStream(), "Expected '[' token.");

        currentToken = tokenizer.getNextToken();

        while (currentToken.getType() != Token.Type.ARRAY_END)
        {
            switch (currentToken.getType())
            {
                case STRING:
                    array.add(new JSONValue(currentToken.getValue()));
                    tokenizer.getNextToken();
                    break;

                case NUMBER:
                    array.add(new JSONValue(Double.parseDouble(currentToken.getValue())));
                    tokenizer.getNextToken();
                    break;

                case BOOLEAN:
                    array.add(new JSONValue(Boolean.parseBoolean(currentToken.getValue())));
                    tokenizer.getNextToken();
                    break;

                case NULL:
                    array.add(new JSONValue((Object) null));
                    tokenizer.getNextToken();
                    break;

                case ARRAY_BEGIN:
                    array.add(new JSONValue(parseArray(tokenizer)));
                    break;

                case OBJECT_BEGIN:
                    array.add(new JSONValue(parseObject(tokenizer)));
                    break;

                default:
                    throw new ParseException(tokenizer.getSourceStream(), "Expected a value token. Got " + currentToken.getType());
            }

            currentToken = tokenizer.getCurrentToken();

            if (currentToken.getType() == Token.Type.COMMA)
            {
                currentToken = tokenizer.getNextToken();

                switch (currentToken.getType())
                {
                    case STRING:
                    case NUMBER:
                    case BOOLEAN:
                    case NULL:
                    case OBJECT_BEGIN:
                    case ARRAY_BEGIN:
                        break;

                    default:
                        throw new ParseException(tokenizer.getSourceStream(), "Expected a value token after comma.");
                }
            }
        }

        // Consume the array end token
        tokenizer.getNextToken();

        return array;
    }
}
