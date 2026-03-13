package plugin.siren.Events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.siren.Marriage;
import plugin.siren.Systems.MarriageComponent;
import plugin.siren.Systems.MarriageSettings;
import plugin.siren.Utils.MarriageUpdateChecker;

import java.awt.*;

public class PlayerReadyEventM {
    public static void onPlayerReadyEvent(PlayerReadyEvent event){
        World world = event.getPlayer().getWorld();
        world.execute(() -> {
            Player player = event.getPlayer();

            Ref<EntityStore> ref = event.getPlayerRef();
            Store<EntityStore> store = ref.getStore();

            MarriageComponent marComp = store.getComponent(ref, Marriage.get().getMarriageComponentType());
            if (marComp == null) {
                MarriageComponent marriageComponent = new MarriageComponent();

                store.putComponent(ref, Marriage.get().getMarriageComponentType(), marriageComponent);

                if (Marriage.ifDebug()) {
                    player.sendMessage(Message.raw("You now have the Marriage Component!"));

                    Marriage.LOGGER.atInfo().log(player.getDisplayName() + " now has the Marriage Component.");
                }
            }else{
                if (Marriage.ifDebug()) {
                    player.sendMessage(Message.raw("You already have the Marriage Component!"));

                    Marriage.LOGGER.atInfo().log(player.getDisplayName() + " tried to receive Marriage Component but already has it.");
                }
            }

            MarriageSettings marSett = store.getComponent(ref, Marriage.get().getMarriageSettingsComponentType());
            if (marSett == null) {
                MarriageSettings marriageSettings = new MarriageSettings();

                store.putComponent(ref, Marriage.get().getMarriageSettingsComponentType(), marriageSettings);

                if (Marriage.ifDebug()) {
                    player.sendMessage(Message.raw("You now have the Marriage Settings Component!"));

                    Marriage.LOGGER.atInfo().log(player.getDisplayName() + " now has the Marriage Settings Component.");
                }
            }else{
                if (Marriage.ifDebug()) {
                    player.sendMessage(Message.raw("You already have the Marriage Settings Component!"));

                    Marriage.LOGGER.atInfo().log(player.getDisplayName() + " tried to receive Marriage Settings Component but already has it.");
                }
            }

            String recentVersion = MarriageUpdateChecker.checkForUpdate();
            if(!Marriage.getVersion().equalsIgnoreCase(recentVersion)){
                String versionMessage = "The Marriage Mod version is outdated, Marriage has released v" + recentVersion +".";
                Marriage.LOGGER.atInfo().log(versionMessage);
                if(player.hasPermission("*") && Marriage.getConfig().get().ifNewVersion()){
                    player.sendMessage(Message.raw(versionMessage).color(Color.RED));
                }
            }
        });
    }
}
