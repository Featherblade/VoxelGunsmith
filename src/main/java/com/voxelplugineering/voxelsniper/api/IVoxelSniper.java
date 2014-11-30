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
package com.voxelplugineering.voxelsniper.api;

import java.io.File;

/**
 * The specific implementation core class, provides direct access to the specific implementation's version of Gunsmith's interfaces.
 */
public interface IVoxelSniper
{

    /**
     * Returns the specific implementation's sniper manager.
     * 
     * @return the sniper manager
     */
    ISniperFactory<?> getSniperManager();

    /**
     * Returns the specific implementation's brush manager.
     * 
     * @return the brush manager
     */
    IBrushManager getBrushManager();

    /**
     * Returns the ClassLoader used to load Gunsmith and its libraries.
     * 
     * @return the classloader
     */
    ClassLoader getGunsmithClassLoader();

    /**
     * Returns the main thread of VoxelSniper.
     * 
     * @return the thread
     */
    Thread getMainThread();
    
    /**
     * Returns the main storage directory for data.
     * 
     * @return the data folder
     */
    File getDataFolder();

}
