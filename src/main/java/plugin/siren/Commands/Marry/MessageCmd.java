package plugin.siren.Commands.Marry;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
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
import plugin.siren.Systems.MarriageSettings;

import javax.annotation.Nonnull;
import java.awt.*;

public class MessageCmd extends AbstractPlayerCommand {
    public MessageCmd() {
        super("msg", "server.commands.marry.message.desc");

        if(Marriage.getConfig().get().ifCmdPermission()){
            this.requirePermission("marriage.msg");
            this.setPermissionGroup(GameMode.Creative);
        }else{
            this.setPermissionGroup(GameMode.Adventure);
        }

        setAllowsExtraArguments(true);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        MarriageSettings marriageSettings = store.getComponent(ref, Marriage.get().getMarriageSettingsComponentType());

        if(marriageSettings == null){
            Marriage.LOGGER.atInfo().log("Failed to get Marriage Settings Component : MessageCmd");
        }else{
            if(marriageSettings.isMarried()){
                PlayerRef partnerPlayerRef = Universe.get().getPlayer(marriageSettings.getPartnerUUID());
                if(partnerPlayerRef == null){
                    Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef : MessageCmd");
                    player.sendMessage(Message.translation("server.commands.marry.message.partnerOffline.player.msg"));
                }else {
                    String msg = commandContext.getInputString();

                    if(msg.length() < 10){
                        player.sendMessage(Message.translation("server.commands.marry.message.missingMsg.player.msg"));
                    }else{
                        msg = msg.substring(10);

                        Player partnerPlayerComp = store.getComponent(partnerPlayerRef.getReference(), Player.getComponentType());
                        if(partnerPlayerComp == null){
                            Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef Player Component : MessageCmd");
                        }else{
                            partnerPlayerComp.sendMessage(Message.raw(playerRef.getUsername() + ": " + msg).color(Color.PINK));
                            player.sendMessage(Message.translation("server.commands.marry.message.sentMsg").param("partnerUsername",partnerPlayerRef.getUsername()).param("msg",msg));
                        }
                    }
                }
            }else{
                player.sendMessage(Message.translation("server.commands.marry.message.unmarried"));
            }
        }

        if(Marriage.ifDebug()) {
            Marriage.LOGGER.atInfo().log(player.getDisplayName() + " successfully ran partner msg command.");
        }
    }
}