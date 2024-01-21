package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.*;
import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;

import java.util.List;

public class SNVReload extends SomCommand {
    public SNVReload() {
        super("SNVReload");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        return false;
    }

    @Override
    public void command(SomSender sender, String[] args) {
        DiscordBot.shutdown();
        SomTask.delay(() -> {
            SomCore.getProxy().getCommandManager().executeAsync(SomCore.getProxy().getConsoleCommandSource(), "vsu unloadplugin somnetworkvelocity");
            Updater.UpdatePlugin();
            SomCore.getProxy().getCommandManager().executeAsync(SomCore.getProxy().getConsoleCommandSource(), "vsu loadplugin SomNetworkVelocity.jar");
        }, 1000);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
