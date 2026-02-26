package plugin.siren.Commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import plugin.siren.Commands.Marry.*;

public class MarryCmd extends AbstractCommandCollection {
    public MarryCmd(){
        super("marry","Marry command line");

        this.addSubCommand(new MarryPlayerCmd());
        this.addSubCommand(new PartnerCmd());
        this.addSubCommand(new TPCmd());
        this.addSubCommand(new MessageCmd());
        this.addSubCommand(new DivorceCmd());
        this.addSubCommand(new MarriedListCmd());

        this.setPermissionGroup(GameMode.Adventure);
    }
}
