package graphWiz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BoutonBellman implements ActionListener{
	
	public BoutonBellman(){
	}
	
	public void actionPerformed(ActionEvent evt) {
		JFrame frame2 = new JFrame();
		GraphWiz Graphwiz = new GraphWiz();
		 Graphwiz.init();
		 frame2.getContentPane().add(Graphwiz);
	     frame2.setTitle("JGraphT Adapter to JGraph Demo");
	     frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     frame2.pack();
	     frame2.setVisible(true);
	}

}
