package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.DiscordBot;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;

import java.util.Collections;
import java.util.List;

public class SpecialAlert extends SomCommand {
    public SpecialAlert() {
        super("specialAlert");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        SomCore.getProxy().getPlayer(args[0]).ifPresentOrElse(
                player -> {
                    PlayerData playerData = PlayerData.fromPlayer(player);
                    switch (args[1]) {
                        case "set" -> {
                            playerData.setSpecialAlert(args[2]);
                            sender.sendMessage(player.getUsername() + ": " + playerData.getSpecialAlert());
                        }
                        case "reset" -> {
                            playerData.resetAlert(args[2]);
                            sender.sendMessage(player.getUsername() + ": " + playerData.getSpecialAlert());
                        }
                    }
                },
                () -> sender.sendMessage("存在しないPlayerです"));
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) return completeAllPlayers();
        if (args.length == 2) return List.of(new String[]{"set", "reset"});
        if (args.length == 3) return Collections.singletonList("message");
        return null;
    }
}
