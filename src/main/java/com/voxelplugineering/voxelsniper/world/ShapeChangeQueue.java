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
package com.voxelplugineering.voxelsniper.world;

import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonLocation;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.shape.Shape;

/**
 * A special change queue for setting all of a shape to a single material.
 */
public class ShapeChangeQueue extends ChangeQueue
{

    /**
     * The shape to change.
     */
    private Shape shape;
    /**
     * The material to set the shape to.
     */
    private CommonMaterial<?> material;
    /**
     * The origin to set the shape relative to.
     */
    private CommonLocation origin;
    /**
     * The current execution state of this change queue.
     */
    private ExecutionState state;
    /**
     * The position of this queue's execution.
     */
    private long position = 0;

    /**
     * Creates a new {@link ShapeChangeQueue}.
     * 
     * @param sniper the owner
     * @param origin the origin of the shape in the world
     * @param shape the shape
     * @param material the material to set the shape to
     */
    public ShapeChangeQueue(ISniper sniper, CommonLocation origin, Shape shape, CommonMaterial<?> material)
    {
        super(sniper, origin.getWorld());
        this.origin = origin.add(-shape.getOrigin().getX(), -shape.getOrigin().getY(), -shape.getOrigin().getZ());
        this.state = ExecutionState.UNSTARTED;
        this.shape = shape;
        this.material = material;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFinished()
    {
        if (this.position == this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength())
        {
            this.state = ExecutionState.DONE;
        }
        return this.state == ExecutionState.DONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush()
    {
        reset();
        this.getOwner().addPending(this);
        this.getOwner().addHistory(this.invert());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeQueue invert()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int perform(int next)
    {
        int count = 0;
        if (this.state == ExecutionState.UNSTARTED)
        {
            for (int x = 0; x < this.shape.getWidth(); x++)
            {
                for (int y = 0; y < this.shape.getHeight(); y++)
                {
                    for (int z = 0; z < this.shape.getLength(); z++)
                    {
                        if (this.shape.get(x, y, z, false)
                                && (getWorld().getBlockAt(x + this.origin.getFlooredX(), y + this.origin.getFlooredY(), z + this.origin.getFlooredZ()).get()
                                        .getMaterial().isLiquid() || getWorld()
                                        .getBlockAt(x + this.origin.getFlooredX(), y + this.origin.getFlooredY(), z + this.origin.getFlooredZ()).get().getMaterial()
                                        .isReliantOnEnvironment()))
                        {
                            getWorld().setBlockAt(x + this.origin.getFlooredX(), y + this.origin.getFlooredY(), z + this.origin.getFlooredZ(), this.material);
                        }
                    }
                }
            }
            this.state = ExecutionState.INCREMENTAL;
        } else if (this.state == ExecutionState.INCREMENTAL)
        {
            Gunsmith.getLogger().info("Position at " + this.position);
            for (; this.position < this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength() && count < next; this.position++)
            {
                int z = (int) (this.position / (this.shape.getWidth() * this.shape.getHeight()));
                int y = (int) ((this.position % (this.shape.getWidth() * this.shape.getHeight())) / this.shape.getWidth());
                int x = (int) ((this.position % (this.shape.getWidth() * this.shape.getHeight())) % this.shape.getWidth());
                if (this.shape.get(x, y, z, false)
                        && !getWorld().getBlockAt(x + this.origin.getFlooredX(), y + this.origin.getFlooredY(), z + this.origin.getFlooredZ()).get().getMaterial()
                                .isLiquid()
                        && !getWorld().getBlockAt(x + this.origin.getFlooredX(), y + this.origin.getFlooredY(), z + this.origin.getFlooredZ()).get().getMaterial()
                                .isReliantOnEnvironment())
                {
                    count++;
                    getWorld().setBlockAt(x + this.origin.getFlooredX(), y + this.origin.getFlooredY(), z + this.origin.getFlooredZ(), this.material);
                }
            }
            if (this.position == this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength())
            {
                this.state = ExecutionState.DONE;
            }
        }
        return count;
    }

    @Override
    public void reset()
    {
        this.state = ExecutionState.UNSTARTED;
    }

}

/**
 * The execution state.
 */
enum ExecutionState
{
    UNSTARTED, INCREMENTAL, DONE;
}
