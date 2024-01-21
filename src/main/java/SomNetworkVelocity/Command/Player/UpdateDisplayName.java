package SomNetworkVelocity.Command.Player;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.DiscordBot;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomSQL;

import java.util.List;

public class UpdateDisplayName extends SomCommand {
    public UpdateDisplayName() {
        super("updateDisplayName");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        playerData.updateDisplayName();
        playerData.sendMessage("§eProxy表示名§aを§b更新§aしました");
        return true;
    }

    @Override
    public void command(SomSender sender, String[] args) {

    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }

    public static void set(PlayerData playerData) {
        DiscordBot.getGuild().modifyNickname(playerData.getMember(), playerData.getName()).queue();
    }
}
