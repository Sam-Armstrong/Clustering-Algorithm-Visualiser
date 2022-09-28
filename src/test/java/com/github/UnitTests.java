/*
 * Author: Sam Armstrong
 */

package com.github;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the project
 */
public class UnitTests
{
	@Test
	public void dbscanAttributesTest()
	{
		DBSCAN dbscan = new DBSCAN();
		dbscan.setAttributes(2, 80.0);
		Assert.assertEquals(80.0, dbscan.epsilon, 0.0);
		Assert.assertEquals(2, dbscan.min_points);
	}
	
	@Test
	public void dbscanCoordinatesEmptyTest()
	{
		ClusterAlgorithm dbscan = new DBSCAN();
		List<List<Double>> coordinates = new ArrayList<List<Double>>();
		dbscan.setCoordinates(coordinates);
		Assert.assertEquals(dbscan.coordinates, coordinates);
	}
	
	@Test
	public void dbscanCoordinatesTest()
	{
		ClusterAlgorithm dbscan = new DBSCAN();
		List<List<Double>> coordinates = new ArrayList<List<Double>>();
		List<Double> example_coordinates = new ArrayList<Double>();
		
		example_coordinates.add(1.5);
		example_coordinates.add(2.5);
		coordinates.add(example_coordinates);
		coordinates.add(example_coordinates);
		
		dbscan.setCoordinates(coordinates);
		Assert.assertEquals(dbscan.coordinates, coordinates);
	}
	
	@Test
	public void kmeansCoordinatesTest()
	{
		ClusterAlgorithm kmeans = new KMeans();
		List<List<Double>> coordinates = new ArrayList<List<Double>>();
		List<Double> example_coordinates = new ArrayList<Double>();
		
		example_coordinates.add(4.1);
		example_coordinates.add(0.8);
		coordinates.add(example_coordinates);
		coordinates.add(example_coordinates);
		
		kmeans.setCoordinates(coordinates);
		Assert.assertEquals(kmeans.coordinates, coordinates);
	}
	
	@Test
	public void kmeansClustersNumberTest()
	{
		ClusterAlgorithm kmeans = new KMeans();
		kmeans.setAttributes(4, "Random");

		List<List<Double>> coordinates = new ArrayList<List<Double>>();
		Random rand = new Random();
		
		for (int i = 0; i < 20; i++)
		{
			List<Double> coordinate = new ArrayList<Double>();
			coordinate.add(250 * rand.nextDouble());
			coordinate.add(250 * rand.nextDouble());
			
			coordinates.add(coordinate);
		}

		// Tests for 4 clusters
		kmeans.setCoordinates(coordinates);
        List<List<List<Double>>> clusters = kmeans.cluster();
		Assert.assertEquals(4, clusters.size());
		
		// Tests for 3 clusters
		kmeans.setAttributes(3, "Random");
		clusters = kmeans.cluster();
		Assert.assertEquals(3, clusters.size());
	}

}
