package graphWiz.widgets;

import graphWiz.GWizModelAdapter;
import graphWiz.GraphEditor;
import graphWiz.model.Algorithm;
import graphWiz.model.Dijkstra;
import graphWiz.visual.GWizEdgeView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.Border;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;


public class Navigation extends JPanel{
	
	protected ImageIcon begin, back, pause, play, forward,end;
	private String[] algos = {"Selectionner l'algorithme", "DIJKSTRA","BELLMAN","FLOYD"};
	private final JComboBox choixAlgo;
	protected JToolBar bHor; 
	private final GraphEditor graphEditor;
	private final JGraph jgraph;
	private Algorithm algo;
	private JList algoText = new JList();
	private JTextField commentaires = new JTextField(" Bienvenue sur GraphWiz ... Le simulateur d'algorithmes de graphes ...  ");
	//private Logo logo = new Logo();
	public Navigation(GraphEditor editor){
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		bHor = new JToolBar();
		this.graphEditor=editor;
		jgraph=this.graphEditor.getGraph();
		algo = new Dijkstra(((GWizModelAdapter) graphEditor.getGraph().getModel()).getGWizGraph());
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
				
			}
			if (choixAlgo.getSelectedIndex()==2){
				
			}
			if (choixAlgo.getSelectedIndex()==3){
				
			}
			}
        });
        
        commentaires.setPreferredSize(new Dimension(430,450));
        commentaires.setMaximumSize(new Dimension(430,500));
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
				// TODO Auto-generated method stub
				
			}});
        
        bHor.add(new AbstractAction("",play){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}}); 
        
        bHor.addSeparator();
        
        bHor.add(new AbstractAction("", forward) {
			public void actionPerformed(ActionEvent e) {
				algo.setStartingVertex(graphEditor.verticeTable.get(Integer.valueOf(0)));
				algo.nextStep();
				algoText.setSelectedIndex(algo.getCurrentStep());
				jgraph.repaint();
			}
		});
                
        bHor.addSeparator();

        bHor.add(new AbstractAction("",end){
        	public void actionPerformed(ActionEvent arg0) {
				while (!algo.isEnd())
					algo.nextStep();
				algoText.setSelectedIndex(algo.getCurrentStep());
				jgraph.repaint();
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
	}
	
}