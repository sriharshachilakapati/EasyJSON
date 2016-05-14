# EasyJSON

An easy to use JSON library which has a common API which supports both standard Java and also GWT. No JSNI is being used. All the parsing will be done through Java code. GWT users are required to use a GWT specific artifact, named `EasyJSON-gwt.jar`

## Parsing JSON

The library does not deal with the files, as it has to be compatible with GWT. You are required to read the file yourself into a string, and pass it to the library to parse.

~~~java
JSONObject json = JSON.parse(jsonString);
~~~

After parsing, it returns a `JSONObject` type, which is equivalent to `HashMap<String, JSONValue>`, that is a map of string keys to values. If an error occurs, a `ParseException` gets thrown which gives clear error message, including the line number and cursor position, and also what is expected there.

~~~
com.shc.easyjson.ParseException: Error at line 9 column 23: Expected '}' token to end the object. Got STRING
	at com.shc.easyjson.JSON.parseObject(JSON.java:34)
	at com.shc.easyjson.JSON.parseValue(JSON.java:131)
	at com.shc.easyjson.JSON.parseArray(JSON.java:74)
	at com.shc.easyjson.JSON.parseValue(JSON.java:128)
	at com.shc.easyjson.JSON.parseProperties(JSON.java:52)
	at com.shc.easyjson.JSON.parseObject(JSON.java:31)
	at com.shc.easyjson.JSON.parse(JSON.java:17)
~~~

This is an example error message produced by the parser. If there is no error, you are good to go.

## JSON Types

The JSON specification defines some data types which are values. The following table defines how they are treated in Java with this library.

| JSON Type | EasyJSON objects | Native Java Types        |
|-----------|------------------|--------------------------|
| `Object`  | `JSONObject`     | `Map<String, JSONValue>` |
| `Array`   | `JSONArray`      | `List<JSONValue>`        |
| `Number`  | `JSONValue`      | `Double`                 |
| `Boolean` | `JSONValue`      | `Boolean`                |
| `Null`    | `JSONValue`      | `Object`                 |
| `String`  | `JSONValue`      | `String`                 |

The class `JSONValue` is used to wrap the different value types, and contains an enumeration field called as type. For example, if the data type matches, you can just call the `getValue()` method and assign to any type.

## Example usage

Assume the following JSON data structure. We have an array of people, where each person is an object.

~~~json
{
    "people": [
        { "firstName": "Harsha", "lastName": "Chilakapati", "age": 21 },
        { "firstName": "Ramu", "lastName": "Chilakapati", "age": 32 }
    ]
}
~~~

~~~java
JSONObject json = JSON.parse(readFile("myjson.json"));

JSONArray people = json.get("people").getValue();

// A JSONArray extends from ArrayList class, you can use for-each statement
// or even the new .forEach(Consumer) from the Java 8 syntax.
for (JSONValue item : people)
{
    JSONObject person = item.getValue();

    // Get the properties
    String fName = person.get("firstName").getValue();
    String lName = person.get("lastName").getValue();

    // If you are reading a double, you can cast the value directly to the double.
    int age = ((Number) person.get("age").getValue()).intValue();

    // Do whatever you want with them
}
~~~

This is how you will be reading values from the JSON string. The generic return types make this easy to use.

## Writing JSON

Not only reading, but EasyJSON can also create JSON strings from code. This is how you can create JSON.

~~~java
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

System.out.println(JSON.write(root));
~~~

And it gives you the following result.

~~~json
{
    "test5": [
            {
                "test4": null,
                "test3": true
            },
            {
                "test4": null,
                "test3": true
            }
        ],
    "test2": 3.0,
    "test1": "Hello World"
}
~~~

If you have any doubts regarding this, drop me an e-mail at [hello@goharsha.com](mailto://hello@goharsha.com).

## License

This is licensed under MIT license, and is free to use even in commercial applications.
