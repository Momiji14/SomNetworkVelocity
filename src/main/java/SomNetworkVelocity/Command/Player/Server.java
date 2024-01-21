package SomNetworkVelocity.Command.Player;

import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static SomNetworkVelocity.Function.decoText;

public class Server extends SomCommand {
    public Server() {
        super("server");
    }

    private static final String Prefix = "§b[Proxy]§r";

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        if (playerData.getSpecialAlert() != null) {
            playerData.sendMessage("§c現在利用できません\n§cもうしばらくお待ちください");
            return true;
        }
        if (args.length == 0) {
            playerData.sendMessage(decoText("Server List"));
            for (RegisteredServer server : SomCore.getProxy().getAllServers()) {
                playerData.sendMessage("§7・§r" + server.getServerInfo().getName());
            }
            playerData.sendMessage(Prefix + "§e移動先§aを§e入力§aしてください §e/server <server_id>");
        } else {
            playerData.sendMessage(Prefix + args[0] + "§aへ§b接続§aを試みています");
            Optional<ServerConnection> serverConnection = playerData.getPlayer().getCurrentServer();
            if (serverConnection.isEmpty() || serverConnection.get().getServerInfo().getName().equals("lobby")) {
                Optional<RegisteredServer> toServer = SomCore.getProxy().getServer(args[0]);
                toServer.ifPresentOrElse(server -> {
                    playerData.getPlayer().createConnectionRequest(toServer.get()).connect();
                }, () -> playerData.sendMessage(Prefix + "§eServerID§aが間違っています"));
            } else {
                playerData.sendMessage(Prefix + "§a一度§eLobby§aに戻ってください §e/hub");
            }
        }
        return true;
    }

    @Override
    public void command(SomSender sender, String[] args) {

    }

    @Override
    public List<String> tabComplete(String[] args) {
        List<String> complete = new ArrayList<>();
        for (RegisteredServer server : SomCore.getProxy().getAllServers()) {
            complete.add(server.getServerInfo().getName());
        }
        return complete;
    }
}
