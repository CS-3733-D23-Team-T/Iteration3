package edu.wpi.tacticaltritons.styling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Translation {
    private static String langTo = "es";
    public static String translate(String text) throws IOException {
        // INSERT YOU URL HERE
        if(!langTo.equals("en")) {
            String urlStr = "https://script.google.com/macros/s/AKfycbzK-CND8_KrYPzpW26xpZOrebVUe3RRRNRmBDJYB6EGrR3FNXB7HV8so0iB1BhQr6h5/exec" +
                    "?q=" + URLEncoder.encode(text, "UTF-8") +
                    "&target=" + langTo +
                    "&source=" + "en";
            URL url = new URL(urlStr);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            byte[] ptext = response.toString().getBytes(StandardCharsets.ISO_8859_1);
            String string = new String(ptext, StandardCharsets.UTF_8);
            System.out.println(string);
            return string;
        }
        else{
            return text;
        }
    }

}