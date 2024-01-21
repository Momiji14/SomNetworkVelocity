package SomNetworkVelocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.GameProfile;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static SomNetworkVelocity.Function.decoColor;
import static SomNetworkVelocity.Function.decoLore;

public class SomTabList {
    public static final String space = "    ";
    private static final String header = "§bSword of Magic Network";
    private static final HashMap<UUID, TabListEntry.Builder> entities = new HashMap<>();
    public static void run() {
        for (Player player : SomCore.getProxy().getAllPlayers()) {
            player.getTabList().clearAll();
        }

        for (Player player : SomCore.getProxy().getAllPlayers()) {
            joinPlayer(player);
        }

        SomTask.timer(() -> {
            for (Map.Entry<UUID, TabListEntry.Builder> entry : entities.entrySet()) {
                UUID uuid = entry.getKey();
                TabListEntry.Builder builder = entry.getValue();
                Player entryPlayer = SomCore.getProxy().getPlayer(uuid).orElseThrow();
                String displayName = PlayerData.fromPlayer(entryPlayer).getTabListName();
                builder.displayName(Component.text(displayName));
                builder.latency((int) entryPlayer.getPing());
            }
            for (Player player : SomCore.getProxy().getAllPlayers()) {
                updateHeaderFooter(player);
                updateTabList(player);
            }
        }, 1000);
    }

    public static void updateTabList(Player player) {
        TabList tabList = player.getTabList();
        for (TabListEntry.Builder builder : entities.values()) {
            tabList.addEntry(builder.tabList(tabList).build());
        }

    }

    public static void joinPlayer(Player entryPlayer) {
        TabListEntry.Builder builder = TabListEntry.builder();
        GameProfile profile = new GameProfile(UUID.randomUUID(), entryPlayer.getUsername(), entryPlayer.getGameProfileProperties());
        builder.profile(profile);
        entities.put(entryPlayer.getUniqueId(), builder);
    }

    public static void quitPlayer(Player entryPlayer) {
        entities.remove(entryPlayer.getUniqueId());
    }

    public static void updateHeaderFooter(Player player) {
        String serverName = player.getCurrentServer().isPresent() ? player.getCurrentServer().get().getServerInfo().getName() : "未接続";
        String locale = player.getEffectiveLocale() != null ? player.getEffectiveLocale().getCountry() : "××";
        String footer = decoColor("Players") + SomCore.getProxy().getPlayerCount()
                + space + decoColor("Server") + serverName
                + space + decoColor("Ping") + player.getPing() + "ms"
                + space + decoColor("Client") + player.getClientBrand()
                + space + decoColor("Locale") + locale
                ;
        player.sendPlayerListHeaderAndFooter(Component.text(header), Component.text(footer));
    }

    @NotNull
    private static List<Player> getPlayers() {
        List<Player> list = new ArrayList<>(SomCore.getProxy().getAllPlayers());
        list.sort((player1, player2) -> {
            if (player1.getCurrentServer().isPresent() && player2.getCurrentServer().isPresent()) {
                RegisteredServer server1 = player1.getCurrentServer().get().getServer();
                RegisteredServer server2 = player2.getCurrentServer().get().getServer();
                return server1.getServerInfo().getName().compareTo(server2.getServerInfo().getName());
            }
            return player1.getUsername().compareTo(player2.getUsername());
        });
        return list;
    }
}
