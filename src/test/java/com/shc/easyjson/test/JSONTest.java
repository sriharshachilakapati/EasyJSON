package com.shc.easyjson.test;

import com.shc.easyjson.JSON;
import com.shc.easyjson.JSONArray;
import com.shc.easyjson.JSONObject;
import com.shc.easyjson.JSONValue;
import com.shc.easyjson.ParseException;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class JSONTest
{
    @Test
    public void testWrite()
    {
        String expected = "{\n"
                          + "    \"test5\": [\n"
                          + "            {\n"
                          + "                \"test4\": null,\n"
                          + "                \"test3\": true\n"
                          + "            },\n"
                          + "            {\n"
                          + "                \"test4\": null,\n"
                          + "                \"test3\": true\n"
                          + "            }\n"
                          + "        ],\n"
                          + "    \"test2\": 3.0,\n"
                          + "    \"test1\": \"Hello World\"\n"
                          + "}";

        JSONObject root = new JSONObject();
        root.put("test1", new JSONValue("Hello World"));
        root.put("test2", new JSONValue(3));

        JSONArray array = new JSONArray();

        for (int i = 0; i < 2; i++)
        {
            JSONObject obj = new JSONObject();
            obj.put("test3", new JSONValue(true));
            obj.put("test4", new JSONValue());

            array.add(new JSONValue(obj));
        }

        root.put("test5", new JSONValue(array));

        assertEquals(expected, JSON.write(root));
    }

    @Test(expected = ParseException.class)
    public void testParseError1() throws ParseException
    {
        JSON.parse("{,}");
    }

    @Test(expected = ParseException.class)
    public void testParseError2() throws ParseException
    {
        JSON.parse("");
    }

    @Test
    public void testEmployee1() throws ParseException
    {
        JSONObject json = JSON.parse(readFromFile("/json/employees.json"));

        JSONArray employees = json.get("employees").getValue();
        JSONObject harsha = employees.get(0).getValue();

        assertEquals("Harsha", harsha.get("firstName").getValue());
        assertEquals("Chilakapati", harsha.get("lastName").getValue());
        assertEquals(50000.0, harsha.get("salary").getValue(), 0);
    }

    @Test
    public void testEmployee2() throws ParseException
    {
        JSONObject json = JSON.parse(readFromFile("/json/employees.json"));

        JSONArray employees = json.get("employees").getValue();
        JSONObject teja = employees.get(1).getValue();

        assertEquals("Teja", teja.get("firstName").getValue());
        assertEquals("Chilakapati", teja.get("lastName").getValue());
        assertEquals(35000.59, teja.get("salary").getValue(), 0);
    }

    @Test
    public void testMultipleKeys() throws ParseException
    {
        JSONObject json = JSON.parse(readFromFile("/json/multipleKeys.json"));

        assertTrue(json.get("key5").getValue());
        assertNull(json.get("key6").getValue());

        assertEquals("value1", json.get("key1").getValue());
        assertEquals("value2", json.get("key2").getValue());
        assertEquals(1.0, ((JSONObject) json.get("key3").getValue()).get("key1").getValue(), 0);

        double[] expectedArray = { 3, 4, 6, 7 };
        double[] resultArray = new double[expectedArray.length];

        JSONArray key4 = json.get("key4").getValue();
        for (int i = 0; i < key4.size(); i++)
            resultArray[i] = key4.get(i).getValue();

        assertArrayEquals(expectedArray, resultArray, 0);
    }

    private String readFromFile(String fileName)
    {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName))))
        {
            String line;

            while ((line = br.readLine()) != null)
                sb.append(line).append("\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
