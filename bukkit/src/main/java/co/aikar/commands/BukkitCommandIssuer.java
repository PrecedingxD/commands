/*
 * Copyright (c) 2016-2017 Daniel Ennis (Aikar) - MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package co.aikar.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public class BukkitCommandIssuer implements CommandIssuer {
    private final BukkitCommandManager manager;
    private final CommandSender sender;

    BukkitCommandIssuer(BukkitCommandManager manager, CommandSender sender) {
        this.manager = manager;
        this.sender = sender;
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    @Override
    public CommandSender getIssuer() {
        return sender;
    }

    public Player getPlayer() {
        return isPlayer() ? (Player) sender : null;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        if (isPlayer()) {
            return ((Player) sender).getUniqueId();
        }

        //generate a unique id based of the name (like for the console command sender)
        return UUID.nameUUIDFromBytes(sender.getName().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public CommandManager getManager() {
        return manager;
    }

    @Override
    public void sendMessageInternal(String message) {
        //sender.sendMessage(ACFBukkitUtil.color(message));
        Component component = MiniMessage.miniMessage().deserialize(message
                        .replace("&", "§")
                        .replace("§0", "<black>")
                        .replace("§1", "<dark_blue>")
                        .replace("§2", "<dark_green>")
                        .replace("§3", "<dark_aqua>")
                        .replace("§4", "<dark_red>")
                        .replace("§5", "<dark_purple>")
                        .replace("§6", "<gold>")
                        .replace("§7", "<gray>")
                        .replace("§8", "<dark_gray>")
                        .replace("§9", "<blue>")
                        .replace("§a", "<green>")
                        .replace("§b", "<aqua>")
                        .replace("§c", "<red>")
                        .replace("§d", "<light_purple>")
                        .replace("§e", "<yellow>")
                        .replace("§f", "<white>")
                        .replace("§l", "<bold>")
                        .replace("§o", "<italic>")
                        .replace("§k", "<obfuscated>")
                        .replace("§n", "<underlined>")
                        .replace("§m", "<strikethrough>")
                        .replace("§r", "<reset>"))
                .decoration(TextDecoration.ITALIC, message.contains("<italic>"));
        sender.sendMessage(component);
    }

    @Override
    public boolean hasPermission(String name) {
        return sender.hasPermission(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BukkitCommandIssuer that = (BukkitCommandIssuer) o;
        return Objects.equals(sender, that.sender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender);
    }
}
