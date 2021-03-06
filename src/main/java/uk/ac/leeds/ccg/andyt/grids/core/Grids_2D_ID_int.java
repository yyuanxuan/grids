/*
 * Copyright (C) 2017 geoagdt.
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.ac.leeds.ccg.andyt.grids.core;

import java.io.Serializable;

/**
 * A simple ID class for distinguishing chunks or cells within chunks.
 */
public class Grids_2D_ID_int extends Object implements Serializable, Comparable<Grids_2D_ID_int> {

    /**
     * For storing the row.
     */
    protected int Row;
    /**
     * For storing the column.
     */
    protected int Col;

    protected Grids_2D_ID_int() {
    }

    public Grids_2D_ID_int(Grids_2D_ID_int i) {
        Col = i.Col;
        Row = i.Row;
    }

    /**
     *
     * @param row The row.
     * @param col The column.
     */
    public Grids_2D_ID_int(
            int row,
            int col) {
        Row = row;
        Col = col;
    }

    /**
     * @return Row
     */
    public int getRow() {
        return Row;
    }

    /**
     * @return Col
     */
    public int getCol() {
        return Col;
    }

    /**
     * @return a description of this
     */
    @Override
    public String toString() {
        return "Grids_2D_ID_int( "
                + "Row(" + Row + "), "
                + "Col(" + Col + "))";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Row;
        hash = 97 * hash + Col;
        return hash;
    }

    /**
     * Overrides equals in Object
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (object.getClass() != getClass())) {
            return false;
        }
        Grids_2D_ID_int i = (Grids_2D_ID_int) object;
        return ((Col == i.Col)
                && (Row == i.Row));
    }

    /**
     * Method required by Comparable.
     *
     * @param t
     * @return 
     */
    @Override
    public int compareTo(Grids_2D_ID_int t) {
        if (t.Row > Row) {
            return 1;
        }
        if (t.Row < Row) {
            return -1;
        }
        if (t.Col > Col) {
            return 1;
        }
        if (t.Col < Col) {
            return -1;
        }
        return 0;
    }
}
