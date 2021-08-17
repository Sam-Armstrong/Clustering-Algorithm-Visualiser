/*
 * Author: Sam Armstrong
 * Date: Summer 2021
 * 
 * Description: Class implementing the DBSCAN clustering algorithm, with 'min_points' and 'epsilon' needing to be set.
 * Inherits from the abstract super class 'ClusterAlgorithm'. 
 */

import java.util.ArrayList;
import java.util.List;

public class DBSCAN extends ClusterAlgorithm
{
    int min_points;
    Double epsilon;

    public DBSCAN()
    {
    }

    @Override
    public void setAttributes(int min_points, Double epsilon)
    {
        this.min_points = min_points;
        this.epsilon = epsilon;
    }

    @Override
    public List<List<List<Double>>> cluster()
    {
        int min_points = this.min_points;
        Double epsilon = this.epsilon;

        List<List<Double>> coordinates = this.coordinates;
        List<List<Double>> core_points = new ArrayList<List<Double>>();

        // Finds all the core points in the dataset
        // (Points with >= min_points points within their epsilon neighbourhood)
        for (int i = 0; i < coordinates.size(); i++)
        {
            List<Double> current_point = coordinates.get(i);
            List<List<Double>> neighbourhood = new ArrayList<List<Double>>();
            neighbourhood.add(current_point);

            // Finds all points that are within the epsilon neighbourhood of the current point (by Euclidean distance)
            for (int j = 0; j < coordinates.size(); j++)
            {
                List<Double> other_point = coordinates.get(j);

                // Finds the Euclidean distance between the two points
                Double distance = Math.sqrt(Math.pow(current_point.get(0) - other_point.get(0), 2) + Math.pow(current_point.get(1) - other_point.get(1), 2));
                
                if (distance < epsilon && !current_point.equals(other_point))
                {
                    neighbourhood.add(other_point);
                }
            }

            // The current point is a core point the points in its neighourhood exceeds 'min_points'
            if (neighbourhood.size() >= min_points && !core_points.contains(current_point))
            {
                core_points.add(current_point);
            }
        }
        

        List<List<List<Double>>> clusters = new ArrayList<List<List<Double>>>();

        // Creates clusters of all the core points - with connected core points being in the same cluster
        for (int z = 0; z < core_points.size(); z++)
        {
            List<List<Double>> current_cluster = new ArrayList<List<Double>>();
            List<Double> current_point = core_points.get(z);

            // Finds the cluster that the current core point is in - if it is in one yet
            for (int j = 0; j < clusters.size(); j++)
            {
                if (clusters.get(j).contains(current_point))
                {
                    current_cluster = clusters.get(j);
                    clusters.remove(current_cluster);
                }
            }

            // Creates a new cluster for the core point if it is not in a cluster yet
            if (current_cluster.size() == 0)
            {
                current_cluster.add(current_point);
            }

            // Adds any other core points to the cluster that are connected to this point (within epsilon distance)
            for (int j = 0; j < core_points.size(); j++)
            {
                List<Double> other_point = core_points.get(j);
                Double distance = Math.sqrt(Math.pow(current_point.get(0) - other_point.get(0), 2) + Math.pow(current_point.get(1) - other_point.get(1), 2));
                
                if (distance < epsilon && !current_point.equals(other_point) && !current_cluster.contains(other_point))
                {
                    current_cluster.add(other_point);
                }
            }

            clusters.add(current_cluster);
        }

        // Clusters all remaining (non-core) points
        for (int i = 0; i < coordinates.size(); i++)
        {
            List<Double> current_point = coordinates.get(i);
            double min_distance = 1000000;
            List<Double> closest_point = new ArrayList<Double>();

            // Finds the closest core point to the current point and the distance to it
            for (int j = 0; j < core_points.size(); j++)
            {
                List<Double> other_point = core_points.get(j);
                Double distance = Math.sqrt(Math.pow(current_point.get(0) - other_point.get(0), 2) + Math.pow(current_point.get(1) - other_point.get(1), 2));
                
                if (distance < min_distance && !current_point.equals(other_point))
                {
                    min_distance = distance;
                    closest_point = other_point;
                }
            }

            // Adds the point to the cluster of the nearest core point if it is within epsilon distance
            if (min_distance < epsilon)
            {
                int cluster_value = 0;

                for (int j = 0; j < clusters.size(); j++)
                {
                    if (clusters.get(j).contains(closest_point))
                    {
                        cluster_value = j;
                    }
                }

                List<List<Double>> cluster = clusters.get(cluster_value);
                cluster.add(current_point);
                clusters.set(cluster_value, cluster);
            }

            // Any points that are not within epsilon distance of any core points are considered noise and are not clustered
        }

        return clusters;
    }    
}
