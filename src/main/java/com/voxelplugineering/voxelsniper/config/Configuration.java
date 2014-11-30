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
package com.voxelplugineering.voxelsniper.config;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IConfiguration;

/**
 * The configuration storage.
 */
public class Configuration implements IConfiguration
{

    /**
     * The configuration values.
     */
    private Map<String, Object> config;
    /**
     * A Map of containers imported to this configuration storage.
     */
    private Map<String, Object> containers;

    /**
     * Constructs a new Configuration
     */
    public Configuration()
    {
        this.config = new HashMap<String, Object>();
        this.containers = new HashMap<String, Object>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String name, Object value)
    {
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        checkNotNull(value, "Value cannot be null!");
        this.config.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name)
    {
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        return this.config.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerContainer(Class<?> container)
    {
        checkNotNull(container, "Container cannot be null!");
        String name = container.getSimpleName();
        if (this.containers.containsKey(name))
        {
            throw new IllegalArgumentException("Cannot register an already registered container");
        }
        //Load default values from the container
        Object obj;
        try
        {
            obj = container.newInstance();
            this.containers.put(name, obj);
            Gunsmith.getLogger().info("Loading configuration container: " + name);
            for (Field f : container.getDeclaredFields())
            {
                String n = f.getName();
                try
                {
                    Object v = f.get(obj);
                    set(n, v);
                    Gunsmith.getLogger().info("Set configuration value " + n + " to " + v.toString());
                } catch (IllegalArgumentException e)
                {
                    Gunsmith.getLogger().error(e, "Error setting configuration value.");
                } catch (IllegalAccessException e)
                {
                    Gunsmith.getLogger().error(e, "Error setting configuration value.");
                }
            }
        } catch (InstantiationException e1)
        {
            Gunsmith.getLogger().error(e1, "Could not create a new instance of the container");
        } catch (IllegalAccessException e1)
        {
            Gunsmith.getLogger().error(e1, "Could not create a new instance of the container");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getContainer(String containerName)
    {
        checkNotNull(containerName, "Name cannot be null!");
        checkArgument(!containerName.isEmpty(), "Name cannot be empty");
        return this.containers.get(containerName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getContainers()
    {
        Set<Entry<String, Object>> entries = this.containers.entrySet();
        Object[] containers = new Object[entries.size()];
        int i = 0;
        for (Entry<String, Object> e : entries)
        {
            containers[i++] = e.getValue();
        }
        return containers;
    }

}
