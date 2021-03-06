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
package com.voxelplugineering.voxelsniper.service.registry;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.service.Service;
import com.voxelplugineering.voxelsniper.world.material.Material;

/**
 * A factory for {@link Material}s wrapping a Material from the underlying implementation.
 * 
 * @param <T> The material class of the specific implementation
 */
public interface MaterialRegistry<T> extends Service
{

    /**
     * Returns the {@link Material} representation of air/empty space in the underlying
     * implementation.
     * 
     * @return the material of air
     */
    Material getAirMaterial();

    /**
     * Gets the {@link Material} with the given name.
     * 
     * @param name The name
     * @return The material, if found
     */
    Optional<Material> getMaterial(String name);

    /**
     * Gets the {@link Material} with the given key.
     * 
     * @param material The object key
     * @return The material, if found
     */
    Optional<Material> getMaterial(T material);

    /**
     * Registers a material with this registry.
     * 
     * @param name The material name
     * @param object The material from the underlying impl
     * @param material The material for gunsmith
     */
    void registerMaterial(String name, T object, Material material);

    /**
     * Gets all materials registered within this registry.
     * 
     * @return The materials
     */
    Iterable<Material> getMaterials();

}
