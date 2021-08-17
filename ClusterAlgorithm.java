/*
 * Author: Sam Armstrong
 * Date: Summer 2021
 * 
 * Description: Provides an abstract superclass for all clustering algorithms with common method implemented, 
 * such as 'setCoordinates'. This can allow for polymorphism to be used.
 */

import java.util.List;

public abstract class ClusterAlgorithm
{
    List<List<Double>> coordinates;

    public ClusterAlgorithm()
    {
    }

    public void setCoordinates(List<List<Double>> coordinates)
    {
        this.coordinates = coordinates;
    }

    public List<List<List<Double>>> cluster()
    {
        return null;
    }

    public void setAttributes(int min_points, Double epsilon)
    {
    }

    public void setAttributes(int num_clusters, String initialization)
    {
    }
}
