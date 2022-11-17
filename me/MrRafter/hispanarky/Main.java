package me.MrRafter.hispanarky;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.MrRafter.hispanarky.commands.CommandCompleter;
import me.MrRafter.hispanarky.commands.Commands;
import me.MrRafter.hispanarky.events.*;
import me.MrRafter.hispanarky.events.vanish.PlayerEvents;
import me.MrRafter.hispanarky.events.vanish.VanishCommand;
import me.MrRafter.hispanarky.events.vanish.VanishPlayer;
import me.MrRafter.hispanarky.misc.Chat;
import me.MrRafter.hispanarky.misc.CommandPreProcess;
import me.MrRafter.hispanarky.patches.*;
import me.MrRafter.hispanarky.patches.NoCom.packetwrapper.WrapperPlayClientBlockDig;
import me.MrRafter.hispanarky.patches.NoCom.packetwrapper.AbstractPacket;
import me.MrRafter.hispanarky.patches.ViolationManager;
import me.MrRafter.hispanarky.patches.antiillegal.events.*;
import me.MrRafter.hispanarky.patches.randomspawn.RandomSpawnManager;
import me.MrRafter.hispanarky.utils.TPS;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public final class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private ProtocolManager protocolManager;
    private final PluginManager PLUGIN_MANAGER = getServer().getPluginManager();

    public static boolean usingPAPI;
    private Logger console;
    public final HashMap<Player, GameMode> gamemodelist = new HashMap<>();
    public final HashMap<Player, Integer> taskidlist = new HashMap<>();
    public final Set<String> connectionMsgs = new HashSet<>();
    public VanishPlayer vanishPlayer;

    private List<Manager> managers;
    private ScheduledExecutorService service;

    private List<ViolationManager> violationManagers;


    public static Main getInstance() {
        return instance;
    }

    public PluginManager pluginManager;
    private final HashMap<UUID, Integer> levernotifier = new HashMap<>();
    public Map<Item, Player> droppers = new HashMap<>();
    private final HashMap<UUID, Integer> leverInteract = new HashMap<>();
    private final HashMap<Chunk, Integer> redstonePerChunk = new HashMap<>();

    private final class FrameDupeListener implements Listener {

        @EventHandler
        private void onFrameBreak(EntityDamageByEntityEvent event) {
            if (event.getEntityType() == EntityType.ITEM_FRAME) {
                int rng = (int)Math.round(Math.random() * 100);
                if (rng < getConfig().getInt("probability-percentage")) {
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), ((ItemFrame) event.getEntity()).getItem());
                }
            }
        }

    }

    private BukkitRunnable runnable;
    private BukkitRunnable notifycleaner;

    @Override
    public void onEnable() {
        //ArmorStand
            PluginManager pluginmanager = this.getServer().getPluginManager();
            pluginmanager.registerEvents(new EntityListener(this), this);
            pluginmanager.registerEvents(new BlockListener(this), this);

        //vanish
        console = getLogger();
        vanishPlayer = new VanishPlayer(this);

        service = Executors.newScheduledThreadPool(4);
        violationManagers = new ArrayList<>();
        instance = this;
        managers = new ArrayList<>();
        saveDefaultConfig();
        registerManagers();
        registerManagers();


        getServer().getPluginManager().registerEvents(new PlayerEvents(this),this);

        PluginCommand vanish = getServer().getPluginCommand("vanish");

        if (vanish != null) {
            vanish.setExecutor(new VanishCommand(this));
            vanish.setTabCompleter(new VanishCommand(this));
        }

        new Metrics(this, 8622);

        //Frame Dupe
        getServer().getPluginManager().registerEvents(new FrameDupeListener(), this);

        //Lava Dupe
        getCommand("hispanarkycore").setExecutor((CommandExecutor)new Commands(this));
        getCommand("hispanarkycore").setTabCompleter((TabCompleter)new CommandCompleter());

        //Drispenser Dupe
        me.MrRafter.hispanarky.misc.Util.load();

        //Load Config
        getLogger().info(ChatColor.GREEN + "Loading Config...");
        saveDefaultConfig();

        //Clean HashMaps
        leverInteract.clear();
        levernotifier.clear();
        redstonePerChunk.clear();

        //Clean HashMaps Every Second
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                leverInteract.clear();
                redstonePerChunk.clear();
            }
        };
        runnable.runTaskTimer(this, 20, 20);

        //Clean LeverNotifier Hashmap
        notifycleaner = new BukkitRunnable() {
            @Override
            public void run() {
                levernotifier.clear();
            }
        };
        notifycleaner.runTaskTimer(this, 600, 600);

        if (instance == null) {
            instance = this;
        }

        //Register Events
        getLogger().info(ChatColor.GREEN + "Registering Events...");
        pluginManager = getServer().getPluginManager();
        register (
                new DupeFixes(this),
                new DisableWithers(this),
                new EndPortalPatch(this),
                new CrashExploits(this),
                new NetherRoof(this),
                new NetherPortals(this),
                new GodMode(this),
                new CoordExploits(this),
                new CommandExploits(this),
                new Burrow(this),
                new BlockPlace(this),
                new DeopOnLeave(this),
                new BookBan(this),
                new AntiBedTrap(this),
                new BowBomb(this),
                new InvalidNames(this),
                new AntiGravity(this),
                new NetherRoof(this),
                new Explosions(this),
                new Chat(this),
                new CommandPreProcess(this),
                new PlayerInteractWithEntityEvent(this),
                new PistonEvent(this),
                new FirstMessage(this),
                new JoinEvent(this),
                new LeaveEvent(this),
                new MotdEvent(this)
        );

        saveDefaultConfig();

        //Load ProtocolLib
        if (getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            getLogger().info(ChatColor.GREEN + "Detected ProtocolLib!");
        } else {
            getLogger().warning("Cannot find ProtocolLib. Please Install ProtocolLib.");
        }
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 100L, 1L);


        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 100L, 1L);

        //Loaded
        getLogger().info(ChatColor.AQUA + "[HispanarkyCore]" + ChatColor.GREEN + " is Loaded and Enabled!");

        //PlaceholderAPI
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            boolean usingPAPI = true;
        }

        //bStats
        int pluginId = 10837;
        me.MrRafter.hispanarky.Metrics metrics = new me.MrRafter.hispanarky.Metrics(this, pluginId);

        saveDefaultConfig();
        //ilegals
        pluginManager.registerEvents(new BlockPlac(), this);
        pluginManager.registerEvents(new ChunkLoad(), this);
        pluginManager.registerEvents(new HopperTansfer(), this);
        pluginManager.registerEvents(new InventoryClose(), this);
        pluginManager.registerEvents(new InventoryOpen(), this);
        pluginManager.registerEvents(new ItemPickup(), this);
        pluginManager.registerEvents(new PlayerScroll(), this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if(event.getPlayer() == null) {
                    return;
                }

                final PacketContainer packet = event.getPacket();
                final WrapperPlayClientBlockDig wrappedPacket = new WrapperPlayClientBlockDig(packet);
                final EnumWrappers.PlayerDigType type = wrappedPacket.getStatus();

                if(type.equals(EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) || type.equals(EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK) || type.equals(EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK)) {
                    final BlockPosition blockPos = wrappedPacket.getLocation();
                    final double distance = event.getPlayer().getLocation().distance(blockPos.toLocation(event.getPlayer().getWorld()));

                    if(distance > 64) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }

    private void registerManagers() {
        registerManager(new PatchManager());
        registerManager(new RandomSpawnManager());
        managers.forEach(manager -> manager.init(this));
    }

    public void registerViolationManager(ViolationManager manager) {
        if (violationManagers.contains(manager)) return;
        violationManagers.add(manager);
    }

    private void registerManager(Manager manager) {
        managers.add(manager);
    }

    @Override
    public void onDisable() {
        instance = null;
        getLogger().info(ChatColor.AQUA + "[HispanarkyCore]" + ChatColor.RED + " is Unloaded and Disabled!");
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }


    //Event Manager
    private void register(Listener... list) {
        pluginManager.registerEvents(this, this);
        for (Listener listener : list) {
            pluginManager.registerEvents(listener, this);
        }
    }

    //Redstone Patches
    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        onPistonEvent(e);
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        onPistonEvent(e);
    }

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent e) {
        redstonePerChunk.put(e.getBlock().getChunk(), redstonePerChunk.computeIfAbsent(e.getBlock().getChunk(), a -> 0) +1);
        if (redstonePerChunk.get(e.getBlock().getChunk()) > 150) {
            e.setNewCurrent(0);
        }
    }

    @EventHandler
    public void onDispenseEvent(BlockDispenseEvent e) {
        redstonePerChunk.put(e.getBlock().getChunk(), redstonePerChunk.computeIfAbsent(e.getBlock().getChunk(), a -> 0) +1);
        if (redstonePerChunk.get(e.getBlock().getChunk()) > 150) {
            e.setCancelled(true);
        }
    }

    public void onPistonEvent(final BlockPistonEvent e) {
        redstonePerChunk.put(e.getBlock().getChunk(), redstonePerChunk.computeIfAbsent(e.getBlock().getChunk(), a -> 0) +1);
        if (redstonePerChunk.get(e.getBlock().getChunk()) > 150) {
            e.setCancelled(true);
        }
    }

    //help
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent evt) {
        if (evt.getMessage().toLowerCase().startsWith("/help")) {
            getConfig().getList("help").forEach(b -> evt.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', (String) b)));
            evt.setCancelled(true);
        }
        if (evt.getMessage().toLowerCase().startsWith("/kill ") || evt.getMessage().equalsIgnoreCase("/kill")) {
            if (getConfig().getBoolean("killcommand")) {
                evt.getPlayer().setHealth(0.0D);
                evt.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void commandEvent(final ServerCommandEvent event) {
        if (event.getCommand().split(" ")[0].equalsIgnoreCase("say")) {
            event.setCancelled(true);
            final String output = event.getCommand().substring(3);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Say.Format").replace("%message%", output)));
        }
    }

    //Lever Patch
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if(this.getConfig().getBoolean("leverspam.enabled")) {
            leverInteract.put(e.getPlayer().getUniqueId(), leverInteract.computeIfAbsent(e.getPlayer().getUniqueId(), a -> 0) +1);
            levernotifier.put(e.getPlayer().getUniqueId(), levernotifier.computeIfAbsent(e.getPlayer().getUniqueId(), a -> 0) +1);
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType() == Material.LEVER) {
                if (leverInteract.get(e.getPlayer().getUniqueId()) > this.getConfig().getInt("leverspam.interval")) {
                    for(Player player : Bukkit.getOnlinePlayers())
                        if(player.isOp()) {
                            if(levernotifier.get(e.getPlayer().getUniqueId()) >= this.getConfig().getInt("leverspam.interval") && levernotifier.get(e.getPlayer().getUniqueId()) < this.getConfig().getInt("leverspam.interval") + 2) {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "HispanarkyCore" + ChatColor.GOLD + "] " + ChatColor.DARK_RED + e.getPlayer().getDisplayName() + ChatColor.GOLD + " is spamming levers!");
                            }
                        }
                    String msg = this.getConfig().getString("leverspam.message");
                    if(this.getConfig().getBoolean("leverspam.kick")) {
                        e.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    e.setCancelled(true);
                }
            }
        }
    }
    public ConfigurationSection getModuleConfig(Manager manager) {
        return getConfig().getConfigurationSection(manager.getName());
    }

    public boolean isVanished(Player player) {
        return gamemodelist.containsKey(player) && taskidlist.containsKey(player);
    }
    public static Main getPlugin(){
        return getPlugin(Main.class);
    }
}
