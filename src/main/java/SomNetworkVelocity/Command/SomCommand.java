package SomNetworkVelocity.Command;

import SomNetworkVelocity.Command.Developer.*;
import SomNetworkVelocity.Command.Player.*;
import SomNetworkVelocity.PlayerData;
import SomNetworkVelocity.SomCore;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class SomCommand implements SimpleCommand {

    public static List<String> completeAllPlayers() {
        List<String> complete = new ArrayList<>();
        for (Player player : SomCore.getProxy().getAllPlayers()) {
            complete.add(player.getUsername());
        }
        return complete;
    }

    private boolean isDevCommand = false;
    public SomCommand(String command) {
        CommandMeta commandMeta = SomCore.getProxy().getCommandManager().metaBuilder(command).build();
        SomCore.getProxy().getCommandManager().register(commandMeta, this);
    }

    public SomCommand(String command, String... alias) {
        CommandMeta commandMeta = SomCore.getProxy().getCommandManager().metaBuilder(command).aliases(alias).build();
        SomCore.getProxy().getCommandManager().register(commandMeta, this);
    }

    @Override
    public void execute(Invocation invocation) {
        SomSender sender = new SomSender(invocation.source());
        if (isDevCommand) {
            if (!invocation.source().hasPermission("snv.dev")) {
                sender.sendMessage("§c権限がありません");
                return;
            }
        }
        String[] args = invocation.arguments();
        if (sender.source() instanceof Player player && commandPlayer(PlayerData.fromPlayer(player), args)) return;
        command(sender, args);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> base = tabComplete(invocation.arguments());
        List<String> list = new ArrayList<>();
        if (base != null) list.addAll(base);
        String[] args = invocation.arguments();
        //if (!args[args.length-1].isEmpty()) list.removeIf(tab -> !tab.toLowerCase().contains(args[args.length-1].toLowerCase()));
        return CompletableFuture.completedFuture(list);
    }

    public void setDevCommand(boolean devCommand) {
        isDevCommand = devCommand;
    }

    public abstract boolean commandPlayer(PlayerData playerData, String[] args);
    public abstract void command(SomSender sender, String[] args);
    public abstract List<String> tabComplete(String[] args);

    public static void register() {
        new SNVReload().setDevCommand(true);
        new ShutDown().setDevCommand(true);
        new CommandUpdate().setDevCommand(true);
        new IP().setDevCommand(true);
        new IPCheck().setDevCommand(true);
        new ModCheck().setDevCommand(true);
        new ProxyBarMessage().setDevCommand(true);
        new SpecialAlert().setDevCommand(true);
        new ProxyMute().setDevCommand(true);
        new AddIgnoreIP().setDevCommand(true);
        new TabListUpdate().setDevCommand(true);
        new UpdateStartMessage().setDevCommand(true);
        new UpdateEndMessage().setDevCommand(true);

        new UUID();
        new Hub();
        new Server();
        new Link();
        new Tell();
        new ReturnTell();
        new ChatRoom();
        new UpdateNick();
    }
}
