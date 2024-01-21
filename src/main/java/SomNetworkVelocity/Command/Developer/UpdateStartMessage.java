package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;
import SomNetworkVelocity.SomTask;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.scheduler.ScheduledTask;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

import java.util.List;

public class UpdateStartMessage extends SomCommand {
    public UpdateStartMessage() {
        super("updateStartMessage");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        new ProxyBarMessage.Timer("アップデート作業のため再起動します", Integer.parseInt(args[0]));
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
