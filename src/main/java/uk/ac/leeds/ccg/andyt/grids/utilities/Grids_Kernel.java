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
package uk.ac.leeds.ccg.andyt.grids.utilities;

import java.awt.geom.Point2D;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_AbstractGridNumber;

/**
 * Class of methods to do with kernels.
 */
public abstract class Grids_Kernel {

    /**
     * @param value
     * @param mean
     * @param variance
     * @return double expected value of value for a normal distribution with
     * mean and variance.
     */
    public static double getNormalDistributionKernelWeight(
            double value,
            double mean,
            double variance) {
        double result = (1.0d / (variance * Math.sqrt(2.0d * Math.PI)))
                * Math.pow(Math.E, (-1.0d * (((value - mean) * (value - mean))
                        / (2.0d * variance * variance))));
        return result;
    }

    /**
     * @param cellsize
     * @param distance
     * @return double[] of kernel weights based on the normal distribution. The
     * mean and variance of the normal distribution is given by the distances to
     * the centroids of the cells that are within distance.
     */
    public static double[][] getNormalDistributionKernelWeights(
            double cellsize,
            double distance) {
        double[][] result;
        int cellDistance = (int) Math.ceil(distance / cellsize);
        int squareSize = (cellDistance * 2) + 1;
        result = new double[squareSize][squareSize];
        int thisDistanceSquared;
        int i;
        int j;
        double meanSquared = 0.0d;
        double mean;
        double varianceSquared = 0.0d;
        double variance;
        double numberOfCentroids = 0.0d;
        int cellDistanceSquared = cellDistance * cellDistance;
        
        // Calculate mean distance
        for (i = -cellDistance; i <= cellDistance; i++) {
            for (j = -cellDistance; j <= cellDistance; j++) {
                thisDistanceSquared = (i * i) + (j * j);
                if (thisDistanceSquared <= cellDistanceSquared) {
                    numberOfCentroids++;
                    meanSquared += thisDistanceSquared;
                }
            }
        }
        meanSquared /= numberOfCentroids;
        mean = Math.sqrt(meanSquared);
        
        // Calculate the variance of distance
        for (i = -cellDistance; i <= cellDistance; i++) {
            for (j = -cellDistance; j <= cellDistance; j++) {
                thisDistanceSquared = (i * i) + (j * j);
                if (thisDistanceSquared <= cellDistanceSquared) {
                    numberOfCentroids++;
                    varianceSquared += (thisDistanceSquared - meanSquared)
                            * (thisDistanceSquared - meanSquared);
                }
            }
        }
        variance = Math.sqrt(varianceSquared);
        variance /= (numberOfCentroids - 1.0d);

        // Calculate the weights (expected values)
        for (i = -cellDistance; i <= cellDistance; i++) {
            for (j = -cellDistance; j <= cellDistance; j++) {
                thisDistanceSquared = (i * i) + (j * j);
                if (thisDistanceSquared <= cellDistanceSquared) {
                    numberOfCentroids++;
                    result[i + cellDistance][j + cellDistance] = 
                            getNormalDistributionKernelWeight(
                            Math.sqrt((double) thisDistanceSquared),
                                    mean,
                                    variance);
                } else {
                    //weights[ i + cellDistance ][ j + cellDistance ] = noDataValue;
                    result[i + cellDistance][j + cellDistance] = 0.0d;
                }
            }
        }
        return result;
    }

