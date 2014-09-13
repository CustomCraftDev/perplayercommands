package perplayercommand;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * @author DieFriiks / CustomCraftDev / undeaD_D
 * @category PerPlayerCommands plugin
 * @version 1.0
 */
public class Perplayercommand extends JavaPlugin {
	
    FileConfiguration config;
    boolean debug;
    
    String cmd;
    String defaultmsg;

	
	/**
     * on Plugin enable
     */
	public void onEnable() {
		loadConfig();
    	say("Config loaded");   	
    	
    	this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
    	say("Eventlistener loaded");
	}

	
	/**
     * on Plugin disable
     */
	public void onDisable() {
		// nothing to save here :(
	}

	/**
     * on Command
     * @param sender - command sender
     * @param cmd - command
     * @param alias
     * @return true or false
     */
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		if ((sender instanceof Player)) {
			Player p = (Player)sender;

			if(cmd.getName().equalsIgnoreCase("ppc") && args.length != 0){
				
				// disable
				if(args[0].equalsIgnoreCase("disable") && p.hasPermission("ppc.disable")){
						this.setEnabled(false);
						p.sendMessage(ChatColor.RED + "[PerPlayerCommands] was disabled");
						say("disabled by " + p.getName());
					return true;
				}
				
				// reset
				if(args[0].equalsIgnoreCase("reset") && p.hasPermission("ppc.reset")){
					    File configFile = new File(getDataFolder(), "config.yml");
					    configFile.delete();
					    saveDefaultConfig();
						p.sendMessage(ChatColor.RED + "[PerPlayerCommands] config reset");
					    reload();
						p.sendMessage(ChatColor.RED + "[PerPlayerCommands] was reloaded");
						say("reset by " + p.getName());
					return true;
				}
				
				// reload
				if(args[0].equalsIgnoreCase("reload") && p.hasPermission("ppc.reload")){
						reload();
						p.sendMessage(ChatColor.RED + "[PerPlayerCommands] was reloaded");
						say("reloaded by " + p.getName());
					return true;
				}
			}
		}
		
		// commands from console
		else{
			System.out.println("[PerPlayerCommands] Command ingame only ...");
			return true;
		}
		
		// nothing to do here \o/
		return false;
	}
	
	
	/**
     * load config settings
     */
	private void loadConfig() {
		
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
		
		debug = config.getBoolean("debug");
		cmd = config.getString("custom-cmd");
		defaultmsg = config.getString("default-msg");
						
	}
	   
    
    /**
     * reload
     */
    private void reload(){
 	   	try {
 	   		// Remove unused variables and objects
			    config = null;
			    cmd = null;
			    defaultmsg = null;

			// Run java garbage collector to delete unused things
			    System.gc();
			
			// load everything again
				reloadConfig();
				loadConfig();
			
 	   	} catch (Exception e) {
        	if(debug){
        		e.printStackTrace();
        	}
        }
    }
    
    
    /**
     * print to console
     * @param message to print
     */
	public void say(String out) {
		if(debug){
			System.out.println("[PerPlayerCommands] " + out);
		}
	}	
}
