package main;

import main.events.PlayerInteract;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Comparator;

public class Vote extends JavaPlugin {

    class Data extends Object {
        Data() {
        }

        Data(String name, Integer point) {
            this.name = name;
            this.point = point;
        }

        public Integer point;
        public String name;
    }

    public static Plugin plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveConfig();
        config = this.getConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList list = new ArrayList();
        for (String s : config.getConfigurationSection("Building").getKeys(false)) {
            String name = s;
            Integer point = config.getInt("Building." + s);
            Data d = new Data(name, point);
            list.add(d);
        }

        list.sort(new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return o2.point - o1.point;
            }
        });

        int i = 0;
        while (i < 5 && i < list.size()) {
            Data d = (Data) list.get(i);
            String name = d.name;
            for (int j = 0; j < 15 - d.name.length(); j++) {
                name += " ";
            }
            sender.sendMessage(name + d.point);
            i++;
        }
        return true;
    }
}
