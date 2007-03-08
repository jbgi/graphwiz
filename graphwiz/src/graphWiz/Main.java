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
	private ValPred TablValPred;
	private GraphWiz Graphwiz;
	
	public Main(){
	}
	
	public void init(){
		
		//Création de la fenetre principale
		this.frame = new Fenetre();
		
		//Création des panels
		JPanel Matrices = new JPanel();
		JPanel explanations = new JPanel();
		JPanel commentaires = new JPanel();
		JPanel menuderoulant = new JPanel();
		JPanel graph = new JPanel();
		
		//Génération de l'algorithme défilant de Dijsktra
		this.explications = new TextDijkstra();
		
		//Création du logo
		ImageIcon icon = new ImageIcon("image/logo.jpg","notre beau logo");
		JLabel image = new JLabel(icon);
		
		//Création du tableau de matrices
		this.TablValPred = new ValPred();
		
		//Création du menu de navigation
		menu = new Navigation();
		
		//Configuration de ce menu de navigation
		this.menu.setBackground(Color.blue);
		this.menu.setMinimumSize(new Dimension(675,40));
		
		//Création des séparations de panels
		JSplitPane Droitehaut = new JSplitPane(JSplitPane.VERTICAL_SPLIT,commentaires,menuderoulant);
		JSplitPane panegauchebas = new JSplitPane(JSplitPane.VERTICAL_SPLIT,graph,Matrices);
		JSplitPane panegauche = new JSplitPane(JSplitPane.VERTICAL_SPLIT,menu,panegauchebas);
		JSplitPane Panedroitebas = new JSplitPane(JSplitPane.VERTICAL_SPLIT,explanations,image);
		JSplitPane Panedroite = new JSplitPane(JSplitPane.VERTICAL_SPLIT,Droitehaut,Panedroitebas);
		JSplitPane Mainpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panegauche,Panedroite);
		
		//Création d'un bouton de choix d'algorithme
		JButton Choix = new JButton("Choix de l'algo");
		
		//Ajout du bouton dans le visuel
		menu.add(Choix);
		
		//Création des ActionListeners
		BoutonChoix myListener4 = new BoutonChoix();
		
		//Configurations du bouton
		Choix.addActionListener(myListener4);		

		//Configuration des différents panels
		Matrices.setBackground(Color.white);
		Matrices.setMaximumSize(new Dimension(675,250));
		Matrices.add(this.TablValPred.getTabbedPane());	
		explanations.add(explications.getAlgoDijkstra());
		explanations.setMinimumSize(new Dimension(425,500));
		explanations.setAutoscrolls(true);
		Droitehaut.setOneTouchExpandable(true);
		Droitehaut.setDividerLocation(200);
		
		//Configuration des séparation de panels
		panegauchebas.setOneTouchExpandable(true);
		Panedroitebas.setOneTouchExpandable(true);
		panegauche.setOneTouchExpandable(true);
		Panedroite.setOneTouchExpandable(true);
		Mainpane.setOneTouchExpandable(true);
		panegauche.setDividerLocation(75);
		panegauchebas.setDividerLocation(450);
		Mainpane.setDividerLocation(675);
		Panedroitebas.setDividerLocation(370);
		Panedroite.setDividerLocation(235);
		Mainpane.setPreferredSize(new Dimension(1100,750));

		// Création du logiciel de création de Graph et de son panel
		this.Graphwiz= new GraphWiz();

		//Ajout du logiciel de génération de graph au panel
		graph.add(Graphwiz);
		
		//Affichage de la frame principale
		frame.showOnFrame(Mainpane, "Graph Wiiiiiizz aller aller aller");
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
	}
}
