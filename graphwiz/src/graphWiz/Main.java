package graphWiz;

import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.applet.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

public class Main extends JApplet{

	private Fenetre frame;
	private JPanel panel;
	private Navigation menu;
	private ExplicationsDijkstra explications;
	private Graphtest graph;
	
	public void init(){
		
		this.frame = new Fenetre();
		this.panel = new JPanel();
		panel.setBackground(Color.pink);
		panel.setMinimumSize(new Dimension(675,75));
		
		//Création d'une image
		
		ImageIcon icon = new ImageIcon("image/logo.jpg","notre beau logo");
		JLabel image = new JLabel(icon);
		
		//Création des différents panels
		
		//Création du menu d'explications de l'algorithme de DIjkstra
		this.explications = new ExplicationsDijkstra();
		//this.explications.setMinimumSize(new Dimension(300,500));
		
		// Création du menu
		menu = new Navigation();
		this.menu.setBackground(Color.blue);
		this.menu.setMinimumSize(new Dimension(675,40));
		
		
		this.graph = new Graphtest();
		//panel4.setBackground(Color.green);
		graph.setMinimumSize(new Dimension(675,75));
		
		JPanel panel5 = new JPanel();
		panel5.setBackground(Color.white);
		panel5.setMinimumSize(new Dimension(675,250));
	
		JSplitPane panegauchetop = new JSplitPane(JSplitPane.VERTICAL_SPLIT,panel,menu);
		panegauchetop.setOneTouchExpandable(true);
		panegauchetop.setDividerLocation(75);
		//panegauchetop.setPreferredSize(new Dimension(1000,750));
		
		JSplitPane panegauchebas = new JSplitPane(JSplitPane.VERTICAL_SPLIT,graph,panel5);
		panegauchebas.setOneTouchExpandable(true);
		panegauchebas.setDividerLocation(400);
		//panegauchebas.setPreferredSize(new Dimension(1000,750));
		
		JSplitPane panegauche = new JSplitPane(JSplitPane.VERTICAL_SPLIT,panegauchetop,panegauchebas);
		panegauche.setOneTouchExpandable(true);
		panegauche.setDividerLocation(115);
		//panegauche.setPreferredSize(new Dimension(1000,750));
	
		JSplitPane Panedroite = new JSplitPane(JSplitPane.VERTICAL_SPLIT,explications,image);
		Panedroite.setOneTouchExpandable(true);
		Panedroite.setDividerLocation(630);
		//Panedroite.setMinimumSize(new Dimension(1000,750));
		
		JSplitPane Mainpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panegauche,Panedroite);
		Mainpane.setOneTouchExpandable(true);
		Mainpane.setDividerLocation(675);
		Mainpane.setPreferredSize(new Dimension(1000,750));
		

		
		frame.showOnFrame(Mainpane, "Graph Wiiiiiizz aller aller aller");
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
	}
}
