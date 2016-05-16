package com.shc.easyjson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            throw new ParseException(tokenizer, "Object notation must start with '{' symbol.");

        tokenizer.getNextToken();

        parseProperties(object, tokenizer);

        if (tokenizer.getCurrentToken().getType() != Token.Type.OBJECT_END)
            throw new ParseException(tokenizer, "Expected '}' token to end the object.");

        tokenizer.getNextToken();

        return object;
    }

    private static void parseProperties(JSONObject jsonObject, Tokenizer tokenizer) throws ParseException
    {
        Token keyToken = tokenizer.getCurrentToken();

        if (keyToken.getType() != Token.Type.STRING)
            throw new ParseException(tokenizer, "Expected STRING keys.");

        if (tokenizer.getNextToken().getType() != Token.Type.COLON)
            throw new ParseException(tokenizer, "Expected COLON (:) symbol after property key.");

        tokenizer.getNextToken();
        jsonObject.put(keyToken.getValue(), parseValue(tokenizer));

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
            throw new ParseException(tokenizer, "Expected '[' token.");

        currentToken = tokenizer.getNextToken();

        while (currentToken.getType() != Token.Type.ARRAY_END)
        {
            array.add(parseValue(tokenizer));
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
                        throw new ParseException(tokenizer, "Expected a value token after comma.");
                }
            }
            else if (currentToken.getType() != Token.Type.ARRAY_END)
                throw new ParseException(tokenizer, "Expected a comma after value.");
        }

        // Consume the array end token
        tokenizer.getNextToken();

        return array;
    }

    private static JSONValue parseValue(Tokenizer tokenizer) throws ParseException
    {
        Token currentToken = tokenizer.getCurrentToken();

        switch (currentToken.getType())
        {
            case STRING:
                tokenizer.getNextToken();
                return new JSONValue(currentToken.getValue());

            case NUMBER:
                tokenizer.getNextToken();
                return new JSONValue(Double.parseDouble(currentToken.getValue()));

            case BOOLEAN:
                tokenizer.getNextToken();
                return new JSONValue(Boolean.parseBoolean(currentToken.getValue()));

            case NULL:
                tokenizer.getNextToken();
                return new JSONValue();

            case ARRAY_BEGIN:
                return new JSONValue(parseArray(tokenizer));

            case OBJECT_BEGIN:
                return new JSONValue(parseObject(tokenizer));

            default:
                throw new ParseException(tokenizer, "Expected a value token.");
        }
    }

    public static String write(JSONObject json)
    {
        StringBuilder sb = new StringBuilder();
        writeObject(sb, 0, json);
        return sb.toString();
    }

    private static void newLine(StringBuilder builder, int indentation)
    {
        builder.append("\n");

        for (int i = 0; i < indentation; i++)
            builder.append("    ");
    }

    private static void writeObject(StringBuilder sb, int indentation, JSONObject object)
    {
        sb.append("{");
        newLine(sb, ++indentation);

        // Sort the keys in ascending order
        List<String> keyList = new ArrayList<>();
        keyList.addAll(object.keySet());
        Collections.sort(keyList, String::compareTo);

        int i = 0;

        for (String key : keyList)
        {
            writeString(sb, key);
            sb.append(": ");

            writeValue(sb, indentation, object.get(key));

            if (i != keyList.size() - 1)
            {
                sb.append(",");
                newLine(sb, indentation);
            }

            i++;
        }

        newLine(sb, --indentation);
        sb.append("}");
    }

    private static void writeArray(StringBuilder sb, int indentation, JSONArray array)
    {
        sb.append("[");
        newLine(sb, indentation + 1);

        int i = 0;

        for (JSONValue value : array)
        {
            writeValue(sb, indentation, value);

            if (i != array.size() - 1)
            {
                sb.append(",");
                newLine(sb, indentation + 1);
            }

            i++;
        }

        newLine(sb, indentation);
        sb.append("]");
    }

    private static void writeString(StringBuilder sb, String string)
    {
        sb.append("\"");

        sb.append(string.replaceAll("\b", "\\b")
                .replaceAll("\\f", "\\f")
                .replaceAll("\\n", "\\n")
                .replaceAll("\\\\", "\\\\")
                .replaceAll("\\r", "\\r")
                .replaceAll("\\t", "\\t")
                .replaceAll("\"", "\\\""));

        sb.append("\"");
    }

    private static void writeValue(StringBuilder sb, int indentation, JSONValue value)
    {
        switch (value.getType())
        {
            case STRING:
                writeString(sb, value.getValue());
                break;

            case NUMBER:
                double num = value.getValue();
                sb.append((num == ((long) num)) ? "" + ((long) num) : num);
                break;

            case NULL:
                sb.append("null");
                break;

            case OBJECT:
                writeObject(sb, indentation + 1, value.getValue());
                break;

            case ARRAY:
                writeArray(sb, indentation + 1, value.getValue());
                break;

            case BOOLEAN:
                boolean condition = value.getValue();
                sb.append(condition ? "true" : "false");
                break;
        }
    }
}
