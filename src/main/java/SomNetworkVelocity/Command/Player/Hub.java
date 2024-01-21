package SomNetworkVelocity.Command.Player;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;
import com.velocitypowered.api.proxy.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Hub extends SomCommand {
    public Hub() {
        super("hub");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        SomCore.getProxy().getServer("lobby").ifPresent(server -> playerData.getPlayer().createConnectionRequest(server).connect());
        return true;
    }

    @Override
    public void command(SomSender sender, String[] args) {

    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
