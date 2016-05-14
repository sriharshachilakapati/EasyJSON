package com.shc.easyjson;

import static com.shc.easyjson.StringStream.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Token
{
    private Type          type;
    private StringBuilder value;

    Token(StringStream source) throws ParseException
    {
        value = new StringBuilder();
        extract(source);
    }

    private void extract(StringStream source) throws ParseException
    {
        eatWhiteSpace(source);

        char currentChar = source.getCurrentChar();

        if (Character.isDigit(currentChar) || currentChar == '+' || currentChar == '-')
            extractNumber(source);

        else if (currentChar == '"')
            extractString(source);

        else if (currentChar == '_' || Character.isLetter(currentChar))
            extractWord(source);

        else if (currentChar == '{')
        {
            type = Type.OBJECT_BEGIN;
            value.append(currentChar);

            source.getNextChar();
        }
        else if (currentChar == '}')
        {
            type = Type.OBJECT_END;
            value.append(currentChar);

            source.getNextChar();
        }
        else if (currentChar == '[')
        {
            type = Type.ARRAY_BEGIN;
            value.append(currentChar);

            source.getNextChar();
        }
        else if (currentChar == ']')
        {
            type = Type.ARRAY_END;
            value.append(currentChar);

            source.getNextChar();
        }
        else if (currentChar == ',')
        {
            type = Type.COMMA;
            value.append(currentChar);

            source.getNextChar();
        }
        else if (currentChar == ':')
        {
            type = Type.COLON;
            value.append(currentChar);

            source.getNextChar();
        }
        else
            type = Type.END_OF_FILE;
    }

    private void extractWord(StringStream source)
    {
        type = Type.WORD;

        char currentChar = source.getCurrentChar();

        while (Character.isLetter(currentChar)
               || Character.isDigit(currentChar)
               || currentChar == '_')
        {
            value.append(currentChar);
            currentChar = source.getNextChar();
        }

        if (value.toString().equals("true") || value.toString().equals("false"))
            type = Type.BOOLEAN;

        if (value.toString().equals("null"))
            type = Type.NULL;
    }

    private void extractString(StringStream source) throws ParseException
    {
        type = Type.STRING;

        char currentChar;
        source.getNextChar(); // Consume initial " symbol

        while ((currentChar = source.getCurrentChar()) != '"')
        {
            if (currentChar == END_OF_INPUT)
                throw new ParseException(source, "Incomplete string found");

            if (currentChar == '\\')
            {
                char nextChar = source.peekNextChar();

                if (nextChar == '\\' || nextChar == '"')
                    value.append(nextChar);

                else if (nextChar == 'b')
                    value.append("\b");

                else if (nextChar == 'f')
                    value.append("\f");

                else if (nextChar == 'n')
                    value.append("\n");

                else if (nextChar == 'r')
                    value.append("\r");

                else if (nextChar == 's')
                    value.append(" ");

                else if (nextChar == 't')
                    value.append("\t");

                source.getNextChar();
                source.getNextChar();

                if (nextChar == 'u')
                {
                    String codePoint = "";

                    for (int i = 0; i < 4; i++)
                    {
                        char ch = source.getNextChar();

                        if (ch == END_OF_INPUT)
                            throw new ParseException(source, "Unexpected end of file");

                        codePoint += ch;
                    }

                    int unicode = Integer.parseInt(codePoint.trim());

                    for (char code : Character.toChars(unicode))
                        value.append(code);
                }
            }
            else
            {
                value.append(currentChar);
                source.getNextChar();
            }
        }

        source.getNextChar(); // Consume last " symbol
    }

    private void eatWhiteSpace(StringStream source)
    {
        char currentChar = source.getCurrentChar();

        while (currentChar == ' ' || currentChar == '\t' || currentChar == '\r' || currentChar == '\n')
            currentChar = source.getNextChar();
    }

    private void extractNumber(StringStream source)
    {
        type = Type.NUMBER;

        char currentChar = source.getCurrentChar();

        if (currentChar == '+' || currentChar == '-')
        {
            value.append(currentChar);
            source.getNextChar();
            eatWhiteSpace(source);
        }

        while (Character.isDigit(currentChar = source.getCurrentChar()))
        {
            value.append(currentChar);
            currentChar = source.getNextChar();

            if (currentChar == 'e'
                || currentChar == 'E'
                || currentChar == '.'
                || currentChar == '+'
                || currentChar == '-')
            {
                value.append(currentChar);
                source.getNextChar();
                eatWhiteSpace(source);
            }
        }

        value.replace(0, value.length(), "" + Double.parseDouble(value.toString()));
    }

    public Type getType()
    {
        return type;
    }

    public String getValue()
    {
        return value.toString();
    }

    public enum Type
    {
        OBJECT_BEGIN,
        OBJECT_END,
        ARRAY_BEGIN,
        ARRAY_END,
        STRING,
        NUMBER,
        BOOLEAN,
        NULL,
        WORD,
        COMMA,
        COLON,
        END_OF_FILE
    }
}
