package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.DiscordBot;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;

import java.util.List;

public class CommandUpdate extends SomCommand {
    public CommandUpdate() {
        super("commandUpdate");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        DiscordBot.commandUpdate();
        SomCore.Log("§9DiscordBot §eCommand Register");
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
