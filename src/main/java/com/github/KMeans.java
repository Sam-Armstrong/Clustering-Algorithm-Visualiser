/*
 * Author: Sam Armstrong
 */

package com.github;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Description: Class implementing a K-Means clustering algorithm to a given set of coordinates.
 * The attributes 'num_clusters' and 'initialization' need to be set using the 'setAttributes' method before 'cluster'
 * can be called. Inherits from 'ClusterAlgorithm'.
 */
public class KMeans extends ClusterAlgorithm
{
    int num_clusters;
    String initialization;

    public KMeans()
    {
    }

    @Override
    public void setAttributes(int num_clusters, String initialization)
    {
        this.num_clusters = num_clusters;
        this.initialization = initialization;
    }

    @Override
    public List<List<List<Double>>> cluster()
    {
        int num_clusters = this.num_clusters;
        String initialization = this.initialization;

        List<List<Double>> centroids = new ArrayList<List<Double>>();
        Random rand = new Random();

        // K-Means++ centroid initialization
        if (initialization.equals("K-Means++"))
        {
            List<Double> initial_centroid = coordinates.get(rand.nextInt(coordinates.size())); // Random initial centroid
            centroids.add(initial_centroid);

            while (centroids.size() < num_clusters)
            {
                List<List<Double>> distances = new ArrayList<List<Double>>();
                List<Double> min_distances = new ArrayList<Double>();
                List<Double> squared_min_distances = new ArrayList<Double>();

                // Creates a 2D list of the distances between points and their nearest centroid
                for (int i = 0; i < coordinates.size(); i++)
                {
                    List<Double> current_point = coordinates.get(i);
                    distances.add(new ArrayList<Double>());

                    for (int j = 0; j < centroids.size(); j++)
                    {
                        List<Double> centroid = centroids.get(j);
                        Double distance = Math.sqrt(Math.pow(current_point.get(0) - centroid.get(0), 2) + Math.pow(current_point.get(1) - centroid.get(1), 2));
                        distances.get(i).add(distance);
                    }
                }

                // Finds the shortest distance to a centroid for each point
                for (int i = 0; i < distances.size(); i++)
                {
                    Double min_distance = 1000000.0;

                    for (int j = 0; j < distances.get(i).size(); j++)
                    {
                        if (distances.get(i).get(j) < min_distance)
                        {
                            min_distance = distances.get(i).get(j);
                        }
                    }

                    min_distances.add(min_distance);
                }

                Double total_distances = 0.0;
                // Square the min distance for each point (standard for the K-Means++ initialization technique)
                for (int i = 0; i < min_distances.size(); i++)
                {
                    Double squared_distance = Math.pow(min_distances.get(i), 2);
                    squared_min_distances.add(squared_distance);
                    total_distances += squared_distance;
                }

                Double random_value = total_distances * rand.nextDouble();
                List<Double> new_centroid = new ArrayList<Double>();

                // Randomly assign a new centroid based upon the probability distribution of the squared distances
                // - So the new centroid is likely to be further away from the current centroid(s)
                for (int i = 0; i < coordinates.size(); i++)
                {
                    if (random_value <= squared_min_distances.get(i))
                    {
                        new_centroid = coordinates.get(i);
                    }
                    else
                    {
                        random_value -= squared_min_distances.get(i);
                    }
                }

                centroids.add(new_centroid);
            }
        }

        // Random centroid initialization
        else
        {
            while (centroids.size() < num_clusters)
            {
                List<Double> centroid = coordinates.get(rand.nextInt(coordinates.size()));

                if (!centroids.contains(centroid))
                {
                    centroids.add(centroid);
                }
            }
        }

        List<List<List<Double>>> clusters = assignPoints(coordinates, centroids, num_clusters);
        Boolean x = false;

        while (x.equals(false))
        {
            final List<List<List<Double>>> old_clusters = clusters;
            centroids = meanCentroids(clusters);
            clusters = assignPoints(coordinates, centroids, num_clusters);

            if (old_clusters.equals(clusters))
            {
                x = true;
            }
        }

        return clusters;
    }

    // Assigns each point to its closest centroid to form clusters
    private static List<List<List<Double>>> assignPoints(List<List<Double>> coordinates, List<List<Double>> centroids, Integer num_clusters)
    {
        // Instantiates the clusters list
        List<List<List<Double>>> clusters = new ArrayList<List<List<Double>>>();
        for (int i = 0; i < num_clusters; i++)
        {
            clusters.add(new ArrayList<List<Double>>());
        }

        // Loops through all the points to add them all to a cluster
        for (int i = 0; i < coordinates.size(); i ++)
        {
            List<Double> distances = new ArrayList<Double>();
            List<Double> current_point = coordinates.get(i);
            Double min_value = 100000.0;
            Integer cluster_number = 0;

            // Finds the shortest distance from the current point to any centroid
            for (int j = 0; j < num_clusters; j++)
            {
                List<Double> centroid = centroids.get(j);
                
                Double distance = Math.sqrt(Math.pow(current_point.get(0) - centroid.get(0), 2) + Math.pow(current_point.get(1) - centroid.get(1), 2));
                distances.add(distance);
                
                if (distance < min_value)
                {
                    min_value = distance;
                }
            }

            // Finds the coordinates of the closest centroid to the current point
            for (int j = 0; j < distances.size(); j++)
            {
                if (distances.get(j) == min_value)
                {
                    cluster_number = j;
                }
            }

            clusters.get(cluster_number).add(current_point); // Add the point to its cluster
        }

        return clusters;
    }

    // Finds the average coordinates (centroid) over all the points in each cluster
    private static List<List<Double>> meanCentroids(List<List<List<Double>>> clusters)
    {
        List<List<Double>> centroids = new ArrayList<List<Double>>();

        for (int i = 0; i < clusters.size(); i++)
        {
            List<List<Double>> current_cluster = clusters.get(i);
            Double totalX = 0.0;
            Double totalY = 0.0;
            List<Double> centroid = new ArrayList<Double>();

            // Sums all the coordinates for the current cluster so the mean for x and y can be calculated
            for (int j = 0; j < current_cluster.size(); j++)
            {
                totalX += current_cluster.get(j).get(0);
                totalY += current_cluster.get(j).get(1);
            }

            Double meanX = totalX / current_cluster.size();
            Double meanY = totalY / current_cluster.size();
            centroid.add(meanX);
            centroid.add(meanY);
            centroids.add(centroid);
        }

        return centroids;
    }    
}
