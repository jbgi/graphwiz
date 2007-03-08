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
	private JPanel panel;
	private Navigation menu;
	private TextDijkstra explications;
	private Graphtest graph;
	private ValPred TablValPred;
	
	public void init(){
		
		//Création d'un bouton de lancement de l'éditeur
		JButton Editeur = new JButton("Editeur");
		CréationGraph myListener1 = new CréationGraph();
		Editeur.addActionListener(myListener1);
		
		this.frame = new Fenetre();
		this.panel = new JPanel();
		panel.setBackground(Color.pink);
		panel.setMinimumSize(new Dimension(675,75));
		
		//Ajout du bouton au panel
		panel.add(Editeur);
		
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
		
		//Création des différents panels
		
		
		//Création du menu d'explications de l'algorithme de Dijkstra
		
		this.explications = new TextDijkstra();
		JPanel explanations = new JPanel();
		
		explanations.add(explications.getAlgoDijkstra());
		explanations.setMinimumSize(new Dimension(425,500));
		
		
		// Création du menu
		
		menu = new Navigation();
		this.menu.setBackground(Color.blue);
		this.menu.setMinimumSize(new Dimension(675,40));
		
		
		// Création du panel servant au graph
		
		this.graph = new Graphtest();
		graph.setMinimumSize(new Dimension(675,75));
		

	
		JSplitPane panegauchetop = new JSplitPane(JSplitPane.VERTICAL_SPLIT,panel,menu);
		panegauchetop.setOneTouchExpandable(true);
		panegauchetop.setDividerLocation(75);
		//panegauchetop.setPreferredSize(new Dimension(1000,750));
		
		JSplitPane panegauchebas = new JSplitPane(JSplitPane.VERTICAL_SPLIT,graph,Matrices);
		panegauchebas.setOneTouchExpandable(true);
		panegauchebas.setDividerLocation(400);
		//panegauchebas.setPreferredSize(new Dimension(1000,750));
		
		JSplitPane panegauche = new JSplitPane(JSplitPane.VERTICAL_SPLIT,panegauchetop,panegauchebas);
		panegauche.setOneTouchExpandable(true);
		panegauche.setDividerLocation(115);
		//panegauche.setPreferredSize(new Dimension(1000,750));
	
		JSplitPane Panedroite = new JSplitPane(JSplitPane.VERTICAL_SPLIT,explanations,image);
		Panedroite.setOneTouchExpandable(true);
		Panedroite.setDividerLocation(630);
		//Panedroite.setMinimumSize(new Dimension(1000,750));
		
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
