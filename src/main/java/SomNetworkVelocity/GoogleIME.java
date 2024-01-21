package SomNetworkVelocity;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class GoogleIME {

    private static final String GOOGLE_IME_URL = "https://www.google.com/transliterate?langpair=ja-Hira|ja&text=";

    public static String convByGoogleIME(String org) {
        return conv(org);
    }

    // 変換の実行
    private static String conv(String org) {
        if (org.length() == 0) {
            return "";
        }

        HttpURLConnection urlconn = null;
        BufferedReader reader = null;
        try {
            String baseurl;
            String encode = "UTF-8";
            baseurl = GOOGLE_IME_URL + URLEncoder.encode(org, StandardCharsets.UTF_8);

            URL url = new URL(baseurl);

            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.setInstanceFollowRedirects(false);
            urlconn.connect();

            reader = new BufferedReader(
                    new InputStreamReader(urlconn.getInputStream(), encode));

            String json = CharStreams.toString(reader);
            return GoogleIME.parseJson(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlconn != null) {
                urlconn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }

        return "";
    }

    public static String parseJson(String json) {
        StringBuilder result = new StringBuilder();
        for (JsonElement response : new Gson().fromJson(json, JsonArray.class)) {
            result.append(response.getAsJsonArray().get(1).getAsJsonArray().get(0).getAsString());
        }
        return result.toString();
    }
}
