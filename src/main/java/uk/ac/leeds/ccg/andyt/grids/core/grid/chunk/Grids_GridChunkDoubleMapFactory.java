/**
 * Version 1.0 is to handle single variable 2DSquareCelled raster data.
 * Copyright (C) 2005 Andy Turner, CCG, University of Leeds, UK.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.
 */
package uk.ac.leeds.ccg.andyt.grids.core.grid.chunk;

import uk.ac.leeds.ccg.andyt.grids.core.Grids_2D_ID_int;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDouble;

/**
 * A factory for constructing Grids_GridChunkDoubleMap instances.
 */
public class Grids_GridChunkDoubleMapFactory
        extends Grids_AbstractGridChunkDoubleFactory {

    public Grids_GridChunkDoubleMapFactory() {
    }

    @Override
    public Grids_GridChunkDoubleMap createGridChunkDouble(
            Grids_GridDouble g,
            Grids_2D_ID_int chunkID) {
        return new Grids_GridChunkDoubleMap(
                g,
                chunkID);
    }

    @Override
    public Grids_GridChunkDoubleMap createGridChunkDouble(
            Grids_AbstractGridChunkDouble chunk,
            Grids_2D_ID_int chunkID) {
        return new Grids_GridChunkDoubleMap(
                chunk,
                chunkID,
                chunk.getGrid().getNoDataValue(false));
    }

    public Grids_GridChunkDoubleMap createGridChunkDouble(
            Grids_AbstractGridChunkDouble chunk,
            Grids_2D_ID_int chunkID,
            double defaultValue) {
        return new Grids_GridChunkDoubleMap(
                chunk,
                chunkID,
                defaultValue);
    }

}
