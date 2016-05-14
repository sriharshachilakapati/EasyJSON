package com.shc.easyjson;

class StringStream
{
    static final char END_OF_INPUT = (char) 0;

    private String string;

    private int lineNumber;
    private int charColumn;
    private int charIndex;

    StringStream(String string)
    {
        this.string = string;

        lineNumber = 0;
        charColumn = 0;
        charIndex = 0;
    }

    int getLineNumber()
    {
        return lineNumber;
    }

    int getCharColumn()
    {
        return charColumn;
    }

    char getNextChar()
    {
        charIndex++;
        charColumn++;

        if (charIndex == string.length())
            return END_OF_INPUT;

        char ch = string.charAt(charIndex);

        if (ch == '\n')
        {
            lineNumber++;
            charColumn = 0;
        }

        return ch;
    }

    char getCurrentChar()
    {
        if (charIndex == string.length())
            return END_OF_INPUT;

        return string.charAt(charIndex);
    }

    char peekNextChar()
    {
        if (charIndex + 1 >= string.length())
            return END_OF_INPUT;

        return string.charAt(charIndex + 1);
    }
}
