/*
 * @(#)GraphEd.java 3.3 23-APR-04
 * 
 * Copyright (c) 2001-2004, Gaudenz Alder All rights reserved.
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *  
 */
package graphWiz;

import graphWiz.model.GWizGraph;
import graphWiz.visual.GWizCellViewFactory;
import graphWiz.visual.SpringEmbeddedLayoutAlgorithm;
import graphWiz.widgets.GWizGraphGeneratorDialog;
import graphWiz.widgets.Navigation;
import graphWiz.widgets.ValPred;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;

//import com.jgraph.example.JGraphShadowBorder;
//import com.jgraph.example.GraphEdX.MyGraphModel;

public class GraphEditor extends JPanel implements GraphSelectionListener,
		KeyListener {
	
	public static final String VERSION = "GraphWiz 0.1";

    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    
    private SpringEmbeddedLayoutAlgorithm layout;
	
    private JScrollPane jSgraph;
	// JGraph instance
	protected JGraph graph;
	
	protected GWizGraphGeneratorDialog generatorDialog;

	private GWizModelAdapter jgAdapter;
	
	/**
	 * File chooser for loading and saving graphs. Note that it is lazily
	 * instaniated, always call initFileChooser before use.
	 */
	protected JFileChooser fileChooser = null;

	// Actions which Change State
	protected Action remove;
	
	private AbstractAction modeConstruction;
	
	private AbstractAction modeAlgo;
	
	private JToolBar toolbar;
	
	// cell count that gets put in cell label
	public int cellCount = 0;

	// Status Bar
	protected StatusBarGraphListener statusBar;

	//Mode
	private JComboBox mode;
	private Navigation navigation;
	private ValPred valPred;

	private GWizGraph gwizGraph;
	//
	// Editor Panel
	//

	// Construct an Editor Panel
	public GraphEditor(Navigation navigation, ValPred TablValPred) {
		this.valPred=TablValPred;
		this.navigation = navigation;
		
		// Construct the Graph
		graph = createGraph();
		// Use a Custom Marquee Handler
		graph.setMarqueeHandler(createMarqueeHandler());
		
        layout = new SpringEmbeddedLayoutAlgorithm();
        
		populateContentPane();

		installListeners(graph);
		
		graph.setScale(2.0);
		generatorDialog = new GWizGraphGeneratorDialog(this);
	}

	// Hook for subclassers
	protected void populateContentPane() {
		// Use Border Layout
		setLayout(new BorderLayout());
		// Add a ToolBar
		add(createToolBar(), BorderLayout.NORTH);
		jSgraph = new JScrollPane(graph);
		jSgraph.setPreferredSize(new Dimension(2048,2048));
		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,jSgraph, valPred);
		add(pane ,BorderLayout.CENTER); 	
	    double weight = 1D;
	    pane.setResizeWeight(weight);
	    // Split the space evenly
	    weight = 0.95D;
	    pane.setResizeWeight(weight);
	}

	// Hook for subclassers
	protected JGraph createGraph() {
		
		gwizGraph = new GWizGraph();
		
		jgAdapter = new GWizModelAdapter(gwizGraph, new AttributeMap(createCellAttributes(new Point(10,10))), new AttributeMap(createEdgeAttributes()));
		graph = new JGraph(new GraphLayoutCache(jgAdapter, new GWizCellViewFactory(jgAdapter)));
		// Make Ports Visible by Default
		graph.setPortsVisible(true);
		// Use the Grid (but don't make it Visible)
		graph.setGridEnabled(false);
		// Set the Grid Size to 10 Pixel
		graph.setGridSize(5);
		// Set the Tolerance to 2 Pixel
		graph.setTolerance(2);
		// Accept edits if click on background
		graph.setInvokesStopCellEditing(true);
		// Allows control-drag
		graph.setCloneable(true);
		// Jump to default port on connect
		graph.setJumpToDefaultPort(true);
		
        Color c = DEFAULT_BG_COLOR;

        graph.setBackground(c);
		
		return graph;
	}

	// Hook for subclassers
	protected void installListeners(JGraph graph) {
		// Add Listeners to Graph
		//
		// Update ToolBar based on Selection Changes
		graph.getSelectionModel().addGraphSelectionListener(this);
		// Listen for Delete Keystroke when the Graph has Focus
		graph.addKeyListener(this);
		graph.getModel().addGraphModelListener(statusBar);
	}

	// Hook for subclassers
	protected void uninstallListeners(JGraph graph) {
		graph.getSelectionModel().removeGraphSelectionListener(this);
		graph.removeKeyListener(this);
		graph.getModel().removeGraphModelListener(statusBar);
	}

	// Hook for subclassers
	protected BasicMarqueeHandler createMarqueeHandler() {
		return new MyMarqueeHandler();
	}

	// Insert a new Vertex at point
	public void insert(Point2D point) {
		if (graph.isEditable()){
		// Construct Vertex with no Label
		DefaultGraphCell vertex = createDefaultGraphCell();
		// Create a Map that holds the attributes for the Vertex
		vertex.getAttributes().applyMap(createCellAttributes(point));
		// Insert the Vertex (including child port and attributes)
		graph.getGraphLayoutCache().insert(vertex);
		}
	}

	// Hook for subclassers
	public Map createCellAttributes(Point2D point) {
		Map map = new Hashtable();
		// Snap the Point to the Grid
		if (graph != null) {
			point = graph.snap((Point2D) point.clone());
		} else {
			point = (Point2D) point.clone();
		}
		// Add a Bounds Attribute to the Map
		GraphConstants.setBounds(map, new Rectangle2D.Double(point.getX(),
				point.getY(), 0, 0));
		// Make sure the cell is resized on insert
		GraphConstants.setResize(map, true);
		// Add a nice looking gradient background
		//GraphConstants.setGradientColor(map, Color.blue);
		// Add a Border Color Attribute to the Map
		GraphConstants.setBorderColor(map, Color.black);
		// Add a White Background
		GraphConstants.setBackground(map, Color.black);
		GraphConstants.setForeground(map, Color.WHITE);
		// Make Vertex Opaque
		GraphConstants.setOpaque(map, true);
		// Auto-size cell
		GraphConstants.setAutoSize(map, true);
		
		return map;
	}

	// Hook for subclassers
	protected DefaultGraphCell createDefaultGraphCell() {
		DefaultGraphCell cell = new DefaultGraphCell(cellCount++);
		// Add one Floating Port
		cell.addPort();
		return cell;
	}

	// Insert a new Edge between source and target
	public void connect(Port source, Port target) {
		// Construct Edge with no label
		DefaultEdge edge = createDefaultEdge();
		if (graph.getModel().acceptsSource(edge, source)
				&& graph.getModel().acceptsTarget(edge, target)) {
			// Create a Map thath holds the attributes for the edge
			edge.getAttributes().applyMap(createEdgeAttributes());
			// Insert the Edge and its Attributes
			graph.getGraphLayoutCache().insertEdge(edge, source, target);
		}
	}

	// Hook for subclassers
	protected DefaultEdge createDefaultEdge() {
		return new DefaultEdge();
	}

	// Hook for subclassers
	public Map createEdgeAttributes() {
		Map map = new Hashtable();
		// Add a Line End Attribute
		GraphConstants.setLineEnd(map, GraphConstants.ARROW_TECHNICAL);
		GraphConstants.setLineWidth(map, 2);
		if (GraphConstants.DEFAULTFONT != null) {
			GraphConstants.setFont(map, GraphConstants.DEFAULTFONT
					.deriveFont(13f));
		}
		// Add a label along edge attribute
		GraphConstants.setLabelAlongEdge(map, true);
		return map;
	}

	// Returns the total number of cells in a graph
	protected int getCellCount(JGraph graph) {
		Object[] cells = graph.getDescendants(graph.getRoots());
		return cells.length;
	}
	//
	// Listeners
	//

	// From GraphSelectionListener Interface
	public void valueChanged(GraphSelectionEvent e) {
		// Update Button States based on Current Selection
		boolean enabled = !graph.isSelectionEmpty();
		remove.setEnabled(enabled);
	}

	//
	// KeyListener for Delete KeyStroke
	//
	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		// Listen for Delete Key Press
		if (e.getKeyCode() == KeyEvent.VK_DELETE)
			// Execute Remove Action on Delete Key Press
			remove.actionPerformed(null);
	}

	//
	// Custom MarqueeHandler

	// MarqueeHandler that Connects Vertices and Displays PopupMenus
	public class MyMarqueeHandler extends BasicMarqueeHandler {

		// Holds the Start and the Current Point
		protected Point2D start, current;

		// Holds the First and the Current Port
		protected PortView port, firstPort;

		// Override to Gain Control (for PopupMenu and ConnectMode)
		public boolean isForceMarqueeEvent(MouseEvent e) {
			if (e.isShiftDown())
				return false;
			// If Right Mouse Button we want to Display the PopupMenu
			if (SwingUtilities.isRightMouseButton(e))
				// Return Immediately
				return true;
			// Find and Remember Port
			port = getSourcePortAt(e.getPoint());
			// If Port Found and in ConnectMode (=Ports Visible)
			if (port != null && graph.isPortsVisible())
				return true;
			if (!graph.isEditable())
				return true;
			// Else Call Superclass
			return super.isForceMarqueeEvent(e);
		}

		// Display PopupMenu or Remember Start Location and First Port
		public void mousePressed(final MouseEvent e) {
			// If Right Mouse Button
			if (SwingUtilities.isRightMouseButton(e) && graph.isEditable()) {
				// Find Cell in Model Coordinates
				Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
				// Create PopupMenu for the Cell
				JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
				// Display PopupMenu
				menu.show(graph, e.getX(), e.getY());
				// Else if in ConnectMode and Remembered Port is Valid
			} else if (port != null && graph.isPortsVisible() && graph.isEditable()) {
				// Remember Start Location
				start = graph.toScreen(port.getLocation());
				// Remember First Port
				firstPort = port;
			} else if (!graph.isEditable() && navigation.algo.isEnd()){
				Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
				navigation.algo.setEndVertex(jgAdapter.getCellVertex((DefaultGraphCell) cell));
				navigation.updateAlgo();
			} else if (!graph.isEditable() && navigation.algo.isStart()){
			Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
			navigation.algo.setStartingVertex(jgAdapter.getCellVertex((DefaultGraphCell) cell));
			navigation.updateAlgo();
			
			}
			
			else {
				// Call Superclass
				super.mousePressed(e);
			}
			
		}

		// Find Port under Mouse and Repaint Connector
		public void mouseDragged(MouseEvent e) {
			// If remembered Start Point is Valid
			if (start != null) {
				// Fetch Graphics from Graph
				Graphics g = graph.getGraphics();
				// Reset Remembered Port
				PortView newPort = getTargetPortAt(e.getPoint());
				// Do not flicker (repaint only on real changes)
				if (newPort == null || newPort != port) {
					// Xor-Paint the old Connector (Hide old Connector)
					paintConnector(Color.black, graph.getBackground(), g);
					// If Port was found then Point to Port Location
					port = newPort;
					if (port != null)
						current = graph.toScreen(port.getLocation());
					// Else If no Port was found then Point to Mouse Location
					else
						current = graph.snap(e.getPoint());
					// Xor-Paint the new Connector
					paintConnector(graph.getBackground(), Color.black, g);
				}
			}
			// Call Superclass
			super.mouseDragged(e);
		}

		public PortView getSourcePortAt(Point2D point) {
			// Disable jumping
			graph.setJumpToDefaultPort(false);
			PortView result;
			try {
				// Find a Port View in Model Coordinates and Remember
				result = graph.getPortViewAt(point.getX(), point.getY());
			} finally {
				graph.setJumpToDefaultPort(true);
			}
			return result;
		}

		// Find a Cell at point and Return its first Port as a PortView
		protected PortView getTargetPortAt(Point2D point) {
			// Find a Port View in Model Coordinates and Remember
			return graph.getPortViewAt(point.getX(), point.getY());
		}

		// Connect the First Port and the Current Port in the Graph or Repaint
		public void mouseReleased(MouseEvent e) {
			// If Valid Event, Current and First Port
			if (e != null && port != null && firstPort != null
					&& firstPort != port) {
				// Then Establish Connection
				connect((Port) firstPort.getCell(), (Port) port.getCell());
				e.consume();
				// Else Repaint the Graph
			} else
				graph.repaint();
			// Reset Global Vars
			firstPort = port = null;
			start = current = null;
			// Call Superclass
			super.mouseReleased(e);
		}

		// Show Special Cursor if Over Port
		public void mouseMoved(MouseEvent e) {
			// Check Mode and Find Port
			if (e != null && getSourcePortAt(e.getPoint()) != null
					&& graph.isPortsVisible()) {
				// Set Cusor on Graph (Automatically Reset)
				graph.setCursor(new Cursor(Cursor.HAND_CURSOR));
				// Consume Event
				// Note: This is to signal the BasicGraphUI's
				// MouseHandle to stop further event processing.
				e.consume();
			} else
				// Call Superclass
				super.mouseMoved(e);
		}

		// Use Xor-Mode on Graphics to Paint Connector
		protected void paintConnector(Color fg, Color bg, Graphics g) {
			// Set Foreground
			g.setColor(fg);
			// Set Xor-Mode Color
			g.setXORMode(bg);
			// Highlight the Current Port
			paintPort(graph.getGraphics());
			// If Valid First Port, Start and Current Point
			if (firstPort != null && start != null && current != null)
				// Then Draw A Line From Start to Current Point
				g.drawLine((int) start.getX(), (int) start.getY(),
						(int) current.getX(), (int) current.getY());
		}

		// Use the Preview Flag to Draw a Highlighted Port
		protected void paintPort(Graphics g) {
			// If Current Port is Valid
			if (port != null) {
				// If Not Floating Port...
				boolean o = (GraphConstants.getOffset(port.getAllAttributes()) != null);
				// ...Then use Parent's Bounds
				Rectangle2D r = (o) ? port.getBounds() : port.getParentView()
						.getBounds();
				// Scale from Model to Screen
				r = graph.toScreen((Rectangle2D) r.clone());
				// Add Space For the Highlight Border
				r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r
						.getHeight() + 6);
				// Paint Port in Preview (=Highlight) Mode
				graph.getUI().paintCell(g, port, r, true);
			}
		}

	} // End of Editor.MyMarqueeHandler

	//
	//
	//

	//
	// PopupMenu and ToolBar
	//

	//
	//
	//

	//
	// PopupMenu
	//
	public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
		JPopupMenu menu = new JPopupMenu();
		if (cell != null) {
			// Edit
			menu.add(new AbstractAction("Edit") {
				public void actionPerformed(ActionEvent e) {
					graph.startEditingAtCell(cell);
					
				}
			});
		}
		// Remove
		if (!graph.isSelectionEmpty()) {
			menu.addSeparator();
			menu.add(new AbstractAction("Remove") {
				public void actionPerformed(ActionEvent e) {
					navigation.commentaires.setText("Cliquez sur un sommet pour le supprimer \n vous supprimerez aussi les arcs associés");
					remove.actionPerformed(e);
				}
			});
		}
		menu.addSeparator();
		// Insert
		menu.add(new AbstractAction("Insert") {
			public void actionPerformed(ActionEvent ev) {
				insert(pt);
				navigation.commentaires.setText("Vous venez d'ajouter un sommet \n double-cliquez sur lui pour changer son nom");
				
			}
		});
		return menu;
	}

	//
	// ToolBar
	//
	public JToolBar createToolBar() {
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		//Open file
		URL openUrl = getClass().getClassLoader().getResource(
		"graphWiz/resources/open.gif");
		ImageIcon openIcon = new ImageIcon(openUrl);
		toolbar.add(new AbstractAction("Open graph file", openIcon) {
			public void actionPerformed(ActionEvent e) {
				deserializeGraph();
				navigation.commentaires.setText("Sélectionner le graphe à éditer");
				
			}
		});
		
		//Save graph
		URL saveUrl = getClass().getClassLoader().getResource(
		"graphWiz/resources/save.gif");
		ImageIcon saveIcon = new ImageIcon(saveUrl);
		toolbar.add(new AbstractAction("Save graph", saveIcon) {
			public void actionPerformed(ActionEvent e) {
				serializeGraph();
				navigation.commentaires.setText("Entrez le nom du graphe courant à enregistrer");
				
			}
		});
		
		//New Random Graph
		URL randomUrl = getClass().getClassLoader().getResource(
		"graphWiz/resources/tree.gif");
		ImageIcon randomIcon = new ImageIcon(randomUrl);
		toolbar.add(new AbstractAction("Insert Random Graph", randomIcon) {
			public void actionPerformed(ActionEvent e) {
				navigation.commentaires.setText("Entrez les paramètres du graphe");
				generatorDialog.newRandomGraph();
				navigation.commentaires.setText("Mode Edition. Pour démarrer un algorithme, cliquez sur Mode Algorithme");
				navigation.commentaires.setBackground(Color.white);
				startEdition();
			}
		});
		
		toolbar.addSeparator();

		// Insert
		URL insertUrl = getClass().getClassLoader().getResource(
				"graphWiz/resources/insert.gif");
		ImageIcon insertIcon = new ImageIcon(insertUrl);
		toolbar.add(new AbstractAction("", insertIcon) {
			public void actionPerformed(ActionEvent e) {
				insert(new Point(10, 10));
				navigation.commentaires.setText("Vous pouvez déplacer le sommet en maintenant le click sur une extrémité");
				navigation.commentaires.setBackground(Color.white);
			}
		});

		// Toggle Connect Mode
		URL connectUrl = getClass().getClassLoader().getResource(
				"graphWiz/resources/connecton.gif");
		ImageIcon connectIcon = new ImageIcon(connectUrl);
		toolbar.add(new AbstractAction("", connectIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setPortsVisible(!graph.isPortsVisible());
				navigation.commentaires.setText("Pour connecter 2 sommets, cliquez aux centres des cercles");
				URL connectUrl;
				if (graph.isPortsVisible())
					connectUrl = getClass().getClassLoader().getResource(
							"graphWiz/resources/connectoff.gif");
				else
					connectUrl = getClass().getClassLoader().getResource(
							"graphWiz/resources/connecton.gif");
				ImageIcon connectIcon = new ImageIcon(connectUrl);
				navigation.commentaires.setText("Pour donner un poids à l'arc, double-cliquez sur celui-ci");
				putValue(SMALL_ICON, connectIcon);
			}
		});

		// Remove
		URL removeUrl = getClass().getClassLoader().getResource(
				"graphWiz/resources/delete.gif");
		ImageIcon removeIcon = new ImageIcon(removeUrl);
		remove = new AbstractAction("", removeIcon) {
			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty()) {
					Object[] cells = graph.getSelectionCells();
					cells = graph.getDescendants(cells);
					graph.getModel().remove(cells);
					navigation.commentaires.setText("Cliquez sur un sommet pour le supprimer");
				}
			}
		};
		remove.setEnabled(false);
		toolbar.add(remove);

		// Zoom Std
		toolbar.addSeparator();
		URL zoomUrl = getClass().getClassLoader().getResource(
				"graphWiz/resources/zoom.gif");
		ImageIcon zoomIcon = new ImageIcon(zoomUrl);
		toolbar.add(new AbstractAction("", zoomIcon) {
			public void actionPerformed(ActionEvent e) {
				navigation.commentaires.setText("Le graphe est zoomé à la tailler de la fenêtre");				
				graph.setScale(7.0/Math.sqrt(((GWizModelAdapter) graph.getModel()).getGWizGraph().vertexSet().size()));
			}
		});
		// Zoom In
		URL zoomInUrl = getClass().getClassLoader().getResource(
				"graphWiz/resources/zoomin.gif");
		ImageIcon zoomInIcon = new ImageIcon(zoomInUrl);
		toolbar.add(new AbstractAction("", zoomInIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setScale(1.5 * graph.getScale());
				navigation.commentaires.setText("Zoom avant du graphe");
				
			}
		});
		// Zoom Out
		URL zoomOutUrl = getClass().getClassLoader().getResource(
				"graphWiz/resources/zoomout.gif");
		ImageIcon zoomOutIcon = new ImageIcon(zoomOutUrl);
		toolbar.add(new AbstractAction("", zoomOutIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setScale(graph.getScale() / 1.5);
				navigation.commentaires.setText("Zoom arrière du graphe");
				
			}
		});

		// Layout Algorithm
		toolbar.addSeparator();
		URL springLayoutUrl = getClass().getClassLoader().getResource(
				"graphWiz/resources/expandAll.gif");
		ImageIcon layoutIcon = new ImageIcon(springLayoutUrl);
		toolbar.add(new AbstractAction("", layoutIcon) {
			public void actionPerformed(ActionEvent e) {
				applySpringLayout();
				navigation.commentaires.setText("Arrangement de la disposition de votre graphe");
				
			}
		});
		toolbar.addSeparator();
		
		//Mode Algo, Mode Dessin
		modeConstruction = new AbstractAction("Mode Construction") {
			public void actionPerformed(ActionEvent e) {
				navigation.commentaires.setText("Vous pouvez modifier votre graphe ou en créer un nouveau");
				navigation.commentaires.setBackground(Color.green);
				this.setEnabled(false);
				modeAlgo.setEnabled(true);
				startEdition();
				navigation.stopExplorer();
				navigation.inhiberChoixAlgo();
				navigation.algo.retoreInitialState();
			}
		};
		
		modeConstruction.setEnabled(false);
		
		modeAlgo = new AbstractAction("Mode Algorithme") {
			public void actionPerformed(ActionEvent e) {
				this.setEnabled(false);
				modeConstruction.setEnabled(true);
				stopEdition();
				navigation.startExplorer();
				navigation.activerChoixAlgo();
				navigation.commentaires.setBackground(Color.yellow);
			}
		};
		
		modeAlgo.setEnabled(true);
		
		toolbar.add(modeConstruction);
		
		toolbar.add(modeAlgo);
		
		toolbar.addSeparator();
		
		//Help
		URL helpUrl = getClass().getClassLoader().getResource(
		"graphWiz/resources/help.jpg");
		ImageIcon helpIcon = new ImageIcon(helpUrl);
		toolbar.add(new AbstractAction("", helpIcon) {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("fichier d'aide");
				JPanel panel = new JPanel();
				
				panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
				
				JLabel JText = new JLabel("<html><blockquote><font size=5>   ...   Description des boutons de GRAPHWIZ   ... </font></blockquote></html>");
		        panel.add(JText);
		        panel.add(new JLabel("     "));
		        ImageIcon begin = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/btn_begin.gif"));
				JLabel Jbegin = new JLabel("<html><font size=4 color=#2B65EC><I>Begin:</I> Recommence la simulation</font></html>",begin,JLabel.LEFT);
		        panel.add(Jbegin);
		        panel.add(new JLabel("     "));
		        ImageIcon back = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/btn_bf.gif"));
				JLabel Jback = new JLabel("<html><font size=4 color=#2B65EC><I>Back Forward:</I> Retour à l'étape précédente</font></html>",back,JLabel.LEFT);
		        panel.add(Jback);
		        panel.add(new JLabel("     "));
		        ImageIcon pause = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/btn_pause.gif"));
				JLabel JPause = new JLabel("<html><font size=4 color=#2B65EC><I>Pause:</I> Arrête la simulation </font></html>",pause,JLabel.LEFT);
		        panel.add(JPause);
		        panel.add(new JLabel("     "));
		        ImageIcon play = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/btn_play.gif"));
				JLabel JPlay = new JLabel("<html><font size=4 color=#2B65EC><I>Play:</I> Lance la simulation continue</font></html>",play,JLabel.LEFT);
		        panel.add(JPlay);
		        panel.add(new JLabel("     "));
		        ImageIcon forward = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/btn_ff.gif"));
				JLabel JForward = new JLabel("<html><font size=4 color=#2B65EC><I>Fast Forward:</I> Etape suivante</font></html>",forward,JLabel.LEFT);
		        panel.add(JForward);
		        panel.add(new JLabel("     "));
		        ImageIcon end = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/btn_end.gif"));
				JLabel JEnd = new JLabel("<html><font size=4 color=#2B65EC><I>End:</I> Exécute l'algorithme en une étape</font></html>",end,JLabel.LEFT);
		        panel.add(JEnd);
		        panel.add(new JLabel("     "));
		        ImageIcon connect = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/connecton.gif"));
				JLabel JConnect = new JLabel("<html><font size=4 color=#2B65EC><I>Connect Off:</I> Déplacement unique des sommets sans ajout de flèches possible</font></html>",connect,JLabel.LEFT);
		        panel.add(JConnect);
		        panel.add(new JLabel("     "));
		        ImageIcon connectoff = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/connectoff.gif"));
				JLabel JConnectoff = new JLabel("<html><font size=4 color=#2B65EC><I>Connect On:</I> Permet de connecter les sommets en ajoutant des fl�ches entre elles, et de donner une valuation en double cliquant sur une flèche</font></html>",connectoff,JLabel.LEFT);
		        panel.add(JConnectoff);
		        panel.add(new JLabel("     "));
		        ImageIcon delete = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/delete.gif"));
				JLabel JDelete = new JLabel("<html><font size=4 color=#2B65EC><I>Delete:</I> Effacer des sommets en un click après avoir selectionn� le bouton</font></html>",delete,JLabel.LEFT);
		        panel.add(JDelete);
		        panel.add(new JLabel("     "));
		        ImageIcon help = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/help.jpg"));
				JLabel JHelp = new JLabel("<html><font size=4 color=#2B65EC><I>Help:</I> Ouvre une page d'aide (page en cours)</font></html>",help,JLabel.LEFT);
		        panel.add(JHelp);
		        panel.add(new JLabel("     "));
		        ImageIcon expand = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/expandAll.gif"));
				JLabel JExpand = new JLabel("<html><font size=4 color=#2B65EC><I>Expand All:</I> Affiche le graphe é la taille de l'éditeur et arrange la disposition aléatoirement</font></html>",expand,JLabel.LEFT);
		        panel.add(JExpand);
		        panel.add(new JLabel("     "));
		        ImageIcon insert = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/insert.gif"));
				JLabel JInsert= new JLabel("<html><font size=4 color=#2B65EC><I>Insert:</I> Introduit un nouveau sommet dans l'espace d'édition</font></html>",insert,JLabel.LEFT);
		        panel.add(JInsert);
		        panel.add(new JLabel("     "));
		        ImageIcon zoom = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/zoom.gif"));
				JLabel JZoom = new JLabel("<html><font size=4 color=#2B65EC><I>Zoom:</I> Affiche le graphe à la taille de l'écran</font></html>",zoom,JLabel.LEFT);
		        panel.add(JZoom);
		        panel.add(new JLabel("     "));
		        ImageIcon zoomin = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/zoomin.gif"));
		        JLabel JZoomin = new JLabel("<html><font size=4 color=#2B65EC><I>Zoom In:</I> Zoom avant sur le graphe</font></html>",zoomin,JLabel.LEFT);
		        panel.add(JZoomin);
		        panel.add(new JLabel("     "));
		        ImageIcon zoomout = new ImageIcon(GraphEditor.class.getClassLoader().getResource("graphWiz/resources/zoomout.gif"));
				JLabel JZoomout = new JLabel("<html><font size=4 color=#2B65EC><I>Zoom Out:</I> Zoom arri�re du graphe</font></html>",zoomout,JLabel.LEFT);
		        panel.add(JZoomout);
		        panel.add(new JLabel("     "));
		        
		        
		        frame.add(panel);
				frame.setSize(800,690);
				frame.setVisible(true);
				navigation.commentaires.setText("Nous espérons que la rubrique vous aura été utile");
				
			}
		});
		
		return toolbar;
	}
	
	public void applySpringLayout(){
		if (graph.isEditable()){
	        layout.setFrame(new Rectangle((int) (jSgraph.getWidth()/graph.getScale()),
	        		(int) (jSgraph.getHeight()/graph.getScale())));
	        layout.setScale(graph.getScale());
	        double i = graph.getScale();
	        graph.setScale(1);
	        SpringEmbeddedLayoutAlgorithm.applyLayout(graph, layout, graph.getRoots());
	        graph.setScale(i);
		}
	}

	/**
	 * @return Returns the graph.
	 */
	public JGraph getGraph() {
		return graph;
	}

	/**
	 * @param graph
	 *            The graph to set.
	 */
	public void setGraph(JGraph graph) {
		this.graph = graph;
	}

	// This will change the source of the actionevent to graph.
	public class EventRedirector extends AbstractAction {

		protected Action action;

		// Construct the "Wrapper" Action
		public EventRedirector(Action a) {
			super("", (ImageIcon) a.getValue(Action.SMALL_ICON));
			this.action = a;
		}

		// Redirect the Actionevent
		public void actionPerformed(ActionEvent e) {
			e = new ActionEvent(graph, e.getID(), e.getActionCommand(), e
					.getModifiers());
			action.actionPerformed(e);
		}
	}

	/**
	 * 
	 * @return a String representing the version of this application
	 */
	protected String getVersion() {
		return VERSION;
	}

	public class StatusBarGraphListener extends JPanel implements GraphModelListener {

		/**
		 * Graph Model change event
		 */
		public void graphChanged(GraphModelEvent e) {
			updateStatusBar();
		}

		protected void updateStatusBar(){
			
		}
	}

	/**
	 * @return Returns the remove.
	 */
	public Action getRemove() {
		return remove;
	}

	/**
	 * @param remove
	 *            The remove to set.
	 */
	public void setRemove(Action remove) {
		this.remove = remove;
	}

	public GWizGraph getGwizGraph() {
		return gwizGraph;
	}
	
	public void startEdition() {
		graph.setPortsVisible(true);
		graph.setEditable(true);
		graph.setMoveable(true);
		graph.setSelectionEnabled(true);
		for (int i = 0; i< (toolbar.getComponentCount()-4);i++)
			toolbar.getComponent(i).setEnabled(true);
	}

	public void stopEdition() {
		graph.clearSelection();
		graph.stopEditing();
		graph.setPortsVisible(false);
		graph.setEditable(false);
		graph.setMoveable(false);
		graph.setSelectionEnabled(false);
		for (int i = 0; i< (toolbar.getComponentCount()-4);i++)
			toolbar.getComponent(i).setEnabled(false);
	}
	
	public void serializeGraph() {
		int returnValue = JFileChooser.CANCEL_OPTION;
		initFileChooser();
		returnValue = fileChooser.showSaveDialog(graph);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			Container parent = graph.getParent();
			BasicMarqueeHandler marquee = graph.getMarqueeHandler();
			graph.setMarqueeHandler(null);
			try {
				// Serializes the graph by removing it from the component
				// hierarchy and removing all listeners from it. The marquee
				// handler, begin an inner class of GraphEd, is not marked
				// serializable and will therefore not be stored. This must
				// be taken into account when deserializing a graph.
				uninstallListeners(graph);
				parent.remove(graph);
				ObjectOutputStream out = new ObjectOutputStream(
						new BufferedOutputStream(new FileOutputStream(
								fileChooser.getSelectedFile())));
				out.writeObject(graph);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(graph, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				// Adds the component back into the component hierarchy
				graph.setMarqueeHandler(marquee);
				if (parent instanceof JViewport) {
					JViewport viewPort = (JViewport) parent;
					viewPort.setView(graph);
				} else {
					// Best effort...
					parent.add(graph);
				}
				// And reinstalls the listener
				installListeners(graph);
			}
		}
	}

	public void deserializeGraph() {
		int returnValue = JFileChooser.CANCEL_OPTION;
		initFileChooser();
		returnValue = fileChooser.showOpenDialog(graph);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			Container parent = graph.getParent();
			BasicMarqueeHandler marqueeHandler = graph.getMarqueeHandler();
			try {
				uninstallListeners(graph);
				parent.remove(graph);
				ObjectInputStream in = new ObjectInputStream(
						new BufferedInputStream(new FileInputStream(fileChooser
								.getSelectedFile())));
				graph = (JGraph) in.readObject();
				// Take the marquee handler from the original graph and
				// use it in the new graph as well.
				graph.setMarqueeHandler(marqueeHandler);
				// Adds the component back into the component hierarchy
				if (parent instanceof JViewport) {
					JViewport viewPort = (JViewport) parent;
					viewPort.setView(graph);
				} else {
					// Best effort...
					parent.add(graph);
				}
				// graph.setMarqueeHandler(previousHandler);
				// And reinstalls the listener
				installListeners(graph);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(graph, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			navigation.restart(graph);
			jgAdapter = (GWizModelAdapter) graph.getModel();
			gwizGraph = jgAdapter.getGWizGraph();
		}
	}

	/**
	 * Utility method that ensures the file chooser is created. Start-up time
	 * is improved by lazily instaniating choosers.
	 *
	 */
	protected void initFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			FileFilter fileFilter = new FileFilter() {
				/**
				 * @see javax.swing.filechooser.FileFilter#accept(File)
				 */
				public boolean accept(File f) {
					if (f == null)
						return false;
					if (f.getName() == null)
						return false;
					if (f.getName().endsWith(".gwz"))
						return true;
					if (f.isDirectory())
						return true;

					return false;
				}

				/**
				 * @see javax.swing.filechooser.FileFilter#getDescription()
				 */
				public String getDescription() {
					return "Fichier GraphWiz (.gwz)";
				}
			};
			fileChooser.setFileFilter(fileFilter);
		}
	}
	
}
