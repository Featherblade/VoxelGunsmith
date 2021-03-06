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
package com.voxelplugineering.voxelsniper.brush;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.service.persistence.DataSourceReader;

/**
 * A standard brush manager.
 */
public class CommonBrushManager implements BrushManager
{

    private BrushManager parent = null;
    private final Map<String, Brush> brushes;
    private final List<DataSourceReader> loaders;

    /**
     * Creates a new CommonBrushManager.
     */
    public CommonBrushManager()
    {
        this(null);
    }

    /**
     * Creates a new CommonBrushManager with the subscribed parent
     * 
     * @param parent the parent
     */
    public CommonBrushManager(BrushManager parent)
    {
        this.parent = parent;
        this.brushes = Maps.newHashMap();
        this.loaders = Lists.newArrayList();
        /*TODO BrushLoader
         * 
         * if (Gunsmith.getPlatformProxy() != null)
        {
            DataSourceReader loader = Gunsmith.getPlatformProxy().getBrushLoader();
            if(loader != null)
            {
                this.loaders.add(loader);
            }
        }*/
    }

    @Override
    public void addLoader(DataSourceReader loader)
    {
        checkNotNull(loader);
        this.loaders.add(0, loader);
    }

    @Override
    public void loadBrush(String identifier, Brush graph)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        checkNotNull(graph, "Brush class cannot be null!");
        // TODO: Check version if already loaded
        this.brushes.put(identifier, graph);
    }

    @Override
    public void loadBrush(String identifier)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        /*Brush graph = null;
         TODO brush loading
         * 
         * for (Iterator<DataSourceReader> iter = this.loaders.iterator(); iter.hasNext();)
        {
            DataSourceReader loader = iter.next();
            Optional<Brush> brush = Gunsmith.getBrushFactory();
            if(brush.isPresent())
            {
                graph = brush.get();
                break;
            }
        }
        if (graph != null)
        {
            loadBrush(identifier, graph);
        }*/
    }

    @Override
    public Optional<Brush> getBrush(String identifier)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        Brush br = this.brushes.get(identifier);
        if (br == null)
        {
            if (this.parent != null)
            {
                return this.parent.getBrush(identifier);
            }
            return Optional.absent();
        }
        return Optional.of(br);
    }

    @Override
    public void setParent(BrushManager parent)
    {
        this.parent = parent;
    }

    @Override
    public BrushManager getParent()
    {
        return this.parent;
    }

    @Override
    public List<DataSourceReader> getAllLoaders()
    {
        return this.loaders;
    }

    @Override
    public void clearLoaders()
    {
        this.loaders.clear();
    }

}
