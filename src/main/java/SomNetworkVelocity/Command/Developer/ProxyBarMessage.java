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

public class ProxyBarMessage extends SomCommand {
    public ProxyBarMessage() {
        super("proxyBarMessage");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        new Timer(args[0], Integer.parseInt(args[1]));
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }

    public static class Timer {
        private ScheduledTask task = null;
        private float timer = 1;
        private final float per;

        public Timer(String text, int time) {
            per = 1f/(time*20);
            BossBar bossBar = BossBar.bossBar(Component.text(text), 1, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            task = SomTask.timer(() -> {
                for (Player player : SomCore.getProxy().getAllPlayers()) {
                    bossBar.addViewer(player);
                }
                bossBar.progress(timer);
                timer -= per;
                if (timer < 0) {
                    task.cancel();
                    for (Player player : SomCore.getProxy().getAllPlayers()) {
                        bossBar.removeViewer(player);
                    }
                }
            }, 50);
        }
    }
}
