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
	
	public Main(){
	}
	
    public static void main(String [] args){
		
		//Création de la fenetre principale
		Fenetre frame = new Fenetre();
		
		//Création des panels
		JPanel Matrices = new JPanel(new BorderLayout());
		JPanel explanations = new JPanel(new BorderLayout());
		JPanel commentaires = new JPanel(new BorderLayout());
		JPanel graph = new JPanel(new BorderLayout());
		
		//Génération de l'algorithme défilant de Dijsktra
		TextDijkstra explications = new TextDijkstra();
		
		//Création du logo
		Logo image = new Logo();
		
		//Création du tableau de matrices
		ValPred TablValPred = new ValPred();
		
		//Création du menu de navigation
		Navigation menu = new Navigation();
		
		//Configuration de ce menu de navigation
		menu.setBackground(Color.blue);
		menu.setMinimumSize(new Dimension(675,500));
		
		//Création des séparations de panels
		JSplitPane Droitehaut = new JSplitPane(JSplitPane.VERTICAL_SPLIT,commentaires,menu);
		JSplitPane panegauche = new JSplitPane(JSplitPane.VERTICAL_SPLIT,graph,Matrices);
		JSplitPane Panedroitebas = new JSplitPane(JSplitPane.VERTICAL_SPLIT,explanations,image);
		JSplitPane Panedroite = new JSplitPane(JSplitPane.VERTICAL_SPLIT,Droitehaut,Panedroitebas);
		JSplitPane Mainpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panegauche,Panedroite);
		
		/*inutile pour le moment
		//Création d'un bouton de choix d'algorithme
		JButton Choix = new JButton("Choix de l'algo");
		
		//Ajout du bouton dans le visuel
		menu.add(Choix);
		
		//Création des ActionListeners
		BoutonChoix myListener4 = new BoutonChoix();
		
		//Configurations du bouton
		Choix.addActionListener(myListener4);
		*/	

		//Configuration des différents panels
		Matrices.setBackground(Color.white);
		Matrices.add(TablValPred.getTabbedPane());
		Matrices.setMaximumSize(new Dimension(675,250));
		explanations.add(explications.getAlgoDijkstra());
		explanations.setMinimumSize(new Dimension(425,500));
		explanations.setAutoscrolls(true);
		Droitehaut.setOneTouchExpandable(true);
		Droitehaut.setDividerLocation(200);
		graph.setMinimumSize(new Dimension (675,500));
		
		//Configuration des séparation de panels
		Panedroitebas.setOneTouchExpandable(true);
		panegauche.setOneTouchExpandable(true);
		Panedroite.setOneTouchExpandable(true);
		Mainpane.setOneTouchExpandable(true);
		panegauche.setDividerLocation(500);
		Mainpane.setDividerLocation(675);
		Panedroitebas.setDividerLocation(370);
		Panedroite.setDividerLocation(235);
		Mainpane.setPreferredSize(new Dimension(1100,750));

		// Création du logiciel de création de Graph et de son panel
		GraphWiz Graphwiz= new GraphWiz();

		//Ajout du logiciel de génération de graph au panel
		graph.add(Graphwiz);
		
		//Affichage de la frame principale
		frame.showOnFrame(Mainpane, "Graph Wiiiiiizz aller aller aller");
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
	}
}
