package graphWiz.widgets;

import javax.swing.JTextField;

public class Commentaires{
	JTextField Commentaires;
	
	public Commentaires(){
		Commentaires = new JTextField(" Bienvenue sur GraphWiz ... Le simulateur d'algorithmes de graphes ...  ");
	}
	
	public JTextField getComments(){
		return Commentaires;
	}
}
