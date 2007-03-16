package graphWiz;

import graphWiz.model.Dijkstra;
import graphWiz.widgets.*;

import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.RandomAccessFile;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GraphWiz extends JApplet{

	private Navigation menu;
	private TextDijkstra explications;
	private ValPred TablValPred;
	private GraphEditor grapheditor;
	
	public GraphWiz()  {
		
		//Création de la fenetre principale
		setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
		JPanel gauche = new JPanel();
		JPanel droite = new JPanel();
		//gauche.setAutoscrolls(true);
		//droite.setAutoscrolls(true);
		droite.setPreferredSize(new Dimension(540,750));
		gauche.setPreferredSize(new Dimension(700,750));
		gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
		droite.setLayout(new BoxLayout(droite, BoxLayout.Y_AXIS));

		Commentaires commentaires = new Commentaires();		
		//Génération de l'algorithme défilant de Dijsktra
		explications = new TextDijkstra();
		
		//Création du logo
		URL graphWizUrl = GraphWiz.class.getClassLoader().getResource(
		"graphWiz/resources/logo.jpg");
		ImageIcon icon = new ImageIcon(graphWizUrl,"notre beau logo");
		JLabel image = new JLabel(icon);
		image.setMaximumSize(new Dimension(540,80));
		
		//Création du tableau de matrices
		TablValPred = new ValPred();
		
		// Création du logiciel de création de Graph et de son panel
		grapheditor= new GraphEditor();
		
		//Création du menu de navigation
		menu = new Navigation(grapheditor.getGraph());
		
		gauche.add(grapheditor);
		gauche.add(TablValPred);
		droite.add(commentaires.getComments());
		droite.add(menu);
		droite.add(explications.getAlgoDijkstra());
		droite.add(image);
		add(gauche);
		add(droite);		
	}
	
	public static void main(String[] args) {
		
		// Construct Frame
		JFrame frame = new JFrame("Graph Wiiiiiizz aller aller aller");
		// Set Close Operation to Exit
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add an Editor Panel
		frame.getContentPane().add(new GraphWiz());
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
	}
}
