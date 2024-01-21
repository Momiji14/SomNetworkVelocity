package SomNetworkVelocity.Command.Player;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.List;

public class ReturnTell extends SomCommand {
    public ReturnTell() {
        super("r", "returnTell");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        if (args.length >= 1) {
            if (playerData.hasPartner()) {
                StringBuilder rawMessage = new StringBuilder();
                for (String arg : args) {
                    rawMessage.append(arg).append(" ");
                }
                Tell.directMessage(playerData, playerData.getTeller(), rawMessage.toString());
            } else {
                playerData.sendMessage("§e会話相手§aがいません");
            }
        } else {
            playerData.sendMessage("§e/returnTell <message>");
        }
        return true;
    }

    @Override
    public void command(SomSender sender, String[] args) {

    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) return completeAllPlayers();
        return null;
    }
}
