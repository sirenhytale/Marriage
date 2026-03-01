package plugin.siren.Commands.Marry;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.siren.Marriage;
import plugin.siren.Systems.MarriageSettings;

import javax.annotation.Nonnull;

public class PartnerCmd extends AbstractPlayerCommand {
    public PartnerCmd() {
        super("partner", "View your marriage partner.");

        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        MarriageSettings marriageSettings = store.getComponent(ref, Marriage.get().getMarriageSettingsComponentType());

        if(marriageSettings == null){
            Marriage.LOGGER.atInfo().log("Failed to get Marriage Settings Component : PartnerCmd");
        }else{
            if(marriageSettings.isMarried()){
                PlayerRef partnerPlayerRef = Universe.get().getPlayer(marriageSettings.getPartnerUUID());
                if(partnerPlayerRef == null){
                    Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef : PartnerCmd, partner is probably offline");
                    player.sendMessage(Message.translation("server.commands.marry.partner.player.msg.offline").param("partnerUsername", marriageSettings.getPartnerUsername()));
                }else {
                    player.sendMessage(Message.translation("server.commands.marry.partner.player.msg").param("partnerUsername", partnerPlayerRef.getUsername()));
                }
            }else{
                player.sendMessage(Message.translation("server.commands.marry.partner.unmarried"));
            }
        }

        if(Marriage.ifDebug()) {
            Marriage.LOGGER.atInfo().log(player.getDisplayName() + " successfully ran partner command.");
        }
    }
}