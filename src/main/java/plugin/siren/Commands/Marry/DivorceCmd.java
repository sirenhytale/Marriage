package plugin.siren.Commands.Marry;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.siren.Marriage;
import plugin.siren.Systems.MarriageComponent;
import plugin.siren.Systems.MarriageSettings;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DivorceCmd extends AbstractPlayerCommand {
    public DivorceCmd() {
        super("divorce", "server.commands.marry.divorce.desc");

        if(Marriage.getConfig().get().ifCmdPermission()){
            this.requirePermission("marriage.divorce");
            this.setPermissionGroup(GameMode.Creative);
        }else{
            this.setPermissionGroup(GameMode.Adventure);
        }
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        MarriageSettings marriageSettings = store.getComponent(ref, Marriage.get().getMarriageSettingsComponentType());

        if(marriageSettings == null){
            Marriage.LOGGER.atInfo().log("Failed to get Marriage Settings Component : DivorceCmd");
        }else{
            if(marriageSettings.isMarried()) {
                MarriageComponent marriage = store.getComponent(ref, Marriage.get().getMarriageComponentType());

                if (marriage == null) {
                    Marriage.LOGGER.atInfo().log("Failed to get Marriage Component : DivorceCmd");
                } else {
                    PlayerRef partnerPlayerRef = Universe.get().getPlayer(marriageSettings.getPartnerUUID());

                    if (partnerPlayerRef == null) {
                        Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef : DivorceCmd");
                    } else {
                        if (marriage.getDivorceTimer() >= 1) {
                            MarriageSettings partnerMarriageSettings = store.getComponent(partnerPlayerRef.getReference(), Marriage.get().getMarriageSettingsComponentType());
                            if (partnerMarriageSettings == null) {
                                Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef Marriage Settings Component : DivorceCmd");
                            } else {
                                partnerMarriageSettings.setMarried(false);
                                partnerMarriageSettings.clearPartnerUUID();
                                partnerMarriageSettings.setPartnerUsername("");
                            }

                            marriageSettings.setMarried(false);
                            marriageSettings.clearPartnerUUID();
                            marriageSettings.setPartnerUsername("");

                            player.sendMessage(Message.translation("server.commands.marry.divorce.player.msg").param("partnerUsername",partnerPlayerRef.getUsername()));

                            Player partnerPlayer = store.getComponent(partnerPlayerRef.getReference(), Player.getComponentType());
                            if (partnerPlayer == null) {
                                Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef Player Component : DivorceCmd");
                            } else {
                                partnerPlayer.sendMessage(Message.translation("server.commands.marry.divorce.player.msg").param("partnerUsername",playerRef.getUsername()));
                            }

                            //String consoleSayCommand = "say " + playerRef.getUsername() + " and " + partnerPlayerRef.getUsername() + " have gotten a Divorce!";
                            //CommandManager.get().handleCommand(ConsoleSender.INSTANCE, consoleSayCommand);
                            Message divorceMessage = Message.translation("server.commands.marry.divorce.console.alert").param("usernameOne", playerRef.getUsername()).param("usernameTwo", partnerPlayerRef.getUsername());
                            List<PlayerRef> onlinePlayers = Universe.get().getPlayers();
                            for(PlayerRef plyRef : onlinePlayers){
                                Player ply = store.getComponent(plyRef.getReference(), Player.getComponentType());

                                if(ply == null){
                                    Marriage.LOGGER.atFine().log("Failed to get onlinePlayer plyRef Player Component : MarryPlayerCmd");
                                }else{
                                    ply.sendMessage(divorceMessage);
                                }
                            }

                        } else {
                            marriage.setDivorceTimer(1);

                            player.sendMessage(Message.translation("server.commands.marry.divorce.confirmation"));

                            HytaleServer.SCHEDULED_EXECUTOR.schedule(() -> {
                                world.execute(() -> {
                                    marriage.setDivorceTimer(0);
                                });
                            }, 10, TimeUnit.SECONDS);
                        }
                    }
                }
            }else{
                player.sendMessage(Message.translation("server.commands.marry.divorce.unmarried"));
            }
        }

        if(Marriage.ifDebug()) {
            Marriage.LOGGER.atInfo().log(Message.translation("server.commands.marry.divorce.success").param("username",player.getDisplayName()).getAnsiMessage());
        }
    }
}