package main;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Parser {
	public static void main(String[] args) {
		String json = "{\"id\":1,\"name\":\"샘플데이터\",\"arr\":[{\"id\":1, \"name\":\"최선한\", \"age\":30},{\"id\":2, \"name\":\"이현석\", \"age\":15},{\"id\":3, \"name\":\"안현석\", \"age\":20}]}";
		
		JsonObject jsonObj = JsonParser.parseString(json).getAsJsonObject();
		String name = jsonObj.get("name").getAsString();
		
		JsonArray arr = jsonObj.get("arr").getAsJsonArray();
		
		for( JsonElement item : arr ){
			JsonObject obj = item.getAsJsonObject();
			String itemName = obj.get("name").getAsString();
			System.out.println(itemName);
		}
		
		
	}
}
