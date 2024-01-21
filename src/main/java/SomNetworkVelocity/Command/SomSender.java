package SomNetworkVelocity.Command;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;

public record SomSender(CommandSource source) {

    public void sendMessage(String text) {
        source.sendMessage(Component.text(text));
    }
}
