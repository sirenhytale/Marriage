package plugin.siren.Commands.Marry;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.siren.Marriage;
import plugin.siren.Systems.MarriageComponent;
import plugin.siren.Systems.MarriageSettingsComponent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class MarryPlayerCmd extends AbstractPlayerCommand {
    public MarryPlayerCmd() {
        super("player", "server.commands.marry.player.desc");

        if(Marriage.getConfig().get().ifCmdPermission()){
            this.requirePermission("marriage.marry");
            this.setPermissionGroup(GameMode.Creative);
        }else{
            this.setPermissionGroup(GameMode.Adventure);
        }
    }

    RequiredArg<PlayerRef> msgMarryPlayerArg = this.withRequiredArg("Player Username", "server.commands.marry.player.arg.username.desc", ArgTypes.PLAYER_REF);

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        PlayerRef partnerPlayerRef = msgMarryPlayerArg.get(commandContext);

        MarriageSettingsComponent marriageSettings = store.getComponent(ref, MarriageSettingsComponent.getComponentType());

        if(marriageSettings == null){
            Marriage.LOGGER.atInfo().log("Failed to get Marriage Settings Component : MarryPlayerCmd");
        }else {
            if (marriageSettings.isMarried()) {
                player.sendMessage(Message.translation("server.commands.marry.player.alreadyMarried"));
            } else {
                boolean marriageAllowed = false;
                if(Marriage.getConfig().get().ifRequireRing()){
                    InventoryComponent.Hotbar hotbarComponent = store.getComponent(ref, InventoryComponent.Hotbar.getComponentType());
                    if(hotbarComponent != null) {
                        ItemStack itemInHand = hotbarComponent.getActiveItem();
                        if (itemInHand != null && itemInHand.getItemId().equalsIgnoreCase("marriage_ring")) {
                            marriageAllowed = true;
                        } else {
                            player.sendMessage(Message.translation("server.commands.marry.player.missingRing"));
                        }
                    }
                }else{
                    marriageAllowed = true;
                }

                if(marriageAllowed) {
                    if (partnerPlayerRef == null || !partnerPlayerRef.isValid()) {
                        Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef reference : MarryPlayerCmd");
                    } else {
                        MarriageComponent partnerMarriage = store.getComponent(partnerPlayerRef.getReference(), MarriageComponent.getComponentType());

                        if (partnerMarriage == null) {
                            Marriage.LOGGER.atInfo().log("Failed to get partnerPlayerRef Marriage Component : MarryPlayerCmd");
                        } else {
                            List<PlayerRef> requests = partnerMarriage.getRequestsList();

                            boolean aRequest = false;

                            if (requests.isEmpty()) {
                                Marriage.LOGGER.atInfo().log(player.getDisplayName() + " has no current requests");
                            } else {
                                for (PlayerRef plyRefs : requests) {
                                    if (plyRefs == playerRef) {
                                        if (!playerRef.getUsername().equalsIgnoreCase(partnerPlayerRef.getUsername())) {
                                            aRequest = true;
                                        }
                                    }
                                }
                            }
                            MarriageComponent marriage = store.getComponent(ref, MarriageComponent.getComponentType());

                            if (marriage == null) {
                                Marriage.LOGGER.atInfo().log("Failed to get ref Marriage Component : MarryPlayerCmd");
                            } else {
                                marriage.addRequestToList(partnerPlayerRef);

                                Player playerMarryComp = store.getComponent(partnerPlayerRef.getReference(), Player.getComponentType());
                                if (aRequest) {
                                    MarriageSettingsComponent partnerMarriageSettings = store.getComponent(partnerPlayerRef.getReference(), MarriageSettingsComponent.getComponentType());

                                    marriageSettings.setPartnerUUID(partnerPlayerRef.getUuid());
                                    marriageSettings.setPartnerUsername(partnerPlayerRef.getUsername());
                                    marriageSettings.setMarried(true);
                                    marriage.clearRequestsList();

                                    partnerMarriageSettings.setPartnerUUID(playerRef.getUuid());
                                    partnerMarriageSettings.setPartnerUsername(playerRef.getUsername());
                                    partnerMarriageSettings.setMarried(true);
                                    partnerMarriage.clearRequestsList();

                                    player.sendMessage(Message.translation("server.commands.marry.player.marry.player.msg").param("partnerUsername",partnerPlayerRef.getUsername()));

                                    playerMarryComp.sendMessage(Message.translation("server.commands.marry.player.marry.player.msg").param("partnerUsername",playerRef.getUsername()));


                                    Message marriageMessage = Message.translation("server.commands.marry.player.console.alert").param("usernameOne", playerRef.getUsername()).param("usernameTwo", partnerPlayerRef.getUsername());
                                    List<PlayerRef> onlinePlayers = Universe.get().getPlayers();
                                    for(PlayerRef plyRef : onlinePlayers){
                                        Player ply = store.getComponent(plyRef.getReference(), Player.getComponentType());

                                        if(ply == null){
                                            Marriage.LOGGER.atFine().log("Failed to get onlinePlayer plyRef Player Component : MarryPlayerCmd");
                                        }else{
                                            ply.sendMessage(marriageMessage.color(Color.PINK));
                                        }
                                    }

                                    Marriage.LOGGER.atInfo().log(playerRef.getUsername() + " and " + partnerPlayerRef.getUsername() + " just got Married!");
                                } else {
                                    if (!playerRef.getUsername().equalsIgnoreCase(partnerPlayerRef.getUsername())) {
                                        player.sendMessage(Message.translation("server.commands.marry.player.request.player.msg").param("username",partnerPlayerRef.getUsername()));

                                        playerMarryComp.sendMessage(Message.translation("server.commands.marry.player.receiveRequest.player.msg").param("username",partnerPlayerRef.getUsername()));
                                    } else {
                                        player.sendMessage(Message.translation("server.commands.marry.player.self.player.msg"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if(Marriage.ifDebug()) {
            Marriage.LOGGER.atInfo().log(Message.translation("server.commands.marry.player.success").param("username",player.getDisplayName()).getAnsiMessage());
        }
    }
}