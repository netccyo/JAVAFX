package main;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Parser {
	public static void main(String[] args) {
		String json = "{\"id\":1,\"name\":\"���õ�����\",\"arr\":[{\"id\":1, \"name\":\"�ּ���\", \"age\":30},{\"id\":2, \"name\":\"������\", \"age\":15},{\"id\":3, \"name\":\"������\", \"age\":20}]}";
		
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
