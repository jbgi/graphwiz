package widgets;

import graphWiz.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BoutonDijkstra implements ActionListener{
	
	public BoutonDijkstra(){
	}
	
	public void actionPerformed(ActionEvent evt) {
		JFrame frame2 = new JFrame();
		Main main = new Main();
		main.init();
		frame2.getContentPane().add(main);
	    frame2.setTitle("On s'amuse comme on peut");
	    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame2.pack();
	    frame2.setVisible(true);
	}

}
