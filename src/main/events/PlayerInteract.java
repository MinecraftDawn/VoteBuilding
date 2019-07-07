package main.events;

import main.Vote;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Date;

public class PlayerInteract implements Listener {
    private FileConfiguration config = Vote.config;

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (e.getClickedBlock().getState() instanceof Sign) {
            Sign s = (Sign) e.getClickedBlock().getState();
            if (s.getLine(0).equalsIgnoreCase("[Vote]")) {
                String configName = "Building." + s.getLine(1);
                if (config.get(configName) == null)
                    config.set(configName, 0);
                else {
                    if (new Date().getTime() / 1000 - config.getInt(e.getPlayer().getName()) > 86400) {
                        config.set(configName, config.getInt(configName) + 1);
                        config.set(e.getPlayer().getName(),new Date().getTime()/1000);
                    }

                }
                Vote.plugin.saveConfig();
            }
        }
    }

    @EventHandler
    public void PlayerSetSign(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[vote]")) {
            if (!e.getPlayer().hasPermission("setVoteSign")) {
                e.setLine(0, "");
                return;
            }
        }
    }
}
