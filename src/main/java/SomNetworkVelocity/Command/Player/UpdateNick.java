package SomNetworkVelocity.Command.Player;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.DiscordBot;
import SomNetworkVelocity.PlayerData;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class UpdateNick extends SomCommand {
    public UpdateNick() {
        super("updateNick");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        if (playerData.isLink()) {
            set(playerData);
            playerData.sendMessage("§9Discord§aの§eニックネーム§aが§b更新§aされました");
        } else {
            playerData.sendMessage("§eリンク§aされていません");
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

    public static void set(PlayerData playerData) {
        DiscordBot.getGuild().modifyNickname(playerData.getMember(), playerData.getName()).queue();
    }
}