    /**
     * Returns a double value for the height of a kernel at thisDistance from
     * the centre of a kernel with; Bandwidth distance, weight at the centre of
     * weightIntersect and distance decay of weightFactor.
     *
     * @param distance
     * @param weightIntersect
     * @param weightFactor Warning: If weightfactor is < 1.0d strange things
     * could be happening!!!! @param thisDistance @return
     * @param thisDistance
     * @return 
     */
    public static double getKernelWeight(
            double distance,
            double weightIntersect,
            double weightFactor,
            double thisDistance) {
        double weight;
        // The following weight is just one example of a kernel that can be used!
        // It provides a general monotonic curve based on distance over bandwidth.
        /*
        if ( weightFactor > 0.0d ) {
            weight = ( Math.pow( 1.0d - ( Math.pow( thisDistance , 2.0d ) / Math.pow( distance, 2.0d ) ), weightFactor ) * weightIntersect );
            //weight = Math.pow( 1.0d - ( Math.pow( thisDistance, distanceWeightFactor ) / Math.pow( distance, distanceWeightFactor ) ), distanceWeightFactor );
        } else {
            if ( weightFactor < 0.0d ) {
                weight = ( ( 1.0d - Math.pow( 1.0d - ( Math.pow( thisDistance , 2.0d ) / Math.pow( distance, 2.0d ) ), Math.abs( weightFactor ) ) * ( 1.0d - weightIntersect ) ) + weightIntersect );
                //weight = 1.0d - Math.pow( 1.0d - ( Math.pow( thisDistance, Math.abs( distanceWeightFactor ) ) / Math.pow( distance, Math.abs( distanceWeightFactor ) ) ), Math.abs( distanceWeightFactor ) );
            } else {
                weight = weightIntersect;
            }
        }
        return weight;
         */
        //if ( distance == thisDistance ) { return 0.0d; }
        return Math.pow(1.0d - (Math.pow(thisDistance, 2.0d) / Math.pow(distance, 2.0d)), weightFactor) * weightIntersect;
        //return Math.pow( 1.0d - ( Math.pow( thisDistance, distanceWeightFactor ) / Math.pow( distance, distanceWeightFactor ) ), distanceWeightFactor );
    }

    /**
     * Returns a double[] of kernel weights.
     *
     * @param g
     * @param distance
     * @param weightIntersect
     * @param weightFactor
     * @return
     */
    public static double[][] getKernelWeights(
            Grids_AbstractGridNumber g,
            double distance,
            double weightIntersect,
            double weightFactor) {
        boolean handleOutOfMemoryError = true;
        double cellsize = g.getCellsizeDouble(handleOutOfMemoryError);
        int cellDistance = (int) Math.ceil(distance / cellsize);
        double[][] weights = new double[(cellDistance * 2) + 1][(cellDistance * 2) + 1];
        // The following weight is just one example of a kernel that can be used!
        // It provides a general monotonic curve based on distance over bandwidth.
        double x0 = g.getCellXDouble(0L, handleOutOfMemoryError);
        double y0 = g.getCellYDouble(0L, handleOutOfMemoryError);
        double x1;
        double y1;
        double thisDistance;
        int row;
        int col;
        for (row = -cellDistance; row <= cellDistance; row++) {
            for (col = -cellDistance; col <= cellDistance; col++) {
                x1 = g.getCellXDouble(col, handleOutOfMemoryError);
                y1 = g.getCellYDouble(row, handleOutOfMemoryError);
                thisDistance = Grids_Utilities.distance(x0, y0, x1, y1);
                //if ( thisDistance <= distance ) {
                if (thisDistance < distance) {
                    weights[row + cellDistance][col + cellDistance] = getKernelWeight(distance, weightIntersect, weightFactor, thisDistance);
                } else {
                    //weights[ i + cellDistance ][ j + cellDistance ] = noDataValue;
                    weights[row + cellDistance][col + cellDistance] = 0.0d;
                }
            }
        }
        return weights;
    }

    /**
     * Returns a double[] of kernel weights.
     *
     * @param grid2DSquareCell
     * @param rowIndex
     * @param colIndex
     * @param distance
     * @param weightIntersect
     * @param weightFactor
     * @param points
     * @return
     */
    public static double[] getKernelWeights(
            Grids_AbstractGridNumber grid2DSquareCell,
            long rowIndex,
            long colIndex,
            double distance,
            double weightIntersect,
            double weightFactor,
            Point2D.Double[] points) {
        double[] weights = new double[points.length];
        // The following weight is just one example of a kernel that can be used!
        // It provides a general monotonic curve based on distance over bandwidth.
        boolean handleOutOfMemroyError = true;
        Point2D.Double centroid = new Point2D.Double(grid2DSquareCell.getCellXDouble(colIndex, handleOutOfMemroyError), grid2DSquareCell.getCellYDouble(rowIndex, handleOutOfMemroyError));
        double thisDistance;
        double weight;
        for (int i = 0; i < points.length; i++) {
            thisDistance = centroid.distance(points[i]);
            if (thisDistance < distance) {
                weights[i] = getKernelWeight(distance, weightIntersect, weightFactor, thisDistance);
            }
        }
        return weights;
    }

