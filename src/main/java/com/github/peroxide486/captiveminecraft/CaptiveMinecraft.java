package com.github.peroxide486.captiveminecraft;

import org.bukkit.plugin.java.JavaPlugin;

public class CaptiveMinecraft extends JavaPlugin {
    CaptiveMinecraft plugin;

    @Override
    public void onEnable() {
        this.plugin = this;
        getLogger().info("CaptiveMinecraft Enabled...");
    }
}
