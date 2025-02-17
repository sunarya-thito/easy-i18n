package de.marhali.easyi18n.io.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.marhali.easyi18n.io.ArrayMapper;

/**
 * Map json array values.
 * @author marhali
 */
public class JsonArrayMapper extends ArrayMapper {
    public static String read(JsonArray array) {
        return read(array.iterator(), JsonElement::getAsString);
    }

    public static JsonArray write(String concat) {
        JsonArray array = new JsonArray();
        write(concat, array::add);
        return array;
    }
}