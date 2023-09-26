package dev.cobblesword.libraries.common.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.parser.JSONParser;

import java.lang.reflect.Type;

public class JsonUtils
{
	private static final JSONParser JSON_PARSER = new JSONParser();
	private static final JsonParser GSON_PARSER = new JsonParser();
	
	public static JSONParser getJSONParser()
	{
		return JSON_PARSER;
	}
	
	public static JsonElement toJsonFromString(String jsonStr)
	{
		return GSON_PARSER.parse(jsonStr);
	}
	
	public static String toJson(Object obj)
	{
		return GsonUtil.getGson().toJson(obj);
	}
	
	public static <T> T fromJson(String obj, Class<T> clazz)
	{
		return GsonUtil.getGson().fromJson(obj, clazz);
	}

	public static Object fromJsonMap(String obj, Type type)
	{
		return GsonUtil.getGson().fromJson(obj, type);
	}
}