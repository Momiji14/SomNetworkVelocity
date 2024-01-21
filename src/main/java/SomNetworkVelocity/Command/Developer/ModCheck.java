package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.DiscordBot;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

import java.util.List;

public class ModCheck extends SomCommand {
    public ModCheck() {
        super("modCheck");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        SomCore.getProxy().getPlayer(args[0]).ifPresentOrElse(
                player -> player.getModInfo().ifPresentOrElse(
                    mobInfo -> sender.sendMessage(player.getUsername() + ": §a" + mobInfo),
                    () -> sender.sendMessage(player.getUsername() + ": §cMod未使用")
                ), () -> sender.sendMessage("存在しないPlayerです"));
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
