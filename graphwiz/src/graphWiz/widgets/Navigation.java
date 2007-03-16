package graphWiz.widgets;

import graphWiz.GWizModelAdapter;
import graphWiz.model.Algorithm;
import graphWiz.model.Dijkstra;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import org.jgraph.JGraph;


public class Navigation {
	
	protected ImageIcon begin, back, pause, play, forward;
	protected JToolBar bHor; 
	private final JGraph jgraph;
	private Algorithm algo;
	
	public Navigation(JGraph graph){
		jgraph = graph;
		algo = new Dijkstra(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		bHor = new JToolBar();
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
        //bHor.add(begin);
        bHor.add(new AbstractAction("", back) {
			public void actionPerformed(ActionEvent e) {
				algo.previousStep();
				jgraph.repaint();
			}
        });	
        //bHor.add(pause);
        //bHor.add(play);
        bHor.add(new AbstractAction("", forward) {
			public void actionPerformed(ActionEvent e) {
				algo.nextStep();
				jgraph.repaint();
			}
		});
	}
	
	public JToolBar getNav() {
		return bHor;
	}
}