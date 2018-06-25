package com.memeinfotech.aideye;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class GoogleTranslateMainActivity {
    private String key;

    public GoogleTranslateMainActivity(String apiKey) {
        key = apiKey;
        System.out.println("kkkkkkkkkkkkkkkkkkkk--------->>>>>>>>>>"+key);
    }

    public GoogleTranslateMainActivity() {

    }



    String translte(String text, String from, String to) {
        StringBuilder result = new StringBuilder();
        try {
            String encodedText = URLEncoder.encode(text, "UTF-8");
            String urlStr = "https://translation.googleapis.com/language/translate/v2?key=" + key + "&q=" + encodedText + "&target=" + to + "&source=" + from;
//            String urlStr = "https://translation.googleapis.com/language/translate/v2/detect?key=" + key + "&q=" + encodedText;
            System.out.println("uuuuurrrrrlllll=----------->>>>>>>>>>>"+encodedText);
            URL url = new URL(urlStr);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            InputStream stream;
//            List<InputStream> detections = GoogleTranslateMainActivity.detect(ImmutableList.of(from));
            if (conn.getResponseCode() == 200) //success
            {
                stream = conn.getInputStream();
            } else
                stream = conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            JsonParser parser = new JsonParser();

            JsonElement element = parser.parse(result.toString());

            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.get("error") == null) {
                    String translatedText = obj.get("data").getAsJsonObject().
                            get("translations").getAsJsonArray().
                            get(0).getAsJsonObject().
                            get("translatedText").getAsString();
                    System.out.println("translated text==------------->>>>>>>>"+obj);
//                    System.out.println("translated text==------------->>>>>>>>"+translatedText);
                    return translatedText;

                }
            }

            if (conn.getResponseCode() != 200) {
                System.err.println(result);
            }

        } catch (IOException | JsonSyntaxException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }
    public static void main(String[] args) {

        GoogleTranslateMainActivity translator = new GoogleTranslateMainActivity("AIzaSyAWPLjcaA8ortKKHxQmNlhiy9U48vCVB3M");
        String text = translator.translte("bahay", "tl", "en");
        System.out.println(text);
    }

}
