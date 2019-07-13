package main.events;

import main.Vote;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Date;

public class PlayerInteract implements Listener {
    private FileConfiguration config = Vote.config;

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign) e.getClickedBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase("[Vote]")) {
                String configName = "Building." + sign.getLine(1);
                if (config.get(configName) == null)
                    config.set(configName, 0);
                else {
                    if (new Date().getTime() / 1000 - config.getInt(e.getPlayer().getName()) > 86400) {
                        config.set(configName, config.getInt(configName) + 1);
                        config.set(e.getPlayer().getName(),new Date().getTime()/1000);
                        e.getPlayer().sendMessage(ChatColor.BLUE + "你成功投票給了 " + sign.getLine(1));
                    }else{
                        e.getPlayer().sendMessage(ChatColor.BLUE + "投票冷卻中");
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

    @EventHandler
    public void PlayerBreakBlock(BlockBreakEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();

        if(! (block.getState() instanceof Sign)) return;

        Sign sign = (Sign) block.getState();
        if(sign.getLine(0).equalsIgnoreCase("[Vote]")){
            if(!player.hasPermission("setVoteSign")){
                e.setCancelled(true);
            }
        }
    }
}
