package graphWiz;

import graphWiz.widgets.Navigation;
import graphWiz.widgets.ValPred;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphWiz extends JApplet{

	private Navigation menu;
	private ValPred TablValPred;
	private GraphEditor grapheditor;
	
	public GraphWiz()  {
		
		//Création de la fenetre principale
		setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
		JPanel gauche = new JPanel();
		//gauche.setAutoscrolls(true);
		gauche.setPreferredSize(new Dimension(2048,2048));
		gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
		
		//Création du tableau de matrices
		TablValPred = new ValPred();
		
		// Création du logiciel de création de Graph et de son panel
		
		//Création du menu de navigation
		
		
		menu = new Navigation();
		grapheditor= new GraphEditor(menu,TablValPred);
		menu.start(grapheditor.getGraph(), TablValPred);
		this.menu.inhiberChoixAlgo();
		gauche.add(grapheditor);
		add(gauche);
		add(menu);
		
	}
	
	public static void main(String[] args) {
		
		// Construct Frame
		JFrame frame = new JFrame("GRAPHWIZ : Simulateur d'Algorithmes de Graphes - Projet OSE FI3 2007");
		// Set Close Operation to Exit
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add an Editor Panel
		GraphWiz gw = new GraphWiz();
		frame.getContentPane().add(gw);
		// Fetch URL to Icon Resource
		URL jgraphUrl = GraphWiz.class.getClassLoader().getResource(
				"graphWiz/resources/logo.png");
		// If Valid URL
		if (jgraphUrl != null) {
			// Load Icon
			ImageIcon jgraphIcon = new ImageIcon(jgraphUrl);
			// Use in Window
			frame.setIconImage(jgraphIcon.getImage());
		}
		// Set Default Size
		frame.setSize(1100, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Show Frame
		frame.setVisible(true);
		gw.grapheditor.generatorDialog.defaultGenerator();
	}
}
