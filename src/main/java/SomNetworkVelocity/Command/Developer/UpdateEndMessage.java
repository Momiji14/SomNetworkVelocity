package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.PlayerData;

import java.util.List;

public class UpdateEndMessage extends SomCommand {
    public UpdateEndMessage() {
        super("updateEndMessage");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        new ProxyBarMessage.Timer("アップデート作業が終わりました", 15);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
