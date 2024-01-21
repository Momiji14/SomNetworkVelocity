package SomNetworkVelocity.Command.Player;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.DiscordBot;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;

import java.util.List;

public class Link extends SomCommand {
    public Link() {
        super("link");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        if (playerData.isLink()) {
            playerData.sendMessage("§aすでに§f" + playerData.getUser().getName() + "§aと§eリンク§aしています");
        } else if (DiscordBot.getRequestLink().containsKey(playerData.getPlayer())) {
            DiscordBot.link(DiscordBot.getRequestLink().get(playerData.getPlayer()), playerData);
        } else {
            playerData.sendMessage("§9Discord§aで§eコマンド§aを実行してください §e/link <mcid>");
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
