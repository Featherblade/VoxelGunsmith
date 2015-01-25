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
package com.voxelplugineering.voxelsniper.api.brushes;

import java.util.Map;

import com.thevoxelbox.vsl.api.variables.VariableScope;
import com.voxelplugineering.voxelsniper.brushes.BrushNodeGraph;

/**
 * An interface for anything which may have a brush selected.
 */
public interface BrushHolder
{

    /**
     * Returns the brush manager specific to this user.
     * 
     * @return the sniper's brush manager
     */
    BrushManager getPersonalBrushManager();

    /**
     * Sets the current brush of this sniper.
     * 
     * @param brush the new brush, cannot be null
     */
    void setCurrentBrush(BrushNodeGraph brush);

    /**
     * Returns the currently selected brush of this sniper.
     * 
     * @return the brush
     */
    BrushNodeGraph getCurrentBrush();

    /**
     * Returns this sniper's current brush settings.
     * 
     * @return the brush settings
     */
    VariableScope getBrushSettings();

    /**
     * Resets the settings of this player to their default configuration.
     */
    void resetSettings();

    /**
     * Gets the player's brush arguments.
     * 
     * @return The arguments
     */
    Map<String, String> getBrushArguments();

    /**
     * Sets a specific brush's argument.
     * 
     * @param brush The brush
     * @param arg The argument
     */
    void setBrushArgument(String brush, String arg);

}
