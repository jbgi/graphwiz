package graphWiz.widgets;

import graphWiz.GWizModelAdapter;
import graphWiz.GraphEditor;
import graphWiz.model.Algorithm;
import graphWiz.model.Bellman;
import graphWiz.model.Dijkstra;
import graphWiz.model.Floyd;
import graphWiz.visual.GWizEdgeView;
import graphWiz.visual.GWizVertexValuationView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Timer;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;

import org.jgraph.JGraph;
import org.jgraph.graph.*;


public class Navigation extends JPanel{
	
	protected ImageIcon begin, back, pause, play, forward,end;
	private String[] algos = {"Selectionner l'algorithme", "DIJKSTRA","BELLMAN","FLOYD"};
	private JComboBox choixAlgo;
	protected JToolBar bHor; 
	private JGraph jgraph;
	public Algorithm algo;
	public JList algoText = new JList();
	public JTextField commentaires = new JTextField(" Bienvenue sur GraphWiz ... Le simulateur d'algorithmes de graphes ...  ");
	private Graphics valuations;
	private Algorithm dijkstra;
	private Algorithm bellman;
	private Algorithm floyd;
	private boolean pauseTimer = true;
	
	private Timer timer = new Timer();
	private TimerTask updateAlgo;
	
	private ValPred valPred;
	
