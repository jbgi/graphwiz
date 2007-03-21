package graphWiz.widgets;

import graphWiz.GWizModelAdapter;
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
import java.awt.event.KeyEvent;

import javax.swing.*;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;


public class Navigation extends JPanel{
	
	protected ImageIcon begin, back, pause, play, forward;
	protected JToolBar bHor; 
	private final JGraph jgraph;
	private Algorithm algo;
	private JList algoText = new JList();
	private JTextField Commentaires = new JTextField(" Bienvenue sur GraphWiz ... Le simulateur d'algorithmes de graphes ...  ");
	//private Logo logo = new Logo();
	public Navigation(JGraph graph){
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(540,750));
		bHor = new JToolBar();
		jgraph = graph;
		algo = new Dijkstra(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
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
        
        add(Commentaires);
        
        bHor.add(new AbstractAction("",begin){
        	public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub	
			}});
        bHor.addSeparator(new Dimension(30,10));
       
        bHor.add(new AbstractAction("", back) {
			public void actionPerformed(ActionEvent e) {
				algo.previousStep();
				jgraph.repaint();
			}
        });	
        bHor.addSeparator(new Dimension(30,10));
       
        bHor.add(new AbstractAction("",pause){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
        bHor.addSeparator(new Dimension(30,10));
        
        bHor.add(new AbstractAction("", forward) {
			public void actionPerformed(ActionEvent e) {
				algo.nextStep();
				algoText.setSelectedIndex(algo.getCurrentStep());
				//Object[] edges = jgraph.getGraphLayoutCache().getCells(false, false, false, true);
				//for (int i = 0; i < edges.length; i++){
				//	GWizEdgeView revert = new GWizEdgeView(edges[i], 1, (GWizModelAdapter) jgraph.getGraphLayoutCache().getModel());
				//	GraphConstants.setLineBegin(revert.getAllAttributes(),GraphConstants.ARROW_DIAMOND);
				//	GraphConstants.setLineEnd(revert.getAllAttributes(),GraphConstants.ARROW_TECHNICAL);
				//	GraphConstants.setLineWidth(revert.getAllAttributes(), 1);
				//	GraphConstants.setLineColor(revert.getAllAttributes(), Color.GREEN);
				//	jgraph.getGraphLayoutCache().insertViews(new CellView[] {revert});
				//}
				jgraph.repaint();
			}
		});
                
        bHor.addSeparator(new Dimension(30,10));
       
        bHor.add(new AbstractAction("",play){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}}); 
        
        add(bHor);
        this.setRequestFocusEnabled(false);
        add(algoText);	
		//add(logo);
	}
	
}