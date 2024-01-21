package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.List;

import static SomNetworkVelocity.SomCore.getProxy;

public class IPCheck extends SomCommand {
    public IPCheck() {
        super("ipCheck");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        for (Player otherPlayer : getProxy().getAllPlayers()) {
            if (otherPlayer.getRemoteAddress().getAddress().getHostAddress().equals(args[0])) {
                sender.sendMessage(otherPlayer.getUsername());
            }
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