	//private Logo logo = new Logo();
	public Navigation(){
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void start(JGraph editorGraph, ValPred valpred){
		
		valPred=valpred;
		bHor = new JToolBar();
		jgraph=editorGraph;
		dijkstra = new Dijkstra(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		bellman = new Bellman(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		floyd = new Floyd(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		algo = dijkstra;
		algoText.setListData(algo.getAlgo());
		algoText.setEnabled(false);
		algoText.setSelectionBackground(Color.CYAN);
		begin = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_begin.gif"));
        back = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_bf.gif"));
        pause = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_pause.gif"));
        play = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_play.gif"));
        forward = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_ff.gif"));
        end = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_end.gif"));
        
        choixAlgo = new JComboBox(algos);
        choixAlgo.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			
        	if(choixAlgo.getSelectedIndex()== 1){
				algo.retoreInitialState();
				algo = dijkstra;
				algoText.setListData(algo.getAlgo());
				algo.initialize();
				valPred.update(algo.getGraph(),algo);
				algoText.setSelectedIndex(algo.getCurrentStep());
			}
			if (choixAlgo.getSelectedIndex()==2){
				algo.retoreInitialState();
				algo = bellman;
				
				algoText.setListData(algo.getAlgo());
				algo.initialize();
				valPred.update(algo.getGraph(),algo);
				algoText.setSelectedIndex(algo.getCurrentStep());
			}
			if (choixAlgo.getSelectedIndex()==3){
				algo.retoreInitialState();
				algo = floyd;
				algoText.setListData(algo.getAlgo());
				algo.initialize();
				valPred.update(algo.getGraph(),algo);
				algoText.setSelectedIndex(algo.getCurrentStep());
			}
			}
        });
        
        commentaires.setPreferredSize(new Dimension(430,450));
        commentaires.setMaximumSize(new Dimension(430,500));
        commentaires.setEditable(false);
        commentaires.setBackground(Color.WHITE);
        add(commentaires);

        bHor.add(new AbstractAction("",begin){
        	public void actionPerformed(ActionEvent arg0) {
				algo.retoreInitialState();
				algoText.setSelectedIndex(algo.getCurrentStep());
				jgraph.repaint();
			}});
        bHor.addSeparator();
       
        bHor.add(new AbstractAction("", back) {
			public void actionPerformed(ActionEvent e) {
				algo.previousStep();
				algoText.setSelectedIndex(algo.getCurrentStep());
				jgraph.repaint();
			}
        });	
        bHor.addSeparator();
       
        bHor.add(new AbstractAction("",pause){

			public void actionPerformed(ActionEvent arg0) {
				if (updateAlgo!=null)
					updateAlgo.cancel();
			}
		});
        
        bHor.add(new AbstractAction("",play){

			public void actionPerformed(ActionEvent arg0) {
				if (updateAlgo!=null)
					updateAlgo.cancel();
		        updateAlgo = new TimerTask() {
				    public void run(){
				    	if (algo.isRunnable()){
							algo.nextStep();
							valPred.update(algo.getGraph(),algo);
							algoText.setSelectedIndex(algo.getCurrentStep());
							jgraph.repaint();
				    	}
				    	
				    }
		        };
				timer.scheduleAtFixedRate(updateAlgo, 1500, 1500);
			}}); 
        
        bHor.addSeparator();
        
        bHor.add(new AbstractAction("", forward) {
			public void actionPerformed(ActionEvent e) {
				algo.nextStep();
				algoText.setSelectedIndex(algo.getCurrentStep());
				valPred.update(algo.getGraph(),algo);
				jgraph.repaint();
			}
		});
                
        bHor.addSeparator();

        bHor.add(new AbstractAction("",end){
        	public void actionPerformed(ActionEvent arg0) {
        		if (algo.isRunnable()){
				while (!algo.isEnd())
					algo.nextStep();
				jgraph.repaint();
				valPred.update(algo.getGraph(),algo);
        		}
        		algoText.setSelectedIndex(algo.getCurrentStep());
			}});
        bHor.addSeparator();
        
        bHor.add(choixAlgo);
        
        bHor.setFloatable(false);
        bHor.setAlignmentY(200);
        Dimension bsize = new Dimension(450,30);
        bHor.setMaximumSize(bsize);
        bHor.setPreferredSize(bsize);
        add(bHor);
        JPanel jpAlgo = new JPanel();
        algoText.setPreferredSize(new Dimension(430,1000));
        algoText.setMaximumSize(new Dimension(430,2048));
        jpAlgo.setPreferredSize(new Dimension(450,1000));
        jpAlgo.setMaximumSize(new Dimension(450,2048));
        jpAlgo.setBackground(Color.WHITE);
        jpAlgo.add(algoText);
        add(jpAlgo);
		URL logoUrl = getClass().getClassLoader().getResource(
		"graphWiz/resources/logo.png");
		ImageIcon logo = new ImageIcon(logoUrl);
		JButton jLogo = new JButton(logo);
		jLogo.setAction(new AbstractAction("", logo) {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("fichier d'aide");
				JPanel panel = new JPanel();
				//panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
				Object[][] tableau = {{"Tuteurs","JUSSIEN Christelle"},
									  {" ","JUSSIEN Narendra"},
									  {"Chef de projet","RAJESSON Fanja"},
									  {"Responsable technique","GIRAUDEAU Jean-Baptiste"},
									  {"Membres de l'équipe","OLIVIER Luc"},
									  {" ","CANTU Paulina"},
									  {" ","REYES Felix"}};
				String[] Colonnes={"1","2"};
				JTable table = new JTable(tableau,Colonnes);
				panel.add(table);
		        frame.add(panel);
				frame.setSize(982, 625);
				frame.setVisible(true);
			}
		});
		JPanel jpLogo = new JPanel();
		Dimension lsize = new Dimension(450,120);
		jpLogo.setPreferredSize(lsize);
		jpLogo.setMaximumSize(lsize);
		//jLogo.setMinimumSize(lsize);
		jpLogo.add(jLogo);
		add(jpLogo);
		setRequestFocusEnabled(false);
		stopExplorer();
	}
	
	public void restart(JGraph editorGraph){
		jgraph=editorGraph;
		dijkstra = new Dijkstra(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		bellman = new Bellman(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		floyd = new Floyd(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		algo = dijkstra;
		algoText.setListData(algo.getAlgo());
	}

	public void stopExplorer() {
		for (int i = 0; i< (bHor.getComponentCount()-1);i++)
			bHor.getComponent(i).setEnabled(false);
		jgraph.getGraphLayoutCache().reload();
	}

	public void startExplorer() {
		for (int i = 0; i< (bHor.getComponentCount()-1);i++)
			bHor.getComponent(i).setEnabled(true);
		Iterator i = algo.getGraph().vertexSet().iterator();
		while (i.hasNext()){
			GWizVertexValuationView view = new GWizVertexValuationView(((GWizModelAdapter)jgraph.getModel()).getVertexCell(i.next()),(GWizModelAdapter)jgraph.getModel());
			jgraph.getGraphLayoutCache().insertViews(new GWizVertexValuationView[] {view});
			view.translate(0, -9);
		}
		algo.initialize();
		valPred.update(algo.getGraph(),algo);
		algoText.setSelectedIndex(algo.getCurrentStep());
	}

	
}