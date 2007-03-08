package graphWiz;

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

import org.jgraph.example.GraphEd;

import widgets.BoutonChoix;
import widgets.Fenetre;
import widgets.Navigation;
import widgets.TextDijkstra;
import widgets.ValPred;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JApplet{

	private Navigation menu;
	private TextDijkstra explications;
	private ValPred TablValPred;
	private GraphEditor grapheditor;
	
	public Main()  {
		
		
		//Création de la fenetre principale
		setLayout(new BorderLayout());
		
		JPanel gauche = new JPanel();
		JPanel droite = new JPanel();
		gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
		droite.setLayout(new BoxLayout(droite, BoxLayout.Y_AXIS));
		//Création des panels
		JPanel matrices = new JPanel();
		JPanel explanations = new JPanel();
		JPanel commentaires = new JPanel();
		JPanel menuderoulant = new JPanel();
		
		//Génération de l'algorithme défilant de Dijsktra
		explications = new TextDijkstra();
		
		//Création du logo
		ImageIcon icon = new ImageIcon("src/graphWiz/resources/logo.jpg","notre beau logo");
		JLabel image = new JLabel(icon);
		
		//Création du tableau de matrices
		TablValPred = new ValPred();
		
		//Création du menu de navigation
		menu = new Navigation();
		//Configuration de ce menu de navigation
		menu.setBackground(Color.blue);
		menu.setMinimumSize(new Dimension(675,40));
		//Création d'un bouton de choix d'algorithme
		JButton Choix = new JButton("Choix de l'algo");
		//Ajout du bouton dans le visuel
		menu.add(Choix);
		
		
		matrices.setBackground(Color.white);
		matrices.setMaximumSize(new Dimension(675,250));
		matrices.add(TablValPred.getTabbedPane());
		
		explanations.add(explications.getAlgoDijkstra());
		explanations.setMinimumSize(new Dimension(425,500));
		explanations.setAutoscrolls(true);
		
		// Création du logiciel de création de Graph et de son panel
		grapheditor= new GraphEditor();
		
		gauche.add(grapheditor);
		gauche.add(matrices);
		droite.add(commentaires);
		droite.add(menu);
		droite.add(explanations);
		droite.add(image);
		add(gauche, BorderLayout.WEST);
		add(droite, BorderLayout.EAST);

		
		//Création des ActionListeners
		BoutonChoix myListener4 = new BoutonChoix();
		
		//Configurations du bouton
		Choix.addActionListener(myListener4);		
		
	}
	
	public static void main(String[] args) {
		
		// Construct Frame
		JFrame frame = new JFrame("Graph Wiiiiiizz aller aller aller");
		// Set Close Operation to Exit
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add an Editor Panel
		frame.getContentPane().add(new Main());
		// Fetch URL to Icon Resource
		URL jgraphUrl = GraphEd.class.getClassLoader().getResource(
				"graphWiz/resources/jgraph.gif");
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
