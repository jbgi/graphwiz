package graphWiz;

import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.RandomAccessFile;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JApplet{

	private Fenetre frame;
	private Navigation menu;
	private TextDijkstra explications;
	private JPanel graph;
	private ValPred TablValPred;
	private GraphWiz Graphwiz;
	
	public Main(){
	}
	
	public void init(){
		
		//Création d'un bouton de choix d'algorithme
		JButton Choix = new JButton("Choix de l'algo");
		BoutonChoix myListener4 = new BoutonChoix();
		Choix.addActionListener(myListener4);
		
		this.frame = new Fenetre();
		
		//Création d'une image
		
		ImageIcon icon = new ImageIcon("image/logo.jpg","notre beau logo");
		JLabel image = new JLabel(icon);
		
		
		//Création des matrices Val et Pred
		
		this.TablValPred = new ValPred();
		
		//Création du panel associé
		JPanel Matrices = new JPanel();
		Matrices.setBackground(Color.white);
		Matrices.setMaximumSize(new Dimension(675,250));
		Matrices.add(this.TablValPred.getTabbedPane());
		
		
		//Création du menu d'explications de l'algorithme de Dijkstra
		
		this.explications = new TextDijkstra();
		JPanel explanations = new JPanel();
		
		explanations.add(explications.getAlgoDijkstra());
		explanations.setMinimumSize(new Dimension(425,500));
		explanations.setAutoscrolls(true);
		
		//Création du menu de droite
		JPanel commentaires = new JPanel();
		JPanel menuderoulant = new JPanel();
		JSplitPane Droitehaut = new JSplitPane(JSplitPane.VERTICAL_SPLIT,commentaires,menuderoulant);
		Droitehaut.setOneTouchExpandable(true);
		Droitehaut.setDividerLocation(200);
		
		// Création du menu
		
		menu = new Navigation();
		this.menu.setBackground(Color.blue);
		this.menu.setMinimumSize(new Dimension(675,40));
		menu.add(Choix);
		
		
		// Création du logiciel de création de Graph et de son panel
		this.Graphwiz= new GraphWiz();
		this.graph = new JPanel();
		graph.setMinimumSize(new Dimension(675,75));
		graph.add(Graphwiz);
		
		
		//Séparation des frames
		
		JSplitPane panegauchebas = new JSplitPane(JSplitPane.VERTICAL_SPLIT,graph,Matrices);
		panegauchebas.setOneTouchExpandable(true);
		panegauchebas.setDividerLocation(450);
		//panegauchebas.setPreferredSize(new Dimension(1000,750));
		
		JSplitPane panegauche = new JSplitPane(JSplitPane.VERTICAL_SPLIT,menu,panegauchebas);
		panegauche.setOneTouchExpandable(true);
		panegauche.setDividerLocation(75);
		//panegauche.setPreferredSize(new Dimension(1000,750));
	
		JSplitPane Panedroitebas = new JSplitPane(JSplitPane.VERTICAL_SPLIT,explanations,image);
		Panedroitebas.setOneTouchExpandable(true);
		Panedroitebas.setDividerLocation(370);
		//Panedroite.setMinimumSize(new Dimension(1000,750));
		
		JSplitPane Panedroite = new JSplitPane(JSplitPane.VERTICAL_SPLIT,Droitehaut,Panedroitebas);
		Panedroite.setOneTouchExpandable(true);
		Panedroite.setDividerLocation(235);
		
		JSplitPane Mainpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panegauche,Panedroite);
		Mainpane.setOneTouchExpandable(true);
		Mainpane.setDividerLocation(675);
		Mainpane.setPreferredSize(new Dimension(1100,750));
		

		
		frame.showOnFrame(Mainpane, "Graph Wiiiiiizz aller aller aller");
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
	}
}
