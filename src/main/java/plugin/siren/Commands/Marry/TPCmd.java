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
import plugin.siren.Systems.MarriageSettingsComponent;

import javax.annotation.Nonnull;

public class TPCmd extends AbstractPlayerCommand {
    public TPCmd() {
        super("tp", "Teleports the player to their married partner.");

        if(Marriage.getConfig().get().ifCmdPermission()){
            this.requirePermission("marriage.tp");
            this.setPermissionGroup(GameMode.Creative);
        }else{
            this.setPermissionGroup(GameMode.Adventure);
        }
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        MarriageSettingsComponent marriageSettings = store.getComponent(ref, MarriageSettingsComponent.getComponentType());

        if(marriageSettings == null){
            Marriage.LOGGER.atInfo().log("Failed to get Marriage Settings Component : PartnerTPCmd");
        }else{
            if(marriageSettings.isMarried()){
                PlayerRef partnerPlayerRef = Universe.get().getPlayer(marriageSettings.getPartnerUUID());
                if(partnerPlayerRef == null){
                    Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef : PartnerTPCmd, partner is probably offline");
                    player.sendMessage(Message.translation("server.commands.marry.tp.partnerOffline.player.msg"));
                }else {
                    String consoleRunCommand = "tp " + playerRef.getUsername() + " " + partnerPlayerRef.getUsername();
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, consoleRunCommand);

                    player.sendMessage(Message.translation("server.commands.marry.tp.player.msg"));

                    Player partnerPlayer = store.getComponent(partnerPlayerRef.getReference(), Player.getComponentType());
                    if(partnerPlayer == null){
                        Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef partnerPlayer Player Component : PartnerTPCmd");
                    }else{
                        partnerPlayer.sendMessage(Message.translation("server.commands.marry.tp.partner.msg"));
                    }
                }
            }else{
                player.sendMessage(Message.translation("server.commands.marry.tp.unmarried"));
            }
        }

        if(Marriage.ifDebug()) {
            Marriage.LOGGER.atInfo().log(Message.translation("server.commands.marry.tp.success").param("username",player.getDisplayName()).getAnsiMessage());
        }
    }
}