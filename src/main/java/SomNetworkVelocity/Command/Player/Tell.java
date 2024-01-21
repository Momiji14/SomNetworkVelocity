package SomNetworkVelocity.Command.Player;

import SomNetworkVelocity.*;
import SomNetworkVelocity.Command.SomCommand;
import SomNetworkVelocity.Command.SomSender;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

import java.util.List;

public class Tell extends SomCommand {
    public Tell() {
        super("tell", "message", "msg", "dm", "w");
    }

    @Override
    public boolean commandPlayer(PlayerData playerData, String[] args) {
        if (playerData.isProxyMute()) {
            playerData.sendMessage("あなたは§cProxyMute§aされています");
            return true;
        }
        if (args.length >= 2) {
            SomCore.getProxy().getPlayer(args[0]).ifPresentOrElse(
                player -> {
                    PlayerData targetData = PlayerData.fromPlayer(player);
                    StringBuilder rawMessage = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        rawMessage.append(args[i]).append(" ");
                    }
                    directMessage(playerData, targetData, rawMessage.toString());
                    playerData.setTeller(targetData);
                    targetData.setTeller(playerData);
                },
                () -> playerData.sendMessage("存在しないPlayerです"));
        } else {
            playerData.sendMessage("§e/tell <player> <message>");
        }
        return true;
    }

    @Override
    public void command(SomSender sender, String[] args) {

    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) return completeAllPlayers();
        return null;
    }

    public static void TellLog(PlayerData sender, PlayerData receiver, String message) {
        String log = sender.getDisplayName() + " -> " + receiver.getDisplayName() + ": " + message;
        MessageChannel channel = DiscordBot.getGuild().getChannelById(MessageChannel.class, 1136784943146606675L);
        channel.sendMessage("```" + log + "```").queue();
    }

    public static void directMessage(PlayerData playerData, PlayerData targetData, String rawMessage) {
        String message = SomTranslator.translation(rawMessage);
        playerData.sendSomText(SomText.create("§d[DM]").add(playerData.chat(message, rawMessage)).add(" §7[" + targetData.getName() + "]"));
        targetData.sendSomText(SomText.create("§d[DM]").add(playerData.chat(message, rawMessage)));
        targetData.playSound(Sound.sound(Key.key("minecraft:block.lever.click"), Sound.Source.PLAYER, 1f, 1f));
        TellLog(playerData, targetData, message);
    }
}
