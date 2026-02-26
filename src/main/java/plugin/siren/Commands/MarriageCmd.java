package plugin.siren.Commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import plugin.siren.Commands.Marriage.*;

public class MarriageCmd extends AbstractCommandCollection {
    public MarriageCmd(){
        super("marriage","Marriage mod command line");

        this.addSubCommand(new RequireRingCmd());

        this.requirePermission("marriage.admin");
        this.setPermissionGroup(GameMode.Creative);
    }
}
