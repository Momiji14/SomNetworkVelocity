package SomNetworkVelocity.Command.Player;

import SomNetworkVelocity.*;
import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import com.github.jasync.sql.db.RowData;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static SomNetworkVelocity.Function.decoLore;
import static SomNetworkVelocity.Function.decoText;
import static SomNetworkVelocity.SomCore.getProxy;

public class ChatRoom extends SomCommand {
    public ChatRoom() {
        super("chatRoom");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        if (args.length >= 1) {
            String chatRoom = args[0];
            if (args[0].equalsIgnoreCase("Global")) {
                if (playerData.hasChatRoom()) {
                    chatRoom(playerData.getChatRoom(), SomText.create(playerData.getDisplayName() + "§aが§c退出§aしました"));
                    playerData.setChatRoom(null);
                    playerData.sendMessage("§6[ChatRoom]§eチャットルーム§aを§b解除§aしました");
                } else {
                    playerData.sendMessage("§6[ChatRoom]§eチャットルーム§aに§b参加§aしていません");
                }
            } else if (args[0].equalsIgnoreCase("member")) {
                if (playerData.hasChatRoom()) {
                    SomTask.run(() -> {
                        List<SomText> somText = new ArrayList<>();
                        somText.add(SomText.create(decoText(playerData.getChatRoom())));
                        for (PlayerData member : chatRoomMember(playerData.getChatRoom())) {
                            somText.add(SomText.create("§7・§r").add(member.getDisplayComponent()));
                        }
                        playerData.sendSomText(somText);
                    });
                } else {
                    playerData.sendMessage("§6[ChatRoom]§eチャットルーム§aに§b参加§aしていません");
                }
            } else if (args[0].equalsIgnoreCase("inverted")) {
                playerData.setChatInverted(!playerData.isChatInverted());
                playerData.sendMessage("§6[ChatRoom]§eデフォルト§aの§eチャット送信先§aを§e" + (playerData.isChatInverted() ? "グローバル" : "チャットルーム") + "§aにしました");
            } else if (3 <= chatRoom.length() && chatRoom.length() <= 8) {
                if (playerData.hasChatRoom()) {
                    chatRoom(playerData.getChatRoom(), SomText.create(playerData.getDisplayName() + "§aが§c退出§aしました"));
                }
                playerData.setChatRoom(chatRoom);
                chatRoom(chatRoom, SomText.create(playerData.getDisplayName() + "§aが§b参加§aしました"));
                playerData.sendMessage("§6[ChatRoom]§eチャットルーム§aを§e[" + chatRoom + "]§aに変更しました");
            } else {
                playerData.sendMessage("§6[ChatRoom]§eチャットルーム名§aは§e3~8文字§aです");
            }
        } else {
            playerData.sendMessage("§e/chatRoom <チャットルーム名(3~8文字)>");
            playerData.sendMessage("§e/chatRoom inverted");
            playerData.sendMessage("§e/chatRoom member");
            playerData.sendMessage("§e/chatRoom global");
        }
        return true;
    }

    @Override
    public void command(SomSender sender, String[] args) {

    }

    @Override
    public List<String> tabComplete(String[] args) {
        List<String> complete = new ArrayList<>();
        if (args.length == 1) {
            complete.add("global");
            complete.add("member");
            complete.add("inverted");
        }
        return complete;
    }

    public static void chatRoom(String chatRoom, SomText somText) {
        somText = SomText.create("§6[" + chatRoom + "]").add(somText);
        for (RegisteredServer server : getProxy().getAllServers()) {
            for (Player otherPlayer : server.getPlayersConnected()) {
                PlayerData otherData = PlayerData.fromPlayer(otherPlayer);
                if (chatRoom.equals(otherData.getChatRoom())) {
                    otherData.sendSomText(somText);
                }
            }
        }
    }

    public static List<PlayerData> chatRoomMember(String chatRoom) {
        List<PlayerData> list = new ArrayList<>();
        for (RowData objects : SomSQL.getSqlList(PlayerData.Table, "ChatRoom", chatRoom, "UUID")) {
            list.add(PlayerData.fromUUID(UUID.fromString(objects.getString("UUID"))));
        }
        return list;
    }
}
