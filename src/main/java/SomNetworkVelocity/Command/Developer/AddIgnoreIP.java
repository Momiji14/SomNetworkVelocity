package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.*;

import java.util.List;

public class AddIgnoreIP extends SomCommand {
    public AddIgnoreIP() {
        super("addIgnoreIP");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        SomSQL.setSql("IgnoreIP", "UUID", args[0], "Username", args[1]);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
