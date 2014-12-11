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
package com.voxelplugineering.voxelsniper.nodes.shape;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.Node;
import com.thevoxelbox.vsl.type.Type;
import com.thevoxelbox.vsl.type.TypeDepth;

/**
 * Creates a square disc with with a side length of radius*2+1
 */
public class VoxelDiscShapeNode extends Node
{

    /**
     * 
     */
    private static final long serialVersionUID = -7263550249408114074L;

    /**
     * Creates a new node.
     */
    public VoxelDiscShapeNode()
    {
        super("VoxelDisc Shape", "shape");
        addInput("radius", Type.FLOAT, true, null);
        addOutput("shape", Type.getType("SHAPE", TypeDepth.SINGLE).get(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        /*
         * ALOAD radius
         * CHECKCAST java/lang/Double
         * INVOKEVIRTUAL Double.doubleValue () : double
         * INVOKESTATIC ShapeFactory.createSphere (double) : Shape
         * ASTORE shape
         */

        int radius = getInput("radius").getSource().get();
        mv.visitVarInsn(Opcodes.DLOAD, radius);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "ceil", "(D)D", false);
        mv.visitInsn(Opcodes.D2I);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "com/voxelplugineering/voxelsniper/common/CommonDirection", "UP",
                "Lcom/voxelplugineering/voxelsniper/common/CommonDirection;");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/voxelplugineering/voxelsniper/shape/ShapeFactory", "createVoxelDisc",
                "(ILcom/voxelplugineering/voxelsniper/common/CommonDirection;)Lcom/voxelplugineering/voxelsniper/shape/Shape;", false);
        mv.visitVarInsn(Opcodes.ASTORE, localsIndex);
        setOutput("shape", localsIndex);
        return localsIndex + 1;
    }
}