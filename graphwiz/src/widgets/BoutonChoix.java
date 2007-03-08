package widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BoutonChoix implements ActionListener{

	public BoutonChoix(){
	}
	
	public void actionPerformed(ActionEvent evt) {
		JFrame frame2 = new JFrame();
		JPanel panel = new JPanel();
		
//		Création d'un bouton pour afficher l'algo de Floyd
		JButton Floyd = new JButton("Floyd");
		BoutonFloyd myListener1 = new BoutonFloyd();
		Floyd.addActionListener(myListener1);
		
		//Création d'un bouton pour afficher l'algo de Bellman a droite
		JButton Bellman = new JButton("Bellman");
		BoutonBellman myListener2 = new BoutonBellman();
		Bellman.addActionListener(myListener2);
		
		//Même chose pour Dijkstra
		JButton Dijkstra = new JButton("Dijkstra");
		BoutonDijkstra myListener3 = new BoutonDijkstra();
		Dijkstra.addActionListener(myListener3);
		
		//Ajout des boutons au visuel
		panel.add(Floyd);
		panel.add(Bellman);
		panel.add(Dijkstra);
		
		frame2.getContentPane().add(panel);
	    frame2.setTitle("JGraphT Adapter to JGraph Demo");
	    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame2.pack();
	    frame2.setVisible(true);
	}
}
