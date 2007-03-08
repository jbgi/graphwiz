package graphWiz;


import graphWiz.model.*;
import graphWiz.model.GWizEdge.Description;
import graphWiz.visual.GWizCellViewFactory;
import graphWiz.visual.SpringEmbeddedLayoutAlgorithm;
import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;
import javax.swing.*;
import org.jgraph.*;
import org.jgraph.graph.*;
import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

/**
 * A demo applet that shows how to use JGraph to visualize JGraphT graphs.
 * @author  Barak Naveh
 * @since  Aug 3, 2003
 */
public class Graphtest
    extends JApplet
{

    //~ Static fields/initializers --------------------------------------------

    private static final long serialVersionUID = 3256444702936019250L;
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    //~ Instance fields -------------------------------------------------------

    //
    private GWizModelAdapter jgAdapter;

    //~ Methods ---------------------------------------------------------------

    /**
     * An alternative starting point for this demo, to also allow running this
     * applet as an application.
     *
     * @param args ignored.
     */
    public static void main(String [] args)
    {
    	Graphtest applet = new Graphtest();
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("JGraphT Adapter to JGraph Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        // create a JGraphT graph
        GWizGraph g = new GWizGraph();

        GWizVertex v1 = new GWizVertex("1");

        GWizVertex v2 = new GWizVertex("2");
        GWizVertex v3 = new GWizVertex("3");
        GWizVertex v4 = new GWizVertex("4");
        GWizVertex v5 = new GWizVertex("5");
        
        // add some sample data (graph manipulated via JGraphT)
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);

        g.addEdge(v1, v2).setGWizGraph(g);
        g.addEdge(v1, v4).setGWizGraph(g);
        g.addEdge(v2, v3).setGWizGraph(g);
        g.addEdge(v3, v4).setGWizGraph(g);
        g.addEdge(v4, v2).setGWizGraph(g);
        g.addEdge(v3, v5).setGWizGraph(g);
        g.addEdge(v4, v5).setGWizGraph(g);
        
        g.setEdgeWeight(g.getEdge(v1, v2), 1);
        g.setEdgeWeight(g.getEdge(v1, v4), 5);
        g.setEdgeWeight(g.getEdge(v2, v3), 3);
        g.setEdgeWeight(g.getEdge(v3, v4), 0);
        g.setEdgeWeight(g.getEdge(v4, v2), 1);
        g.setEdgeWeight(g.getEdge(v3, v5), 2);
        g.setEdgeWeight(g.getEdge(v4, v5), 1);
        
        // create a visualization using JGraph, via an adapter
        jgAdapter = new GWizModelAdapter(g);
        Object[] test = new Object[] {"hello"};
        GraphConstants.setExtraLabels(jgAdapter.getEdgeCell(g.getEdge(v1, v2)).getAttributes(), test);
        
        JGraph jgraph = new JGraph(new GraphLayoutCache(jgAdapter, new GWizCellViewFactory(jgAdapter)));
        Point[] test1 = new Point[] {new Point(20,20)};
        GraphConstants.setExtraLabelPositions(jgAdapter.getEdgeCell(g.getEdge(v1, v2)).getAttributes(), test1);
        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);
        
        GraphConstants.setAutoSize(jgAdapter.getVertexCell(v1).getAttributes(), true);
        GraphConstants.setAutoSize(jgAdapter.getVertexCell(v2).getAttributes(), true);
        GraphConstants.setAutoSize(jgAdapter.getVertexCell(v3).getAttributes(), true);
        GraphConstants.setAutoSize(jgAdapter.getVertexCell(v4).getAttributes(), true);
        GraphConstants.setAutoSize(jgAdapter.getVertexCell(v5).getAttributes(), true);
        
        GraphConstants.setBackground(jgAdapter.getVertexCell(v1).getAttributes(), Color.BLACK);

        Vector<DefaultGraphCell> cellList = new Vector<DefaultGraphCell>();
        cellList.add(jgAdapter.getVertexCell(v1));
        cellList.add(jgAdapter.getVertexCell(v2));
        cellList.add(jgAdapter.getVertexCell(v3));
        cellList.add(jgAdapter.getVertexCell(v4));
        cellList.add(jgAdapter.getVertexCell(v5));
        
        SpringEmbeddedLayoutAlgorithm layout = new SpringEmbeddedLayoutAlgorithm();
        layout.setFrame(new Rectangle(DEFAULT_SIZE));
        //layout.setMaxIterations(-1);
        SpringEmbeddedLayoutAlgorithm.applyLayout(jgraph, layout, cellList.toArray());
        // that's all there is to it!...
        
        //jgraph.g
        g.getEdge(v1, v2).setDescription(Description.REGULAR);
        //((GWizEdge) jgAdapter.getValue(jgAdapter.getEdgeCell(g.getEdge(v1, v2)))).setDescription(Description.PATH);
        jgraph.graphDidChange();
        jgraph.repaint();
    }

    private void adjustDisplaySettings(JGraph jg)
    {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }

  }

