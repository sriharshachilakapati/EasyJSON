package com.shc.easyjson.test;

import com.shc.easyjson.JSON;
import com.shc.easyjson.JSONArray;
import com.shc.easyjson.JSONObject;
import com.shc.easyjson.JSONValue;
import com.shc.easyjson.ParseException;
import com.shc.easyjson.Token;
import com.shc.easyjson.Tokenizer;

/**
 * @author Sri Harsha Chilakapati
 */
class TokensTest
{
    public static void main(String[] args)
    {
        String text;

        text = "{\n"
               + "\"employees\": [\n"
               + "    {\n"
               + "        \"firstName\": \"Harsha\",\n"
               + "        \"lastName\": \"Chilakapati\"\n"
               + "    },\n"
               + "    {\n"
               + "        \"firstName\": \"Teja\",\n"
               + "        \"lastName\": \"Chilakapati\"\n"
               + "    }\n"
               + "]\n"
               + "}";

        Tokenizer tokenizer = new Tokenizer(text);
        Token token;

        try
        {
            while ((token = tokenizer.getNextToken()).getType() != Token.Type.END_OF_FILE)
                System.out.println("Token: Type=" + token.getType() + " Value=" + token.getValue());

            JSONObject json = JSON.parse(text);

            JSONArray employees = json.get("employees").getValue();

            for (JSONValue value : employees)
            {
                JSONObject employee = value.getValue();

                System.out.println("Employee: " + employee.get("firstName").getValue()
                                   + " " + employee.get("lastName").getValue());
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
