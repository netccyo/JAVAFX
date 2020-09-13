package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import com.google.gson.Gson;

public class MainApp {
	public static void main(String[] args) {
		String clientId = "5XJV_DxoWoXsZAug3VHU";
		String clientSecret = "FIdG2yv1jo";
		String apiURL = "https://openapi.naver.com/v1/search/adult.json";
		
		Scanner in = new Scanner(System.in);
		System.out.println("판별을 원하는 단어를 입력하세요");
		String word = in.nextLine();
		
		try {
			word = URLEncoder.encode(word, "UTF-8");
			apiURL += "?query=" + word;
			
			URL url = new URL(apiURL);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			int responseCode = con.getResponseCode();
			
			BufferedReader br;
			
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			
			String line ="";
			StringBuffer response = new StringBuffer();
			while((line = br.readLine()) != null) {
				response.append(line);
			}
			br.close();
			
			String result = response.toString();
			
			Gson gson = new Gson();
			ResponseVO json = gson.fromJson(result, ResponseVO.class);
			System.out.println(json.adult);
			
//			System.out.println(response.toString());			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
