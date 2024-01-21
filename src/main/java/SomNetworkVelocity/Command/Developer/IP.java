package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.DiscordBot;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;
import SomNetworkVelocity.SomSQL;

import java.util.List;

public class IP extends SomCommand {
    public IP() {
        super("ip");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        SomCore.getProxy().getPlayer(args[0]).ifPresentOrElse(
                player -> sender.sendMessage(player.getUsername() + ": " + player.getRemoteAddress().getAddress().getHostAddress()),
                () -> sender.sendMessage("存在しないPlayerです"));
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) return completeAllPlayers();
        return null;
    }
}
