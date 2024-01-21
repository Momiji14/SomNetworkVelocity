package SomNetworkVelocity.Command.Player;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;
import SomNetworkVelocity.SomText;
import com.velocitypowered.api.proxy.Player;

import java.util.List;
import java.util.stream.Collectors;

public class UUID extends SomCommand {
    public UUID() {
        super("uuid");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        if (args.length == 0) {
            showUUID(playerData, playerData.getPlayer().getUsername());
            return true;
        } else {
            showUUID(playerData, args[0]);
        }
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
    }

    public void showUUID(PlayerData playerData, String target) {
        SomCore.getProxy().getPlayer(target).ifPresentOrElse(
                player -> playerData.sendSomText(SomText.create().addClipboard(player.getUsername() + ": " + player.getUniqueId().toString(), "§eクリックでコピー", player.getUniqueId().toString() + " " + player.getUsername())),
                () -> playerData.sendMessage("§c存在しないプレイヤーです")
        );
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) {
            return SomCore.getProxy().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return null;
    }
}
