package com.github.peroxide486.captiveminecraft.commands;

import com.github.peroxide486.captiveminecraft.utils.RegionManager;
import com.github.peroxide486.captiveminecraft.utils.Regions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BorderChangeCommand implements TabExecutor {
    private final RegionManager regionManager;

    public BorderChangeCommand(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args[0].equalsIgnoreCase("size")) {
                if (args.length != 2) {
                    player.sendMessage(Component.text("Usage: /regionborder size <size>", NamedTextColor.RED));
                    return true;
                }
                double newSize;
                try {
                    newSize = Double.parseDouble(args[1]);
                    if (newSize <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(Component.text("The Number must be a positive integer", NamedTextColor.RED));
                    return true;
                }
                regionManager.resizeRegion(player, newSize);
                return true;
            }

            return true;
        } else {
            sender.sendMessage(Component.text("Only players can run this command!", NamedTextColor.RED));
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String input = args[0].toLowerCase();
                List<String> completions = new ArrayList<>();
                if ("size".startsWith(input)) {
                    completions.add("size");
                }
                return completions;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("size")) {
                Regions region = regionManager.getPlayerRegion(player);
                if (region != null) {
                    double currentSize = region.getRegionSize();
                    return Collections.singletonList(String.valueOf(currentSize));
                } else {
                    return Collections.singletonList("100");
                }
            }
        } else {
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }
}
