package SomNetworkVelocity;

import SomNetworkVelocity.Command.Player.ChatRoom;
import com.github.jasync.sql.db.RowData;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.ConnectionHandshakeEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.util.UUID;

import static SomNetworkVelocity.SomCore.*;

public class Events {

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        try {
            if (event.getIdentifier().getId().equalsIgnoreCase(SNCChannel)) {
                ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
                String dataType = in.readUTF();
                switch (dataType) {
                    case "TabListUpdate" -> {
                        Player player = SomCore.getProxy().getPlayer(UUID.fromString(in.readUTF())).orElseThrow();
                        PlayerData.fromPlayer(player).setTabListName(in.readUTF());
                        //SomTabList.update(player, false);
                    }
                    case "Chat" -> {
                        Player player = SomCore.getProxy().getPlayer(in.readUTF()).orElseThrow();
                        PlayerData playerData = PlayerData.fromPlayer(player);
                        if (playerData.isProxyMute()) {
                            playerData.sendMessage("あなたは§cProxyMute§aされています");
                            return;
                        }
                        String rawMessage = in.readUTF();
                        boolean isChatRoom = playerData.hasChatRoom();
                        if (rawMessage.charAt(0) == '!') {
                            isChatRoom = false;
                            rawMessage = rawMessage.replaceFirst("!", "");
                        }
                        if (playerData.isChatInverted()) {
                            isChatRoom = !isChatRoom;
                        }
                        String message = SomTranslator.translation(rawMessage);
                        SomText somText = playerData.chat(message, rawMessage);
                        if (isChatRoom) {
                            String chatRoom =  playerData.getChatRoom();
                            ChatRoom.chatRoom(chatRoom, somText);
                            MessageChannel channel = DiscordBot.getGuild().getChannelById(MessageChannel.class, 1137049263604510842L);
                            channel.sendMessage("```[" + chatRoom + "] " + playerData.getUsername() + ": " + message + "```").queue();
                        } else {
                            SomCore.broadcast(somText);
                            DiscordBot.receiveChat(playerData, message);
                        }
                    }
                    case "Component" -> {
                        Player player = SomCore.getProxy().getPlayer(in.readUTF()).orElseThrow();
                        PlayerData playerData = PlayerData.fromPlayer(player);
                        SomCore.broadcast(playerData.chat(JSONComponentSerializer.json().deserialize(in.readUTF())));
                    }
                    case "GlobalComponent" -> SomCore.broadcast(JSONComponentSerializer.json().deserialize(in.readUTF()));
                }
            }
        } catch (Exception e) {
            Log(e);
        }
    }

    @Subscribe
    public void onConnect(LoginEvent event) {
        Player player = event.getPlayer();
        String ip = player.getRemoteAddress().getAddress().getHostAddress();
        boolean ignore = false;
        for (RowData objects : SomSQL.getSqlList("ignoreIP", "UUID")) {
            if (player.getUniqueId().toString().equals(objects.getString("UUID"))) {
                //player.sendMessage(Component.text("§eIP例外アカウント§aを§b承認§aしました"));
                ignore = true;
            }
        }
        if (!ignore) {
            if (SomSQL.existSql("ips", "IP", ip)) {
                String uuid = SomSQL.getString("ips", "IP", ip, "UUID");
                if (!uuid.equals(player.getUniqueId().toString())) {
                    String username = SomSQL.getString("ips", "IP", ip, "Username");
                    event.setResult(ResultedEvent.ComponentResult.denied(Component.text(
                                    "§aあなたの§eアドレス(" + ip + ")§aは§f" + username + "§aに§e使用§aされています\n" +
                                    "§a心当たりがない場合は§9Discord§aの§cアカウントロック解除申請§aへ§b申請§aしてください\n" +
                                    "§a兄弟でプレイしているなど必然的に§c同IP§aから接続する場合§e同IP例外申請§aへ§b申請§aしてください\n" +
                                    "§eMCID/UUID§7: §a" + player.getUsername() + " / " + player.getUniqueId() + "\n" +
                                    "§9Discord§7: §ahttps://discord.gg/YSnGhhG"
                    )));
                    return;
                }
            }
            for (Player otherPlayer : getProxy().getAllPlayers()) {
                if (otherPlayer.getRemoteAddress().getAddress().getHostAddress().equals(ip)) {
                    event.setResult(ResultedEvent.ComponentResult.denied(Component.text("同IPからの接続は禁止されています")));
                    return;
                }
            }
        }
        SomSQL.delete("ips", "UUID", player.getUniqueId().toString());
        SomSQL.setSql("ips", "IP", ip, "UUID", player.getUniqueId().toString());
        SomSQL.setSql("ips", "IP", ip, "Username", player.getUsername());
        //SomTabList.joinPlayer(player);
        SomCore.broadcast(PlayerData.fromPlayer(event.getPlayer()).getDisplayComponent().add("§aが§dSomNetwork§aに§bログイン§aしました"));
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.fromPlayer(player);
        playerData.setLastOnline();
        //SomTabList.quitPlayer(player);
        //SomCore.broadcast(event.getPlayer().getUsername() + "§aが§dSomNetwork§aから§cログアウト§aしました");
    }
}
