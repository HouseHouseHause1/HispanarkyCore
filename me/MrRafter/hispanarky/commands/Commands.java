package me.MrRafter.hispanarky.commands;

import me.MrRafter.hispanarky.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
/*    */ 
/*    */ public class Commands
/*    */   implements CommandExecutor {
/*    */   private Main plugin;
/*    */   
/*    */   public Commands(Main instance) {
/* 14 */     this.plugin = instance;
/*    */   }
/*    */   
/* 17 */   public static String PREFIX = "&b&l[HispanarkyCore] ";
/*    */
/*    */   
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
/* 21 */     if (!cmd.getName().equalsIgnoreCase("HispanarkyCore"))
/* 22 */       return true; 
/* 23 */     if (args.length == 0) {
/* 24 */       sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(PREFIX) + "&cUsage: &l/hc reload"));
/* 25 */       return true;
/*    */     } 
/* 27 */     if (args[0].equalsIgnoreCase("reload")) {
/* 28 */       if (!sender.hasPermission("hc.reload")) {
/* 29 */         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
/* 30 */               String.valueOf(PREFIX) + "&cYou don't have permissions to run this command."));
/* 31 */         return true;
/*    */       } 
/* 33 */       this.plugin.reloadConfig();
/* 34 */       sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(PREFIX) + "&aConfig reloaded!"));
/* 35 */       return true;
/*    */     } 
/* 37 */     return false;
/*    */   }
/*    */ }