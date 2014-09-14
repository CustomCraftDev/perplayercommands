package perplayercommand;

import java.io.File;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
public class Perplayercommand extends JavaPlugin implements Listener{
	
    FileConfiguration config;
    boolean debug;
    
    String cmd;
    String defaultmsg;
	String nopermission_msg;
	ChatColor debug_color;
	boolean isplayer;

	
	/**
     * on Plugin enable
     */
	public void onEnable() {
		loadConfig();
    	say("Config loaded");   	
    	
    	this.getServer().getPluginManager().registerEvents(this, this);
    	say("Events registered");
	}

	
	/**
     * on Plugin disable
     */
	public void onDisable() {
		reloadConfig();
		saveConfig();
	}

	
	/**
     * on Command
     * @param sender - command sender
     * @param cmd - command
     * @param alias
     * @return true or false
     */
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		isplayer = false;
		Player p = null;
		
		if ((sender instanceof Player)) {
			p = (Player)sender;
			isplayer = true;
		}
			if(cmd.getName().equalsIgnoreCase("ppc") && args.length != 0){
								
				// disable
				if(args[0].equalsIgnoreCase("disable")){
					if(isplayer){
						if(p.hasPermission("ppc.disable")){
							this.setEnabled(false);
							p.sendMessage(debug_color + "[PerPlayerCommands] was disabled");
							say("disabled by " + p.getName());
						return true;
					}
						else{
							p.sendMessage(nopermission_msg);
							return true;
						}
					}
					else{
							this.setEnabled(false);
						System.out.println("[PerPlayerCommands] was disabled");
						return true;
					}
				}
				
				// reset
				if(args[0].equalsIgnoreCase("reset")){
					if(isplayer){
						if(p.hasPermission("ppc.reset")){
						    File configFile = new File(getDataFolder(), "config.yml");
						    configFile.delete();
						    saveDefaultConfig();
							p.sendMessage(debug_color + "[PerPlayerCommands] config reset");
						    reload();
							p.sendMessage(debug_color + "[PerPlayerCommands] was reloaded");
							say("reset by " + p.getName());
						return true;
						}
						else{
							p.sendMessage(nopermission_msg);
							return true;
						}
					}
					else{
					    File configFile = new File(getDataFolder(), "config.yml");
					    configFile.delete();
					    saveDefaultConfig();
					    System.out.println(debug_color + "[PerPlayerCommands] config reset");
					    reload();
					    System.out.println(debug_color + "[PerPlayerCommands] was reloaded");
					    return true;
					}
				}
				
				// reload
				if(args[0].equalsIgnoreCase("reload")){
					if(isplayer){
						if(p.hasPermission("ppc.reload")){
							reload();
							p.sendMessage(debug_color + "[PerPlayerCommands] was reloaded");
							say("reloaded by " + p.getName());
						return true;
					}
						else{
							p.sendMessage(nopermission_msg);
							return true;
						}
					}
					else{
						    reload();
					    System.out.println("[PerPlayerCommands] was reloaded");
						return true;
				    }
				}
			}
		
		// nothing to do here \o/
		return false;
	}
	
	
	
    @EventHandler
    public void onPreprocess(PlayerJoinEvent e) {
    	Player p = e.getPlayer();
    	if(!config.contains("players." + p.getName())){
    		config.set("players." + p.getName(), defaultmsg);
	    		saveConfig();
	    			reloadConfig();
    		say(p.getName() + " was added to ppc config");
    	}
    }
	
    
    @EventHandler
    public void onPreprocess(PlayerCommandPreprocessEvent e) {
    	Player p = e.getPlayer();
            if (e.getMessage().equalsIgnoreCase("/" + cmd)) {
            		if(p.hasPermission("ppc.use")){
            			String message = config.getString("players." + p.getName());
            			if(!message.startsWith("%no%")){
            				if(message.contains("%nextline%")){
            					String[] messagelist = message.split("%nextline%");
            						for(int i = 0; i < messagelist.length; i++){
        	            				String message2 = "";
        	            				message2 = messagelist[i].replace("%playername%", p.getName());
        	            				message2 = message2.replace("%world%", p.getWorld().getName());
        	            				message2 = ChatColor.translateAlternateColorCodes('&', message2);
        	                			p.sendMessage(message2);
            						}
            				}
            				else{
	            				String message2 = "";
	            				message2 = message.replace("%playername%", p.getName());
	            				message2 = message2.replace("%world%", p.getWorld().getName());
	            				message2 = ChatColor.translateAlternateColorCodes('&', message2);
	                			p.sendMessage(message2);
            				}
            			}
            		}
            		else{
            			 p.sendMessage(nopermission_msg);
            		}
        		e.setCancelled(true);
            }
            else{
            	// this is not the command you are looking for
            	e.setCancelled(false);
            }            
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
		defaultmsg = ChatColor.translateAlternateColorCodes('&', config.getString("default-msg"));
		nopermission_msg = ChatColor.translateAlternateColorCodes('&', config.getString("nopermission-msg"));
						
		debug_color = ChatColor.valueOf(config.getString("debug-color"));
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
			    nopermission_msg = null;
			    debug_color = null;
			    
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
