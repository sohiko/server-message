package com.sohiko.server_message;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin {
    private boolean tasukuEnabled = true;
    private boolean taichiEnabled = true;

    @Override
    public void onEnable() {
        getLogger().info("サーバーメッセージプラグインが有効になりました！");
    }

    @Override
    public void onDisable() {
        getLogger().info("サーバーメッセージプラグインが無効になりました");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("server-message")) {
            if (sender instanceof Player && sender.isOp()) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /server-message <message> <color>");
                    return true;
                }

                String fullArgs = String.join(" ", args);
                String message;
                ChatColor color = ChatColor.WHITE;

                if (fullArgs.startsWith("\"") && fullArgs.lastIndexOf("\"") > 0) {
                    int lastQuoteIndex = fullArgs.lastIndexOf("\"");
                    message = fullArgs.substring(1, lastQuoteIndex);
                    if (fullArgs.length() > lastQuoteIndex + 1) {
                        String colorArg = fullArgs.substring(lastQuoteIndex + 2);
                        try {
                            color = ChatColor.valueOf(colorArg.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(ChatColor.RED + "指定された色が無効です");
                            return true;
                        }
                    }
                } else {
                    message = args[0];
                    if (args.length > 1) {
                        try {
                            color = ChatColor.valueOf(args[1].toUpperCase());
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(ChatColor.RED + "指定された色が無効です");
                            return true;
                        }
                    }
                }

                message = message.replace("\\n", "\n");

                String coloredMessage = color + message;
                getServer().broadcastMessage(coloredMessage);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "このコマンドを実行する権限がありません");
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("tasuku")) {
            if (args.length > 0 && (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off"))) {
                if (!sender.hasPermission("servermessage.tasuku.admin")) {
                    sender.sendMessage(ChatColor.RED + "このコマンドを使用する権限がありません。");
                    return true;
                }
                tasukuEnabled = args[0].equalsIgnoreCase("on");
                sender.sendMessage(ChatColor.GREEN + "Tasuku コマンドが" + (tasukuEnabled ? "有効" : "無効") + "になりました。");
                return true;
            }

            if (!tasukuEnabled) {
                sender.sendMessage(ChatColor.RED + "Tasuku コマンドは現在無効です。");
                return true;
            }

            Player tasuku = getServer().getPlayer("Tasu9N");
            if (tasuku != null) {
                tasuku.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 6000, 1));
                getServer().broadcastMessage(ChatColor.YELLOW + "Tasu9Nが発光している！");
            } else {
                sender.sendMessage(ChatColor.RED + "Tasu9Nは現在オフラインです");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("taichi")) {
            if (args.length > 0 && (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off"))) {
                if (!sender.hasPermission("servermessage.taichi.admin")) {
                    sender.sendMessage(ChatColor.RED + "このコマンドを使用する権限がありません。");
                    return true;
                }
                taichiEnabled = args[0].equalsIgnoreCase("on");
                sender.sendMessage(ChatColor.GREEN + "Taichi コマンドが" + (taichiEnabled ? "有効" : "無効") + "になりました。");
                return true;
            }

            if (!taichiEnabled) {
                sender.sendMessage(ChatColor.RED + "Taichi コマンドは現在無効です。");
                return true;
            }

            Player taichi = getServer().getPlayer("KomeNero");
            if (taichi != null) {
                taichi.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 6000, 1));
                getServer().broadcastMessage(ChatColor.YELLOW + "KomeNeroが発光している！");
            } else {
                sender.sendMessage(ChatColor.RED + "KomeNeroは現在オフラインです");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("server-message")) {
            if (args.length == 2) {
                return Arrays.stream(ChatColor.values())
                    .map(Enum::name)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
            }
        }
        return null;
    }
}
