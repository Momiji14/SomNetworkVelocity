package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.DiscordBot;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;

import java.util.List;

public class ShutDown extends SomCommand {
    public ShutDown() {
        super("shutDown");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        if (playerData.getUsername().equals("MomiNeko")) {
            DiscordBot.shutdown();
        }
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
