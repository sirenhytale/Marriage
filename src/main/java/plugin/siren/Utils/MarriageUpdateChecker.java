package plugin.siren.Utils;

import plugin.siren.Marriage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MarriageUpdateChecker {
    public static String checkForUpdate(){
        try{
            URL url = new URL("https://api.mermaids.dev/versions/marriage/release/");

            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    if(line.contains("<h1>") && line.contains("</h1>")){
                        line = line.substring(line.indexOf("<h1>") + 4);
                        line = line.substring(0, line.indexOf("</"));
                        return line;
                    }
                }
            }
        } catch (Exception e) {
            return Marriage.getVersion();
        }

        return Marriage.getVersion();
    }
}
