package graphWiz.widgets;

import graphWiz.model.Algorithm;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;


public class Navigation {
	
	protected ImageIcon begin, back, pause, play, forward;
	protected JToolBar bHor; 
	private Algorithm algo;
	
	public Navigation(Algorithm algorithme){
		algo = algorithme;
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
			}
        });	
        //bHor.add(pause);
        //bHor.add(play);
        bHor.add(new AbstractAction("", forward) {
			public void actionPerformed(ActionEvent e) {
				algo.nextStep();
			}
		});
	}
	
	public JToolBar getNav() {
		return bHor;
	}
}