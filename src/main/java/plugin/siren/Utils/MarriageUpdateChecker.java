package plugin.siren.Utils;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import plugin.siren.Marriage;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class MarriageUpdateChecker {
    public static String checkForUpdate(){
        try{
            URL url = new URL("https://api.mermaids.dev/versions/marriage/release/");

            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    if(line.contains("<h1>{ version: ") && line.contains(" }</h1>")){
                        line = line.substring(line.indexOf("<h1>{ version: ") + 15);
                        line = line.substring(0, line.indexOf(" }</"));
                        return line;
                    }
                }
            }
        } catch (Exception e) {
            return Marriage.getVersion();
        }

        return Marriage.getVersion();
    }

    public static void sendUpdateMessage(){
        sendUpdateMessage(null, false);
    }

    public static void sendUpdateMessage(Type type){
        if(Type.StartUp.getValue() == type.getValue()) {
            String recentVersion = checkForUpdate();
            if(!Marriage.getVersion().equalsIgnoreCase(recentVersion)) {
                Marriage.LOGGER.atInfo().log("= =- -=- -=- -=- -=- -=- -=- -=- -= =");
                String versionMessage = "The Marriage Mod version is outdated, Marriage has released v" + recentVersion +".";
                Marriage.LOGGER.atInfo().log(versionMessage);
            }

            Runnable updateCheckRunnable = new Runnable() {
                @Override
                public void run() {
                    sendUpdateMessage();
                }
            };

            HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(updateCheckRunnable, 15, 60*60*6, TimeUnit.SECONDS);
        }else{
            sendUpdateMessage();
        }
    }

    public static void sendUpdateMessage(Player player){
        sendUpdateMessage(player, true);
    }

    public static void sendUpdateMessage(@Nullable Player player, boolean sendToPlayer){
        String recentVersion = checkForUpdate();
        if(!Marriage.getVersion().equalsIgnoreCase(recentVersion)){

            String translationId = "server.updateChecker.marriage.release.message";
            Message versionMessage = Message.translation(translationId).param("version", recentVersion);

            Marriage.LOGGER.atInfo().log(versionMessage.getAnsiMessage());

            if(sendToPlayer && player != null) {
                if (player.hasPermission("*") && Marriage.getConfig().get().ifNewVersion()) {
                    player.sendMessage(versionMessage.color(Color.RED));
                }
            }
        }
    }

    public enum Type {
        StartUp(0);

        private final int value;
        private Type(int value){
            this.value = value;
        }

        public int getValue(){
            return this.value;
        }
    }
}
