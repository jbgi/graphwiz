package graphWiz.widgets;

import graphWiz.GraphEditor;
import graphWiz.model.GWizEdge;
import graphWiz.model.GWizGraph;
import graphWiz.model.GWizRandomGraphGenerator;
import graphWiz.model.GWizVertex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class GWizGraphGeneratorDialog extends JDialog {
	
	private GWizGraph graph;

	private JTextField jnumVertice = new JTextField("5");

	private JTextField jnumEdges = new JTextField("7");
	
	private JTextField jmaxWeight = new JTextField("5");

	private JTextField jminWeight = new JTextField("0");

	private JCheckBox connected = new JCheckBox("",true);

	private int numEdges;

	private int numVertice;
	
	private GraphEditor editor;
	
	//RandomGraphGenerator<GWizVertex, GWizEdge> generator;
	
	public GWizGraphGeneratorDialog(GraphEditor graphEditor){
		super((Frame) null, "Générer un graphe", true);
		
		editor = graphEditor;
		graph = editor.getGwizGraph();
		JPanel panel = new JPanel(new GridLayout(6, 2, 4, 4));
		panel.add(new JLabel("Nombre de noeuds"));
		panel.add(jnumVertice);
		panel.add(new JLabel("Nombre d'arcs"));
		panel.add(jnumEdges);
		panel.add(new JLabel("Poids minimum"));
		panel.add(jminWeight);
		panel.add(new JLabel("Poids maximum"));
		panel.add(jmaxWeight);
		panel.add(new JLabel(""));
		panel.add(connected);
		connected.setVisible(false);
		
		JPanel panelBorder = new JPanel();
		panelBorder.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelBorder.add(panel);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
				.createEmptyBorder(16, 8, 8, 8)));

		JButton applyButton = new JButton("Générer");
		JButton closeButton = new JButton("Fermer");
		buttonPanel.add(closeButton);
		buttonPanel.add(applyButton);
		getRootPane().setDefaultButton(applyButton);

		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyValues();
			}
		});
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.startEdition();
				setVisible(false);
			}
		});

		getContentPane().add(panelBorder, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
		setResizable(false);
		// setLocationRelativeTo(parent);
	}
	
	private void applyValues() {
		setNumVertice(Integer.parseInt(this.jnumVertice.getText()));
		setNumEdges(Integer.parseInt(this.jnumEdges.getText()));
		GWizRandomGraphGenerator<GWizVertex, GWizEdge> generator = new GWizRandomGraphGenerator<GWizVertex, GWizEdge>(numVertice, numEdges, connected.isSelected(), Integer.parseInt(jminWeight.getText()), Integer.parseInt(jmaxWeight.getText()) );
		editor.getGraph().getModel().remove(editor.getGraph().getRoots());
		editor.getGraph().getModel().remove(editor.getGraph().getRoots());
		generator.generateGraph(graph, new GWizVertex("Vertex Generator"));
		editor.getGraph().setScale(7.0/Math.sqrt(numVertice));
		editor.applySpringLayout();
		editor.getGraph().clearSelection();
		editor.cellCount = 0;
	}
	
	public void defaultGenerator(){
		GWizRandomGraphGenerator<GWizVertex, GWizEdge> generator = new GWizRandomGraphGenerator<GWizVertex, GWizEdge>(5, 7, true, 0, 5);
		//editor.getGraph().getModel().remove(editor.getGraph().getRoots());
		//editor.getGraph().getModel().remove(editor.getGraph().getRoots());
		generator.generateGraph(graph, new GWizVertex("Vertex Generator"));
		editor.getGraph().setScale(7.0/Math.sqrt(5));
		editor.applySpringLayout();
		editor.getGraph().clearSelection();
	}
	
	/**
	 * @return Returns the numEdges.
	 */
	public int getNumEdges() {
		return numEdges;
	}

	/**
	 * @param numEdges
	 *            The numEdges to set.
	 */
	public void setNumEdges(int numEdges) {
		if (numEdges < 1) {
			numEdges = 1;
		} else if (numEdges > 300) {
			numEdges = 300;
		}
		this.numEdges = numEdges;
	}

	/**
	 * @return Returns the numVertice.
	 */
	public int getNumVertice() {
		return numVertice;
	}

	/**
	 * @param numVertice
	 *            The numVertice to set.
	 */
	public void setNumVertice(int numVertice) {
		if (numVertice < 1) {
			numVertice = 1;
		} else if (numVertice > 100) {
			numVertice = 100;
		}
		this.numVertice = numVertice;
	}

	/**
	 * Entry method for inserting a sample graph
	 * 
	 * @param graph
	 *            the JGraph to perform the insert on
	 * @param graphType
	 *            which sample graph type is to be inserted
	 * @param defaultVertexAttributes
	 *            the default attributes to use for vertices
	 * @param defaultEdgeAttributes
	 *            the default attributes to use for edges
	 */
	public void newRandomGraph() {
		editor.stopEdition();
		editor.getGraph().setMoveable(true);
		editor.getGraph().setEditable(true);
		
		this.setModal(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		this.setLocation(screenSize.width / 2 - (frameSize.width / 2),
				screenSize.height / 2 - (frameSize.height / 2));
		this.setVisible(true);
	}
}
