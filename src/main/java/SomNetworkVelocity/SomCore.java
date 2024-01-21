package SomNetworkVelocity;

import SomNetworkVelocity.Command.SomCommand;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.config.ProxyConfig;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.title.Title;

import java.nio.file.Path;
import java.time.Duration;
import java.util.logging.Logger;

@Plugin(
    id = "somnetworkvelocity",
    name = "SomNetworkVelocity",
    version = "alpha",
    description = "SomNetworkVelocity",
    authors = {"MomiNeko"}
)

public class SomCore {

    private static ProxyServer proxy;
    private static Logger logger;
    private static Path dataDirectory;
    private static SomCore plugin;
    public static String SNCChannel = "snc:main";

    @Inject
    public SomCore(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        SomCore.proxy = server;
        SomCore.logger = logger;
        SomCore.dataDirectory = dataDirectory;
        SomCore.plugin = this;
        logger.info("SomNetworkVelocity loaded !");
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        proxy.getEventManager().register(this, new Events());
        proxy.getChannelRegistrar().register(new LegacyChannelIdentifier(SNCChannel));
        SomCommand.register();
        SomSQL.connection();
        SomTranslator.initialize();
        DiscordBot.connect();
        //SomTabList.run();

        SomTask.timer(() -> {
            for (Player player : SomCore.getProxy().getAllPlayers()) {
                player.getCurrentServer().ifPresent(server -> {
                    if (server.getServerInfo().getName().equals("lobby")) {
                        player.showTitle(Title.title(Component.text(""), Component.text("§e/server"), Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ZERO)));
                    }
                });
            }
        }, 100);
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static SomCore getPlugin() {
        return plugin;
    }

    public static Path getDataDirectory() {
        return dataDirectory;
    }

    public static void broadcast(String message) {
        broadcast(SomText.create(message));
    }

    public static void broadcast(SomText message) {
        broadcast(message.toComponent());
    }

    public static void broadcast(Component component) {
        for (RegisteredServer server : getProxy().getAllServers()) {
            for (Player player : server.getPlayersConnected()) {
                player.sendMessage(component);
            }
        }
    }

    public static void Log(Exception e) {
        Log(e.toString());
        for (int i = 0; i < e.getStackTrace().length; i++) {
            Log(e.getStackTrace()[i].toString());
        }
    }
    public static void Log(String log) {
        Log(log, false);
    }

    public static void Log(String log, boolean stacktrace) {
        for (Player player : proxy.getAllPlayers()) {
            if (player.hasPermission("snv.dev")) {
                player.sendMessage(Component.text(log));
            }
        }
        logger.info(log);

        if (stacktrace) throw new RuntimeException("StackTrace Log");
    }
}