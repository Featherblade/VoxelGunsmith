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
package com.voxelplugineering.voxelsniper.brush.shape;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.brush.AbstractBrush;
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.shape.csg.CylinderShape;
import com.voxelplugineering.voxelsniper.util.Direction;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * A shape brush which defines a cylindrical region.
 */
public class CylinderBrush extends AbstractBrush
{

    /**
     * Creates a new {@link CylinderBrush}.
     */
    public CylinderBrush()
    {
        super("cylinder", BrushPartType.SHAPE);
    }

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        double size = args.get(BrushKeys.BRUSH_SIZE, Double.class).get();
        Optional<Double> oheight = args.get(BrushKeys.HEIGHT, Double.class);
        if (!oheight.isPresent())
        {
            player.sendMessage("Please specify a height for the cylinder: /param cylinder height=#");
            return ExecutionResult.abortExecution();
        }
        int height = (int) Math.floor(oheight.get());
        boolean face = args.get(BrushKeys.USE_FACE, Boolean.class).or(false);
        Shape s = null;
        if (face)
        {
            Direction d = args.get(BrushKeys.TARGET_FACE, Direction.class).or(Direction.UP);
            switch (d)
            {
            case NORTH:
            case SOUTH:
                s = new CylinderShape(size, height, size, new Vector3i(size, 0, size), Direction.SOUTH);
                break;
            case EAST:
            case WEST:
                s = new CylinderShape(size, height, size, new Vector3i(size, 0, size), Direction.EAST);
                break;
            default:
                s = new CylinderShape(size, height, size, new Vector3i(size, 0, size));
                break;
            }
        } else
        {
            s = new CylinderShape(size, height, size, new Vector3i(size, 0, size));
        }
        args.set(BrushContext.RUNTIME, BrushKeys.SHAPE, s);
        return ExecutionResult.continueExecution();
    }

}
