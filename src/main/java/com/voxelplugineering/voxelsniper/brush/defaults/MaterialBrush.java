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
package com.voxelplugineering.voxelsniper.brush.defaults;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.entity.Player;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.core.shape.SingleMaterialShape;
import com.voxelplugineering.voxelsniper.core.world.queue.ShapeChangeQueue;


public class MaterialBrush extends AbstractBrush {

    public MaterialBrush() {
        super("material", BrushPartType.EFFECT);
    }

    @Override
    public void run(Player player, BrushVars args) {
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if(!s.isPresent()) {
            player.sendMessage("You must have at least one shape brush before your material brush.");
            return;
        }
        Optional<Material> m = args.get(BrushKeys.MATERIAL, Material.class);
        if(!m.isPresent()) {
            player.sendMessage("You must select a material.");
            return;
        }
        Optional<Block> l = args.get(BrushKeys.TARGET_BLOCK, Block.class);
        MaterialShape ms = new SingleMaterialShape(s.get(), m.get());
        new ShapeChangeQueue(player, l.get().getLocation(), ms).flush();
    }

}