package graphWiz.widgets;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Commentaires{
	JTextField Commentaires;
	
	public Commentaires(){
		Commentaires = new JTextField("Commentaires, pau, felix, fanja");
	}
	
	public JTextField getComments(){
		return Commentaires;
	}
}
