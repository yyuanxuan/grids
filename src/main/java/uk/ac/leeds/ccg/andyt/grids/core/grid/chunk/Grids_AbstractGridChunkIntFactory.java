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
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridInt;

/**
 * Abstract Class for defining (an interface for) chunk factory methods. These
 * methods generally would work as protected, but are tested externally and so
 * are declared public. Really no user should have a chunk without a grid even
 * if the grid contains only one chunk.
 */
public abstract class Grids_AbstractGridChunkIntFactory
        extends Grids_AbstractGridChunkFactory {

    /**
     * Creates a new Grids_AbstractGridChunkInt containing all
     * noDataValues that is linked to g via chunkID.
     *
     * @param g
     * @param chunkID
     * @return
     */
    public abstract Grids_AbstractGridChunkInt createGridChunkInt(
            Grids_GridInt g,
            Grids_2D_ID_int chunkID);

    /**
     * Creates a new Grids_AbstractGridChunkInt with values taken from
     * chunk.
     *
     * @param chunk
     * @param chunkID
     * @return
     */
    public abstract Grids_AbstractGridChunkInt createGridChunkInt(
            Grids_AbstractGridChunkInt chunk,
            Grids_2D_ID_int chunkID);

}
