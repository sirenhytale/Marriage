package plugin.siren.Commands.Marriage;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.siren.Marriage;

import javax.annotation.Nonnull;

public class RequireRingCmd extends AbstractPlayerCommand {
    public RequireRingCmd() {
        super("requirering", "Toggles to require users to hold a ring to get married.");

        this.requirePermission("marriage.admin.requirering");
    }

    RequiredArg<Boolean> msgRequireRingArg = this.withRequiredArg("Require Ring", "Boolean to toggle user to hold ring to get married.", ArgTypes.BOOLEAN);

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        boolean requireRing = msgRequireRingArg.get(commandContext);

        Marriage.getConfig().get().setRequireRing(requireRing);
        Marriage.getConfig().save();

        String toggledStr = "";
        if (requireRing) {
            toggledStr = "Enabled";
        } else {
            toggledStr = "Disabled";
        }
        player.sendMessage(Message.raw("You have " + toggledStr + " requiring Ring to get married."));

        Marriage.LOGGER.atInfo().log(player.getDisplayName() + " has toggled required ring: " + String.valueOf(requireRing) + ".");
    }
}