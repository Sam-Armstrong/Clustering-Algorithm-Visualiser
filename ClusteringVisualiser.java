/*
 * Author: Sam Armstrong
 * Date: Summer 2021
 * 
 * Description: Creates the GUI and allows the user to interact with different clustering algorithms and their parameters,
 * and see how changing these can affect the clusterings that occur.
 */

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class ClusteringVisualiser
{
    public List<List<Double>> coordinates = new ArrayList<List<Double>>();
    public List<ShapeItem> shapes = new ArrayList<ShapeItem>();

    public ClusteringVisualiser(int n)
    {
        Random rand = new Random();

        // Creates n randomly positioned points
        for (int i = 0; i < n; i++)
        {
            shapes.add(new ShapeItem(new Ellipse2D.Double((rand.nextInt(325) + 5), (rand.nextInt(245) + 70), 15, 15), Color.BLUE));
        }
        
        // Logs the coordinates of all the points in a list
        for (int i = 0; i < shapes.size(); i++)
        {
            List<Double> location = new ArrayList<Double>();
            ShapeItem circle = shapes.get(i);
            location.add(circle.getShape().getBounds2D().getMinX());
            location.add(circle.getShape().getBounds2D().getMinY());
            coordinates.add(location);
        }

        // Creates the window
        JFrame frame = new JFrame("Clustering Visualiser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sets up the panel that will contain the points and other GUI elements
        ClusterPanel panel = new ClusterPanel(shapes);
        frame.add(panel);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);

        String algorithm_choices[] = { "DBSCAN", "K-Means" };
        String initialization_choices[] = { "K-Means++", "Random" };
        Integer clusters_choices[] = { 1, 2, 3, 4, 5, 6, 7 };
        Integer points_choices[] = { 1, 2, 3, 4, 5, 6, 7 };

        // Creates the GUI elements that allow the user to interact with the program 
        JButton points_button = new JButton("Re-distribute Points");
        JButton cluster_button = new JButton("Cluster");
        JComboBox<String> algorithm_selection = new JComboBox<String>(algorithm_choices);
        JComboBox<String> initialization = new JComboBox<String>(initialization_choices);        
        JComboBox<Integer> num_clusters = new JComboBox<Integer>(clusters_choices);        
        JComboBox<Integer> min_points = new JComboBox<Integer>(points_choices);
        JTextField epsilon_text = new JTextField("80.0");

        JLabel initialization_label = new JLabel("Initialization");
        JLabel num_clusters_label = new JLabel("Number of Clusters");
        JLabel min_points_label = new JLabel("Min Points");
        JLabel epsilon_label = new JLabel("Epsilon");
        
        algorithm_selection.setSelectedItem("DBSCAN");

        // Adds the GUI elements to the panel
        panel.add(points_button);
        panel.add(algorithm_selection);
        panel.add(cluster_button);
        panel.add(min_points_label);
        panel.add(min_points);
        panel.add(epsilon_label);
        panel.add(epsilon_text);
        panel.add(initialization_label);
        panel.add(initialization);
        panel.add(num_clusters_label);
        panel.add(num_clusters);

        initialization_label.setVisible(false);
        initialization.setVisible(false);
        num_clusters_label.setVisible(false);
        num_clusters.setVisible(false);

        algorithm_selection.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	// Changes the buttons that are visible based on the algorithm that is currently selected
                if (algorithm_selection.getSelectedItem().equals("K-Means"))
                {
                    min_points_label.setVisible(false);
                    min_points.setVisible(false);
                    epsilon_label.setVisible(false);
                    epsilon_text.setVisible(false);
                    initialization_label.setVisible(true);
                    initialization.setVisible(true);
                    num_clusters_label.setVisible(true);
                    num_clusters.setVisible(true);
                }
                else if (algorithm_selection.getSelectedItem().equals("DBSCAN"))
                {
                    initialization_label.setVisible(false);
                    initialization.setVisible(false);
                    num_clusters_label.setVisible(false);
                    num_clusters.setVisible(false);
                    min_points_label.setVisible(true);
                    min_points.setVisible(true);
                    epsilon_label.setVisible(true);
                    epsilon_text.setVisible(true);
                }
            }
        });


        // Removes the current points and creates new ones (redistributes the points)
        points_button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                String chosen_algorithm = algorithm_selection.getSelectedItem().toString();
                Integer min_points_int = Integer.parseInt(min_points.getSelectedItem().toString());
                Double epsilon = Double.parseDouble(epsilon_text.getText());
                Integer n_clusters = Integer.parseInt(num_clusters.getSelectedItem().toString());
                String init_method = initialization.getSelectedItem().toString();

                shapes = new ArrayList<ShapeItem>();
                coordinates = new ArrayList<List<Double>>();

                // Creates n randomly positioned points
                for (int i = 0; i < n; i++)
                {
                    shapes.add(new ShapeItem(new Ellipse2D.Double((rand.nextInt(325) + 5), (rand.nextInt(245) + 70), 15, 15), Color.BLUE));
                }

                // Logs the coordinates of all the points in a list
                for (int i = 0; i < shapes.size(); i++)
                {
                    List<Double> location = new ArrayList<>();
                    ShapeItem circle = shapes.get(i);
                    location.add(circle.getShape().getBounds2D().getMinX());
                    location.add(circle.getShape().getBounds2D().getMinY());
                    coordinates.add(location);
                }

                ClusterPanel panel = new ClusterPanel(shapes);
                frame.add(panel);

                // GUI elements need to be re-defined here for Swing to work correctly
                String algorithm_choices[] = { "DBSCAN", "K-Means" };
                String initialization_choices[] = { "K-Means++", "Random" };
                Integer clusters_choices[] = { 1, 2, 3, 4, 5, 6, 7 };
                Integer points_choices[] = { 1, 2, 3, 4, 5, 6, 7 };

                JButton points_button = new JButton("Re-distribute Points");
                JButton cluster_button = new JButton("Cluster");
                JComboBox<String> algorithm_selection = new JComboBox<String>(algorithm_choices);
                JComboBox<String> initialization = new JComboBox<String>(initialization_choices);        
                JComboBox<Integer> num_clusters = new JComboBox<Integer>(clusters_choices);        
                JComboBox<Integer> min_points = new JComboBox<Integer>(points_choices);
                JTextField epsilon_text = new JTextField("80.0");

                algorithm_selection.setSelectedItem(chosen_algorithm);
                initialization.setSelectedItem(init_method);
                num_clusters.setSelectedItem(n_clusters);
                min_points.setSelectedItem(min_points_int);
                epsilon_text.setText(epsilon.toString());

                JLabel initialization_label = new JLabel("Initialization");
                JLabel num_clusters_label = new JLabel("Number of Clusters");
                JLabel min_points_label = new JLabel("Min Points");
                JLabel epsilon_label = new JLabel("Epsilon");

                panel.add(points_button);
                panel.add(algorithm_selection);
                panel.add(cluster_button);

                if (chosen_algorithm.equals("K-Means"))
                {
                    panel.add(initialization_label);
                    panel.add(initialization);
                    panel.add(num_clusters_label);
                    panel.add(num_clusters);
                }
                else
                {
                    panel.add(min_points_label);
                    panel.add(min_points);
                    panel.add(epsilon_label);
                    panel.add(epsilon_text);
                }
                frame.setVisible(true);
            }
        });

        
        // Clusters the points using the selected algorithm & parameters, and displays these clusters by coloring the points
        cluster_button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                ClusterAlgorithm clustering_algorithm;

                String chosen_algorithm = algorithm_selection.getSelectedItem().toString();
                Integer min_points_int = Integer.parseInt(min_points.getSelectedItem().toString());
                Double epsilon = Double.parseDouble(epsilon_text.getText());
                Integer n_clusters = Integer.parseInt(num_clusters.getSelectedItem().toString());
                String init_method = initialization.getSelectedItem().toString();

                // Apply the selected clustering algorithm
                if (chosen_algorithm.equals("DBSCAN"))
                {
                    clustering_algorithm = new DBSCAN();
                    clustering_algorithm.setAttributes(min_points_int, epsilon);
                }
                else if (chosen_algorithm.equals("K-Means"))
                {
                    clustering_algorithm = new KMeans();
                    clustering_algorithm.setAttributes(n_clusters, init_method);
                }
                else
                {
                    System.out.println("Using default DBSCAN algorithm. ");
                    clustering_algorithm = new DBSCAN();
                    clustering_algorithm.setAttributes(min_points_int, Double.parseDouble(epsilon_text.getText()));
                }

                clustering_algorithm.setCoordinates(coordinates);
                List<List<List<Double>>> clusters = clustering_algorithm.cluster();

                List<Color> colors = createClusterColors(clusters.size());
                List<List<Double>> clustered_points = new ArrayList<List<Double>>();

                // Changes the color of the points according to their cluster
                for (int i = 0; i < clusters.size(); i++)
                {
                    for (int j = 0; j < clusters.get(i).size(); j++)
                    {
                        clustered_points.add(clusters.get(i).get(j));
                        shapes.add(new ShapeItem(new Ellipse2D.Double(clusters.get(i).get(j).get(0), clusters.get(i).get(j).get(1), 15, 15), (colors.get(i))));
                    }
                }
                
                // Turns any unclustered points (noise) gray to show them as such
                for (int i = 0; i < coordinates.size(); i++)
                {
                    if (!clustered_points.contains(coordinates.get(i)))
                    {
                        shapes.add(new ShapeItem(new Ellipse2D.Double(coordinates.get(i).get(0), coordinates.get(i).get(1), 15, 15), Color.GRAY));
                    }
                }
                

                ClusterPanel panel = new ClusterPanel(shapes);
                frame.add(panel);

                // GUI elements need to be re-defined here for Swing to work correctly
                String algorithm_choices[] = { "DBSCAN", "K-Means" };
                String initialization_choices[] = { "K-Means++", "Random" };
                Integer clusters_choices[] = { 1, 2, 3, 4, 5, 6, 7 };
                Integer points_choices[] = { 1, 2, 3, 4, 5, 6, 7 };

                JButton points_button = new JButton("Re-distribute Points");
                JButton cluster_button = new JButton("Cluster");
                JComboBox<String> algorithm_selection = new JComboBox<String>(algorithm_choices);
                JComboBox<String> initialization = new JComboBox<String>(initialization_choices);        
                JComboBox<Integer> num_clusters = new JComboBox<Integer>(clusters_choices);        
                JComboBox<Integer> min_points = new JComboBox<Integer>(points_choices);
                JTextField epsilon_text = new JTextField("80.0");

                algorithm_selection.setSelectedItem(chosen_algorithm);
                initialization.setSelectedItem(init_method);
                num_clusters.setSelectedItem(n_clusters);
                min_points.setSelectedItem(min_points_int);
                epsilon_text.setText(epsilon.toString());

                JLabel initialization_label = new JLabel("Initialization");
                JLabel num_clusters_label = new JLabel("Number of Clusters");
                JLabel min_points_label = new JLabel("Min Points");
                JLabel epsilon_label = new JLabel("Epsilon");

                panel.add(points_button);
                panel.add(algorithm_selection);
                panel.add(cluster_button);

                if (chosen_algorithm.equals("K-Means"))
                {
                    panel.add(initialization_label);
                    panel.add(initialization);
                    panel.add(num_clusters_label);
                    panel.add(num_clusters);
                }
                else
                {
                    panel.add(min_points_label);
                    panel.add(min_points);
                    panel.add(epsilon_label);
                    panel.add(epsilon_text);
                }
                frame.setVisible(true);
            }
        });
    }

    
    // Class that defines a shape and its color - used for defining the shapes that represent points
    class ShapeItem
    {
        private Shape shape;
        private Color color;

        public ShapeItem(Shape shape, Color color) 
        {
            super();
            this.shape = shape;
            this.color = color;
        }

        public Shape getShape() 
        {
            return shape;
        }

        public void setShape(Shape shape) 
        {
            this.shape = shape;
        }

        public Color getColor() 
        {
            return color;
        }

        public void setColor(Color color) 
        {
            this.color = color;
        }
    }

    
    // Defines the panel which the GUI elements/shapes can be placed upon
    // Overrides methods in JPanel to allow the panel to display the defined shapes
    class ClusterPanel extends JPanel
    {
        private List<ShapeItem> shapes;

        public ClusterPanel(List<ShapeItem> shapesList) 
        {
            this.shapes = shapesList;
        }

        public void setShapes(List<ShapeItem> shapesList)
        {
            this.shapes = shapesList;
        }

        // Displays the shapes on the panel
        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            for (ShapeItem item : shapes) 
            {
                g2.setColor(item.getColor());
                g2.fill(item.getShape());
            }

            g2.dispose();
        }

        // Sets the size of the panel
        @Override
        public Dimension getPreferredSize() 
        {
            return new Dimension(350, 350);
        }
    }

    // Defines random n colors that will be used to represent the n clusters
    private static List<Color> createClusterColors(int n)
    {
        Random rand = new Random();
        List<Color> colors = new ArrayList<Color>();
        int i = 0;

        while (i < n)
        {
            colors.add(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
            i++;
        }

        return colors;
    }

    // Main method that runs the program
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run()
            {
                new ClusteringVisualiser(25); // The panel has 25 points that can be clustered
            }
        });
    }
}