package SomNetworkVelocity.Command.Developer;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.DiscordBot;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;
import SomNetworkVelocity.SomTabList;

import java.util.List;

public class TabListUpdate extends SomCommand {
    public TabListUpdate() {
        super("tabListUpdate");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        SomTabList.updateTabList(playerData.getPlayer());
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
