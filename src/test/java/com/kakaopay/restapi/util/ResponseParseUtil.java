package com.kakaopay.restapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.test.web.servlet.MvcResult;

public class ResponseParseUtil {

	private static JSONParser jsonParser = new JSONParser();

	public static JSONObject getResponseAsJson(MvcResult result) throws Exception {
		String content = result.getResponse().getContentAsString();
		JSONObject json = (JSONObject)jsonParser.parse(content);
		return json;
	}

	public static JSONObject getDataAsJson(MvcResult result) throws Exception {
		JSONObject json = getResponseAsJson(result);
		JSONObject data = (JSONObject)json.get("data");
		return data;
	}

	public static JSONArray getDataAsJsonArray(MvcResult result) throws Exception {
		JSONObject json = getResponseAsJson(result);
		JSONArray data = (JSONArray)json.get("data");
		return data;
	}
}