    /**
     * Returns double[] result of kernel weights.
     *
     * @param centroid
     * @param distance
     * @param weightIntersect
     * @param weightFactor
     * @param points
     * @return
     */
    public static double[] getKernelWeights(
            Point2D.Double centroid,
            double distance,
            double weightIntersect,
            double weightFactor,
            Point2D.Double[] points) {
        double[] weights = new double[points.length];
        // The following weight is just one example of a kernel that can be used!
        // It provides a general monotonic curve based on distance over bandwidth.
        double thisDistance;
        for (int i = 0; i < points.length; i++) {
            thisDistance = centroid.distance(points[i]);
            if (thisDistance < distance) {
                weights[i] = getKernelWeight(distance, weightIntersect, weightFactor, thisDistance);
            }
        }
        return weights;
    }

    /**
     * Returns double[] result of kernel parameters where: result[0] = The total
     * sum of all the weights for a given kernel; result[1] = The total number
     * of cells thats centroids are within distance of an arbitrary cell
     * centroid of grid2DSquareCell.
     *
     * @param grid2DSquareCell Grids_AbstractGridNumber for which kernel
     * parameters are returned
     * @param cellDistance
     * @param distance
     * @param weightIntersect
     * @param weightFactor
     * @return
     */
    public static double[] getKernelParameters(
            Grids_AbstractGridNumber grid2DSquareCell,
            int cellDistance,
            double distance,
            double weightIntersect,
            double weightFactor) {
        double kernelParameters[] = new double[2];
        kernelParameters[0] = 0.0d;
        kernelParameters[1] = 0.0d;
        boolean handleOutOfMemroyError = true;
        double x0 = grid2DSquareCell.getCellXDouble(0, handleOutOfMemroyError);
        double y0 = grid2DSquareCell.getCellYDouble(0, handleOutOfMemroyError);
        double x1;
        double y1;
        double thisDistance;
        for (int p = -cellDistance; p <= cellDistance; p++) {
            for (int q = -cellDistance; q <= cellDistance; q++) {
                x1 = grid2DSquareCell.getCellXDouble(q, handleOutOfMemroyError);
                y1 = grid2DSquareCell.getCellYDouble(p, handleOutOfMemroyError);
                thisDistance = Grids_Utilities.distance(x0, y0, x1, y1);
                //if ( thisDistance <= distance ) {
                if (thisDistance < distance) {
                    kernelParameters[0] += getKernelWeight(
                            distance, weightIntersect, weightFactor, thisDistance);
                    kernelParameters[1] += 1.0d;
                }
            }
        }
        return kernelParameters;
    }

    /**
     * Returns a double representing an adaptive kernel weight.
     *
     * @param distance
     * @param bandwidth
     * @param sumWeights
     * @param precision
     * @param weightIntersect
     * @param weightFactor
     * @return
     */
    public static double getAdaptiveKernelWeight(
            double distance, double bandwidth,
            double sumWeights, int precision,
            double weightIntersect, double weightFactor) {
        //int sectionSize = ( int ) Math.ceil( bandwidth / ( double ) precision );
        //double sectionArea = Math.pow( Math.ceil( bandwidth / ( double ) precision ), 2.0d );
        double kernelVolume = getKernelVolume(bandwidth, precision, weightIntersect, weightFactor);
        double kernelWeight = getKernelWeight(bandwidth, weightIntersect, weightFactor, distance);
        return (kernelWeight * sumWeights) / kernelVolume;
    }

    /**
     * Returns a double representing the kernel volume.
     *
     * @param bandwidth
     * @param precision
     * @param weightIntersect
     * @param weightFactor
     * @return
     */
    public static double getKernelVolume(
            double bandwidth, int precision,
            double weightIntersect, double weightFactor) {
        double sumKernelWeights = 0.0d;
        int sectionSize = (int) Math.ceil(bandwidth / (double) precision);
        double sectionArea = (double) sectionSize * sectionSize;
        double thisDistance;
        Point2D.Double kernelCentroid = new Point2D.Double(0.0d, 0.0d);
        Point2D.Double point;
        //int sectionCount = 0;
        for (int p = 1; p < precision; p++) {
            for (int q = 0; q < precision; q++) {
                point = new Point2D.Double((double) p * sectionSize, (double) q * sectionSize);
                thisDistance = point.distance(kernelCentroid);
                if (thisDistance < bandwidth) {
                    sumKernelWeights += getKernelWeight(bandwidth, weightIntersect, weightFactor, thisDistance);
                    //sectionCount ++;
                }
            }
        }
        // Multiply by 4 for all quadrants
        // Add kernelCentroid weight
        // Multiply result be sectionArea to get volume
        return ((sumKernelWeights * 4.0d) + weightIntersect) * sectionArea;
    }

}
