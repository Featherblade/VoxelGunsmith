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
package com.voxelplugineering.voxelsniper.service.scheduler;

/**
 * An abstract task for the scheduler proxy.
 */
public abstract class Task
{

    private final Runnable runnable;
    private final int interval;

    /**
     * Creates a new {@link Task}.
     * 
     * @param runnable the task's runnable
     * @param interval the interval of the task's execution, in milliseconds
     */
    public Task(Runnable runnable, int interval)
    {
        this.runnable = runnable;
        this.interval = interval;
    }

    /**
     * Returns the task's runnable.
     * 
     * @return the runnable
     */
    public Runnable getRunnable()
    {
        return this.runnable;
    }

    /**
     * The task's interval in milliseconds.
     * 
     * @return the interval
     */
    public int getInterval()
    {
        return this.interval;
    }

    /**
     * Cancels this task.
     */
    public abstract void cancel();

}
