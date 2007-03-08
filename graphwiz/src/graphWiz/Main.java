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
		
		//Cr�ation de la fenetre principale
		Fenetre frame = new Fenetre();
		
		//Cr�ation des panels
		JPanel Matrices = new JPanel(new BorderLayout());
		JPanel explanations = new JPanel(new BorderLayout());
		JPanel commentaires = new JPanel(new BorderLayout());
		JPanel graph = new JPanel(new BorderLayout());
		
		//G�n�ration de l'algorithme d�filant de Dijsktra
		TextDijkstra explications = new TextDijkstra();
		
		//Cr�ation du logo
		Logo image = new Logo();
		
		//Cr�ation du tableau de matrices
		ValPred TablValPred = new ValPred();
		
		//Cr�ation du menu de navigation
		Navigation menu = new Navigation();
		
		//Configuration de ce menu de navigation
		menu.setBackground(Color.blue);
		menu.setMinimumSize(new Dimension(675,500));
		
		//Cr�ation des s�parations de panels
		JSplitPane Droitehaut = new JSplitPane(JSplitPane.VERTICAL_SPLIT,commentaires,menu);
		JSplitPane panegauche = new JSplitPane(JSplitPane.VERTICAL_SPLIT,graph,Matrices);
		JSplitPane Panedroitebas = new JSplitPane(JSplitPane.VERTICAL_SPLIT,explanations,image);
		JSplitPane Panedroite = new JSplitPane(JSplitPane.VERTICAL_SPLIT,Droitehaut,Panedroitebas);
		JSplitPane Mainpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panegauche,Panedroite);
		
		/*inutile pour le moment
		//Cr�ation d'un bouton de choix d'algorithme
		JButton Choix = new JButton("Choix de l'algo");
		
		//Ajout du bouton dans le visuel
		menu.add(Choix);
		
		//Cr�ation des ActionListeners
		BoutonChoix myListener4 = new BoutonChoix();
		
		//Configurations du bouton
		Choix.addActionListener(myListener4);
		*/	

		//Configuration des diff�rents panels
		Matrices.setBackground(Color.white);
		Matrices.add(TablValPred.getTabbedPane());
		Matrices.setMaximumSize(new Dimension(675,250));
		explanations.add(explications.getAlgoDijkstra());
		explanations.setMinimumSize(new Dimension(425,500));
		explanations.setAutoscrolls(true);
		Droitehaut.setOneTouchExpandable(true);
		Droitehaut.setDividerLocation(200);
		graph.setMinimumSize(new Dimension (675,500));
		
		//Configuration des s�paration de panels
		Panedroitebas.setOneTouchExpandable(true);
		panegauche.setOneTouchExpandable(true);
		Panedroite.setOneTouchExpandable(true);
		Mainpane.setOneTouchExpandable(true);
		panegauche.setDividerLocation(500);
		Mainpane.setDividerLocation(675);
		Panedroitebas.setDividerLocation(370);
		Panedroite.setDividerLocation(235);
		Mainpane.setPreferredSize(new Dimension(1100,750));

		// Cr�ation du logiciel de cr�ation de Graph et de son panel
		GraphWiz Graphwiz= new GraphWiz();

		//Ajout du logiciel de g�n�ration de graph au panel
		graph.add(Graphwiz);
		
		//Affichage de la frame principale
		frame.showOnFrame(Mainpane, "Graph Wiiiiiizz aller aller aller");
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
	}
}
