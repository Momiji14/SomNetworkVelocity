package SomNetworkVelocity;

import SomNetworkVelocity.Command.Player.UpdateNick;
import com.velocitypowered.api.proxy.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static SomNetworkVelocity.SomCore.Log;

public class DiscordBot extends ListenerAdapter {

    private static long chatChannelId;
    private static TextChannel chatChannel;
    private static String webhook;
    private static JDA jda;
    private static Guild guild;
    public static Role LinkRole;
    public static void connect() {
        try {
            String discordBotToken = "OTY3NTk0ODc4ODY1Mzc1MzAy.Gn1DQh._xWZtIxv7LhnT2z0gVaLs2tks7VgIaQW7dU1HE";
            long discordServerId = 520366411999346698L;
            long discordChatChannel = 520366412003540993L;
            String discordChatWebhook = "https://discord.com/api/webhooks/967720248293875762/Xrypxz7X5tfEuotjcDuSPpnT-CdgO0l5FXzmcIbNR_sVaELfgboSbSBymS6Bp6xIcFTi";

            jda = JDABuilder.createDefault(discordBotToken)
                    .enableIntents(Arrays.asList(GatewayIntent.values()))
                    .disableCache(Arrays.asList(CacheFlag.values()))
                    .setRawEventsEnabled(true)
                    .addEventListeners(new DiscordBot())
                    .setActivity(Activity.streaming("Sword of Magic Network", "mc.somrpg.net"))
                    .build();
            jda.awaitReady();

            guild = jda.getGuildById(discordServerId);
            chatChannelId = discordChatChannel;
            chatChannel = guild.getTextChannelById(chatChannelId);
            webhook = discordChatWebhook;
            LinkRole = guild.getRoleById(1033424878709256262L);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("DiscordBot Start");
            embed.setColor(Color.CYAN);
            chatChannel.sendMessageEmbeds(embed.build()).queue();
            Log("§bDiscordBotが起動しました");
        } catch (InterruptedException e) {
            Log("§cDiscordBotの起動処理中にエラーが発生しました");
            throw new RuntimeException(e);
        }
    }

    public static void commandUpdate() {
        jda.updateCommands().queue();
        jda.upsertCommand("ping", "Pingを計ります").queue();
        jda.upsertCommand("link", "DiscordとMinecraftをリンクします")
                .addOptions(new OptionData(OptionType.STRING, "mcid", "MinecraftのIDです").setRequired(true))
                .queue();
        jda.upsertCommand("shutdown", "DiscordBotを停止します").setDefaultPermissions(DefaultMemberPermissions.DISABLED).queue();
    }

    public static boolean isOnline() {
        return jda != null;
    }

    public static Guild getGuild() {
        return guild;
    }

    public static User getUser(long id) {
        return jda.retrieveUserById(id).complete();
    }

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        Log("trigger:" + event.getUser().getName());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try {
            User user = event.getAuthor();
            if (!user.isBot()) {
                MessageChannel channel = event.getChannel();
                Message discordMessage = event.getMessage();

                if (chatChannelId == channel.getIdLong()) {
                    if (SomSQL.existSql(PlayerData.Table, "Discord", user.getId())) {
                        PlayerData playerData = PlayerData.fromUser(user);
                        String message = discordMessage.getContentDisplay();
                        SomCore.broadcast(playerData.chat(message, message));
                    } else {
                        MessageCreateAction reply = discordMessage.reply(
                                user.getAsMention() + "DiscordとMinecraftがリンクされていないためサーバーチャットを使用できません\nDiscordから`/link <mcid>`を実行してMinecraft側で認証してください"
                        );
                        discordMessage.delete().queueAfter(5, TimeUnit.SECONDS);
                        reply.complete().delete().queueAfter(5, TimeUnit.SECONDS);
                    }
                }
            }
        } catch (Exception e) {
            Log(e);
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        List<OptionMapping> options = event.getOptions();
        String[] args = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            args[i] = options.get(i).getAsString();
        }
        switch (event.getName()) {
            case "ping" -> {
                long time = System.currentTimeMillis();
                event.reply("Pong!").setEphemeral(true)
                        .flatMap(v -> event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)).queue();
            }
            case "link" -> {
                if (PlayerData.existsName(args[0])) {
                    PlayerData playerData = PlayerData.fromName(args[0]);
                    if (!playerData.isLink()) {
                        requestLink(user, playerData);
                        event.reply("Minecraftで/linkを実行してください").setEphemeral(true).queue();
                    } else {
                        event.reply("すでにリンクしています").setEphemeral(true).queue();
                    }
                } else {
                    event.reply("存在しないまたはログインしていないプレイヤーです").setEphemeral(true).queue();
                }
            }
            case "shutdown" -> {
                if (user.getIdLong() == 330949002780606475L) {
                    event.reply("DiscordBotを停止します").setEphemeral(true).queue();
                    shutdown();
                } else event.reply("権限がありません").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        try {
            /*EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("DiscordBot Stop");
            embed.setColor(Color.RED);
            chatChannel.sendMessageEmbeds(embed.build()).queue();*/
            Log("§cDiscordBotが停止しました");
        } catch (Exception e) {
            Log("§cDiscordBotの停止処理中にエラーが発生しました");
            e.printStackTrace();
        }
    }

    public static void receiveChat(PlayerData playerData, String message) {
        SomTask.run(() -> {
            try {
                String url;
                if (playerData.isLink()) {
                    url = playerData.getUser().getAvatarUrl();
                } else if (playerData.isBE()) {
                    url = "https://pbs.twimg.com/profile_images/919989928326336515/HyjLVdtR_400x400.jpg";
                } else {
                    url = "http://tt0.link/minecraft/others/skinget/helm.php?ID=" + playerData.getName();
                }

                DiscordWebhook discordWebhook = new DiscordWebhook(webhook);
                discordWebhook.setUsername(playerData.getDisplayName());
                discordWebhook.setAvatarUrl(url);
                discordWebhook.setContent(message);
                discordWebhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void sendMessage(String name, String message) {
        SomTask.run(() -> {
            try {
                DiscordWebhook discordWebhook = new DiscordWebhook(webhook);
                discordWebhook.setUsername(name);
                discordWebhook.setContent(message);
                discordWebhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static JDA getJDA() {
        return jda;
    }

    public static void shutdown() {
        jda.shutdown();
    }

    private static final HashMap<Player, User> requestLink = new HashMap<>();
    public static void requestLink(User user, PlayerData playerData) {
        requestLink.put(playerData.getPlayer(), user);
        playerData.sendMessage(user.getName() + "§aと§f" + playerData.getDisplayName() + "§aを§eリンク§aしますか？ §e/link");
    }
    public static void link(User user, PlayerData playerData) {
        playerData.setDiscord(user.getIdLong());
        playerData.sendMessage(user.getName() + "§aと§f" + playerData.getDisplayName() + "§aを§eリンク§aしました");
        DiscordBot.getGuild().addRoleToMember(playerData.getUser(), LinkRole).queue();
        UpdateNick.set(playerData);
    }

    public static HashMap<Player, User> getRequestLink() {
        return requestLink;
    }
}
