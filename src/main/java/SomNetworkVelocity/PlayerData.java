package SomNetworkVelocity;

import SomNetworkVelocity.Command.SomSender;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import static SomNetworkVelocity.Config.DateFormat;

public class PlayerData {

    public static String Table = "PlayerData";
    private static final HashMap<UUID, PlayerData> playerData = new HashMap<>();

    public static boolean existUser(User user) {
        return SomSQL.existSql(Table, "Discord", user.getId());
    }

    public static boolean existsName(String name) {
        return SomSQL.existSql(Table, "Username", name);
    }

    public static PlayerData fromPlayer(Player player) {
        if (!playerData.containsKey(player.getUniqueId())) {
            playerData.put(player.getUniqueId(), new PlayerData(player));
        }
        PlayerData data = playerData.get(player.getUniqueId());
        data.player = player;
        return data;
    }

    public static PlayerData fromUUID(UUID uuid) {
        if (!playerData.containsKey(uuid)) {
            playerData.put(uuid, new PlayerData(uuid));
        }
        PlayerData data = playerData.get(uuid);
        data.player = SomCore.getProxy().getPlayer(uuid).orElse(null);
        return playerData.get(uuid);
    }

    public static PlayerData fromName(String name) {
        String uuid = SomSQL.getString(Table, "Username", name, "UUID");
        return fromUUID(UUID.fromString(uuid));
    }

    public static PlayerData fromUser(User user) {
        String uuid = SomSQL.getString(Table, "Discord", user.getId(), "UUID");
        return fromUUID(UUID.fromString(uuid));
    }

    private final UUID uuid;
    private Player player;
    private String displayName;
    private String tabListName;
    private long discord;
    private LocalDateTime lastOnline = LocalDateTime.now();
    private SomSender sender;
    private PlayerData teller = null;

