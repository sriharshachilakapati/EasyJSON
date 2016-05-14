package com.shc.easyjson;

import static com.shc.easyjson.StringStream.END_OF_INPUT;

/**
 * @author Sri Harsha Chilakapati
 */
public class Token
{
    private Type   type;
    private String value;

    Token(StringStream source) throws ParseException
    {
        value = "";
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
            value += currentChar;

            source.getNextChar();
        }
        else if (currentChar == '}')
        {
            type = Type.OBJECT_END;
            value += currentChar;

            source.getNextChar();
        }
        else if (currentChar == '[')
        {
            type = Type.ARRAY_BEGIN;
            value += currentChar;

            source.getNextChar();
        }
        else if (currentChar == ']')
        {
            type = Type.ARRAY_END;
            value += currentChar;

            source.getNextChar();
        }
        else if (currentChar == ',')
        {
            type = Type.COMMA;
            value += currentChar;

            source.getNextChar();
        }
        else if (currentChar == ':')
        {
            type = Type.COLON;
            value += currentChar;

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
            value += currentChar;
            source.getNextChar();
        }

        if (value.equals("true") || value.equals("false"))
            type = Type.BOOLEAN;

        if (value.equals("null"))
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
                    value += nextChar;

                else if (nextChar == 'b')
                    value += "\b";

                else if (nextChar == 'f')
                    value += "\f";

                else if (nextChar == 'n')
                    value += "\n";

                else if (nextChar == 'r')
                    value += "\r";

                else if (nextChar == 's')
                    value += " ";

                else if (nextChar == 't')
                    value += "\t";

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
                        value += code;
                }
            }
            else
            {
                value += currentChar;
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
            value += currentChar;
            source.getNextChar();
            eatWhiteSpace(source);
        }

        while (Character.isDigit(currentChar = source.getCurrentChar()))
        {
            value += currentChar;
            currentChar = source.getNextChar();

            if (currentChar == 'e'
                || currentChar == 'E'
                || currentChar == '.'
                || currentChar == '+'
                || currentChar == '-')
            {
                value += currentChar;
                source.getNextChar();
                eatWhiteSpace(source);
            }
        }

        value = "" + Double.parseDouble(value);
    }

    public Type getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
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
