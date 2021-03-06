/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 The Voxel Plugineering Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.voxelplugineering.voxelsniper.event.handler;

import com.google.common.eventbus.DeadEvent;
import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.event.SnipeEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent.SniperCreateEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent.SniperDestroyEvent;
import com.voxelplugineering.voxelsniper.service.config.Configuration;
import com.voxelplugineering.voxelsniper.service.eventbus.EventHandler;
import com.voxelplugineering.voxelsniper.service.permission.PermissionProxy;
import com.voxelplugineering.voxelsniper.service.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.RayTrace;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.queue.OfflineUndoHandler;

/**
 * An event handler for the default behavior for events.
 */
public class CommonEventHandler
{

    private final Configuration conf;
    private final PlayerRegistry<?> players;
    private final OfflineUndoHandler undo;
    private final PermissionProxy perms;

    //private final String playerFolderName = this.conf.get("playerDataDirectory", String.class).or("players/");
    //private final String aliasFile = this.conf.get("aliasesFileName", String.class).or("aliases.json");

    private final double rayTraceRange;

    /**
     * Constructs a new {@link CommonEventHandler}.
     * 
     * @param context The context
     */
    public CommonEventHandler(Context context)
    {
        this.conf = context.getRequired(Configuration.class);
        this.players = context.getRequired(PlayerRegistry.class);
        this.undo = context.getRequired(OfflineUndoHandler.class);
        this.perms = context.getRequired(PermissionProxy.class);

        this.rayTraceRange = this.conf.get("rayTraceRange", Double.class).or(250.0);
    }

    /**
     * An event handler for the {@link SniperCreateEvent}s. This should initialize the players into
     * the player registry, creating their {@link Player} objects and setting their defaults.
     * 
     * @param event the event
     */
    @EventHandler
    public void onPlayerJoin(SniperEvent.SniperCreateEvent event)
    {

        //TODO persistence
        /*Player player = event.getSniper();
        File playerFolder = new File(Gunsmith.getPlatformProxy().getDataFolder(), this.playerFolderName + player.getUniqueId().toString());
        playerFolder.mkdirs();
        File aliases = new File(playerFolder, this.aliasFile);
        JsonDataSource data = new JsonDataSource(aliases);
        if (aliases.exists())
        {
            try
            {
                data.read(player.getPersonalAliasHandler());
            } catch (IOException e)
            {
                Gunsmith.getLogger().error(e, "Error loading player aliases!");
            }
        }*/
        //Gunsmith.getOfflineUndoHandler().invalidate(player.getName());
    }

    /**
     * An event handler for {@link SniperDestroyEvent} in order to save player-specific settings.
     * 
     * @param event the event
     */
    @EventHandler
    public void onPlayerLeave(SniperEvent.SniperDestroyEvent event)
    {
        Player player = event.getSniper();
        // TODO persistence
        /*File playerFolder = new File(Gunsmith.getPlatformProxy().getDataFolder(), this.playerFolderName + player.getUniqueId().toString());
        playerFolder.mkdirs();
        File aliases = new File(playerFolder, this.aliasFile);
        JsonDataSource data = new JsonDataSource(aliases);

        try
        {
            if (aliases.exists())
            {
                aliases.createNewFile();
            }
            data.write(player.getPersonalAliasHandler());
        } catch (IOException e)
        {
            Gunsmith.getLogger().error(e, "Error saving player aliases!");
        }*/

        this.undo.register(player.getName(), player.getUndoHistory());
        this.players.invalidate(player.getName());
    }

    /**
     * Processes the given {@link com.voxelplugineering.voxelsniper.event.SnipeEvent} and
     * performs all necessary checks of the event. This event handler is supports asynchronous
     * callback.
     * 
     * @param event the snipe event to perform
     */
    @EventHandler
    public void onSnipe(SnipeEvent event)
    {
        Player sniper = event.getSniper();
        if (!this.perms.hasPermission(sniper, "voxelsniper.sniper"))
        {
            return;
        }
        boolean attemptedNullAction = false;
        try
        {
            Location location = sniper.getLocation();
            double yaw = event.getYaw();
            double pitch = event.getPitch();
            //TODO move min/max values form conf to world
            int minY = this.conf.get("minimumWorldDepth", int.class).or(0);
            int maxY = this.conf.get("maximumWorldHeight", int.class).or(255);
            double step = this.conf.get("rayTraceStep", double.class).or(0.2);
            Vector3d eyeOffs = new Vector3d(0, this.conf.get("playerEyeHeight", double.class).or(1.62), 0);
            RayTrace ray = new RayTrace(location, yaw, pitch, this.rayTraceRange, minY, maxY, step, eyeOffs);
            double range = this.rayTraceRange;
            if (sniper.getBrushVars().has(BrushKeys.RANGE))
            {
                range = sniper.getBrushVars().get(BrushKeys.RANGE, Double.class).get();
            }
            ray.setRange(range);
            ray.trace();

            if (ray.getTargetBlock() == null)
            {
                attemptedNullAction = true;
            }
            BrushVars vars = sniper.getBrushVars();
            vars.clearRuntime();
            vars.set(BrushContext.RUNTIME, BrushKeys.ORIGIN, location);
            vars.set(BrushContext.RUNTIME, BrushKeys.YAW, yaw);
            vars.set(BrushContext.RUNTIME, BrushKeys.PITCH, pitch);
            vars.set(BrushContext.RUNTIME, BrushKeys.TARGET_BLOCK, ray.getTargetBlock());
            vars.set(BrushContext.RUNTIME, BrushKeys.TARGET_FACE, ray.getTargetFace());
            vars.set(BrushContext.RUNTIME, BrushKeys.LAST_BLOCK, ray.getLastBlock());
            vars.set(BrushContext.RUNTIME, BrushKeys.LAST_FACE, ray.getLastFace());
            vars.set(BrushContext.RUNTIME, BrushKeys.ACTION, event.getAction());
            vars.set(BrushContext.RUNTIME, BrushKeys.LENGTH, ray.getLength());
            //TODO move player to the global context not runtime
            vars.set(BrushContext.RUNTIME, BrushKeys.PLAYER, sniper);
            //Gunsmith.getLogger().info("Snipe at " + ray.getTargetBlock().getLocation().toString());
            sniper.getCurrentBrush().run(sniper, vars);
        } catch (Throwable e)
        {
            if (!attemptedNullAction)
            {
                sniper.sendMessage("Error executing brush, see console for more details.");
                GunsmithLogger.getLogger().error(e, "Error executing brush");
            }
        }
    }

    /**
     * Reports on unhandled (aka. dead) events on the event bus.
     * 
     * @param deadEvent the dead event
     */
    @EventHandler
    public void handleDeadEvent(DeadEvent deadEvent)
    {
        Object event = deadEvent.getEvent();
        GunsmithLogger.getLogger().warn("An unhandled " + event.getClass().getName() + " event was posted to the event bus!");
    }
}
