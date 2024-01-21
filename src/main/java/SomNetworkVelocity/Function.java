package SomNetworkVelocity;

public class Function {
    //アイテム名などに使うテキストデコレーション
    public static String decoText(String str) {
        return decoText(str, 8);
    }
    public static String decoText(String str, int flames) {
        if (str == null) return "null";
        int flame = flames - Math.round(uncolored(str).length() / 1.5f);
        StringBuilder deco = new StringBuilder("===");
        deco.append("=".repeat(Math.max(0, flame)));
        return "§6" + deco + "§r " + colored(str, "§e") + "§r §6" + deco;
    }

    //アイテムのLoreなどに使うテキストデコレーション
    public static String decoLore(String str) {
        return "§7・" + colored(str, "§e") + "§7: §a";
    }

    public static String decoColor(String str) {
        return colored(str, "§e") + "§7: §a";
    }

    //アイテムのLoreの分割部などに使うテキストデコレーション
    public static String decoSeparator(String str) {
        return decoText("§3" + str);
    }
    public static String decoSeparator(String str, int flames) {
        return decoText("§3" + str, flames);
    }
    //カラーを有効化
    public static String colored(String str, String def) {
        if (str.contains("&")) {
            return str.replace("&", "§");
        } else {
            return def.replace("&", "§") + str;
        }
    }

    //カラーを削除
    public static String uncolored(String string) {
        return string
                .replace("§0", "")
                .replace("§1", "")
                .replace("§2", "")
                .replace("§3", "")
                .replace("§4", "")
                .replace("§5", "")
                .replace("§6", "")
                .replace("§7", "")
                .replace("§8", "")
                .replace("§9", "")
                .replace("§a", "")
                .replace("§b", "")
                .replace("§c", "")
                .replace("§d", "")
                .replace("§e", "")
                .replace("§f", "")
                .replace("§l", "")
                .replace("§m", "")
                .replace("§n", "")
                .replace("§k", "")
                .replace("§r", "")
                ;
    }
}