    public PlayerData(Player player) {
        uuid = player.getUniqueId();
        this.player = player;
        SomSQL.setSql(Table, "UUID", uuid.toString(), "Username", player.getUsername());
        setDisplayName(player.getUsername());
        discord = getDiscord();
        sender = new SomSender(player);
        tabListName = player.getUsername();
    }

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        displayName = getDisplayName();
        tabListName = displayName;
        discord = getDiscord();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return player.getUsername();
    }

    public SomSender getSender() {
        return sender;
    }

    public String getTabListName() {
        return tabListName;
    }

    public void setTabListName(String tabListName) {
        this.tabListName = tabListName;
    }

    public boolean isOnline() {
        return player != null;
    }

    public boolean isBE() {
        return getName().indexOf(0) == '.';
    }

    public void updateDisplayName() {
        SomSQL.setSql(Table, "UUID", uuid.toString(), "DisplayName", player.getUsername());
    }

    public void playSound(Sound sound) {
        if (isOnline()) {
            player.playSound(sound);
        }
    }

    public void sendMessage(String message) {
        if (isOnline()) {
            player.sendMessage(Component.text(message));
        }
    }

    public void sendSomText(SomText somText) {
        if (isOnline()) {
            player.sendMessage(somText.toComponent());
        }
    }

    public void sendSomText(Collection<SomText> somText) {
        if (isOnline()) {
            for (SomText text : somText) {
                player.sendMessage(text.toComponent());
            }
        }
    }

    public String getUsername() {
        return SomSQL.getString(Table, "UUID", uuid.toString(), "Username");
    }

    public long getDiscord() {
        return SomSQL.getLong(Table, "UUID", uuid.toString(), "Discord");
    }

    public void setDiscord(long id) {
        discord = id;
        SomSQL.setSql(Table, "UUID", uuid.toString(), "Discord", id);
    }

    public String getDisplayName() {
        return SomSQL.getString(Table, "UUID", uuid.toString(), "DisplayName");
    }

    public void setDisplayName(String name) {
        displayName = name;
        SomSQL.setSql(Table, "UUID", uuid.toString(), "DisplayName", name);
    }

    public LocalDateTime getLastOnline() {
        if (isOnline()) return LocalDateTime.now();
        return LocalDateTime.parse(SomSQL.getString(Table, "UUID", uuid.toString(), "LastOnline"), DateFormat);
    }

    public void setLastOnline() {
        SomSQL.setSql(Table, "UUID", uuid.toString(), "LastOnline", LocalDateTime.now().format(DateFormat));
    }

    public boolean hasChatRoom() {
        return getChatRoom() != null;
    }

    public String getChatRoom() {
        return SomSQL.getString(Table, "UUID", uuid.toString(), "ChatRoom");
    }

    public void setChatRoom(String chatRoom) {
        SomSQL.setSql(Table, "UUID", uuid.toString(), "ChatRoom", chatRoom);
    }

    public boolean isChatInverted() {
        return Boolean.parseBoolean(SomSQL.getString(Table, "UUID", uuid.toString(), "ChatInverted"));
    }

    public void setChatInverted(boolean chatInverted) {
        SomSQL.setSql(Table, "UUID", uuid.toString(), "ChatInverted", String.valueOf(chatInverted));
    }

    public String getSpecialAlert() {
        return SomSQL.getString(Table, "UUID", uuid.toString(), "SpecialAlert");
    }

    public void setSpecialAlert(String specialAlert) {
        SomSQL.setSql(Table, "UUID", uuid.toString(), "SpecialAlert", specialAlert);
        sendMessage(specialAlert);
        SomCore.getProxy().getServer("lobby").ifPresent(server -> player.createConnectionRequest(server).connect());
    }

    public void resetAlert(String message) {
        SomSQL.setSql(Table, "UUID", uuid.toString(), "SpecialAlert", null);
        sendMessage(message);
    }

    public void setProxyMute(int minute) {
        String date = LocalDateTime.now().plusMinutes(minute).format(DateFormat);
        SomSQL.setSql(Table, "UUID", uuid.toString(), "ProxyMute", date);
        SomCore.broadcast(getDisplayName() + "§aが§cProxyMute§aされました\n§e" + date + "§aまで§eチャット機能§aを利用できません");
    }

    public boolean isProxyMute() {
        String dateText = SomSQL.getString(Table, "UUID", uuid.toString(), "ProxyMute");
        if (dateText != null) {
            LocalDateTime date = LocalDateTime.parse(dateText, DateFormat);
            return LocalDateTime.now().isBefore(date);
        } else {
            return false;
        }
    }

    public boolean hasPartner() {
        return teller != null;
    }

    public PlayerData getTeller() {
        return teller;
    }

    public void setTeller(PlayerData teller) {
        this.teller = teller;
    }

    public RegisteredServer getServer() {
        if (isOnline() && player.getCurrentServer().isPresent()) {
            return player.getCurrentServer().get().getServer();
        }
        return null;
    }

    public String getServerText() {
        if (isOnline() && player.getCurrentServer().isPresent()) {
            return player.getCurrentServer().get().getServer().getServerInfo().getName();
        }
        return "§cオフライン";
    }

    public User getUser() {
        return DiscordBot.getUser(discord);
    }

    public Member getMember() {
        return DiscordBot.getGuild().retrieveMemberById(discord).complete();
    }

    public boolean isLink() {
        return getDiscord() != 0;
    }

    public SomText getDisplayComponent() {
        SomText hover = SomText.create().addDeco(displayName).newLine();
        hover.add("§7・§9Discord§7: §a" + (isLink() ? getUser().getName() : "§c未リンク")).newLine();
        hover.add("§7・§eServer§7: §a" + getServerText()).newLine();
        hover.add("§7・§eLastOnline§7: §a" + getLastOnline().format(DateFormat));
        String color;
        if (!isOnline() && isLink()) {
            color = "§9";
        } else if (isLink()) {
            color = "§a";
        } else {
            color = "§7";
        }

        return SomText.create().addHover(color + "★§r" + displayName, hover);
    }

    public SomText chat(String message, String rawMessage) {
        return getDisplayComponent().add("§a:§r ").addHover(message, rawMessage);
    }

    public SomText chat(Component component) {
        return getDisplayComponent().add("§a:§r ").add(component);
    }

    public static SomText chatRaw(String message, String rawMessage) {
        return SomText.create().addHover(message, rawMessage);
    }
}
