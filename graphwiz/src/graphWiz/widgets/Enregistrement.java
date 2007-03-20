package graphWiz.widgets;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jgraph.JGraph;


public class Enregistrement extends JFrame{
	  
	JButton enregistrer;
    JButton annuler;
    JLabel messageEnregistrer;
    JPanel panneauNom;
    JTextField nomGraphe;
    JGraph graph;
    
	Enregistrement(JGraph g) {
		graph = g;
		this.setModal(true);
		this.setTitle("Enregistrement du graphe");
		this.setSize(350, 200);
		this.setLocationRelativeTo(null); // centrage de la fenêtre par rapport à l'écran
		this.setResizable(false);
		Container contenu = getContentPane();
		contenu.setLayout(new BoxLayout(contenu, defaultCloseOperation));

			
		panneauNom = new JPanel();
		panneauNom.add(new JLabel("Nom du graphe :"));
		nomGraphe = new JTextField("", 15);
		contenu.add(panneauNom);
		
		
		// crée les boutons
		enregistrer = new JButton("Enregistrer");
		annuler = new JButton("Annuler");
		contenu.add(enregistrer);
		contenu.add(annuler);
		enregistrer.requestFocus();
		
		// écoute les boutons
		enregistrer.addActionListener(new ActionListener(){
		    public void actionPerformed (ActionEvent ev){
		        try {
					enregistrer();
					JOptionPane.showMessageDialog(null, nomGraphe.getText()+ " est enregistrée.", 
							"Confirmation d'enregistrement", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
				finally {
					return;
				} 
		    }
		});
		annuler.addActionListener(new ActionListener(){
		    public void actionPerformed (ActionEvent ev){
		    dispose();
		    }
		});
		enregistrer.setSelected(true);
		enregistrer.addKeyListener(this);
		annuler.addKeyListener(this);
		nomPartie.addKeyListener(this);
	}
	
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			enregistrer.doClick();
		}
		else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			annuler.doClick();
		}
	}
	
	public void keyReleased(KeyEvent e) {
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	public void enregistrer() throws IOException {
		try{
			FileOutputStream save = new FileOutputStream(nomGraphe.getText());
			ObjectOutputStream oos = new ObjectOutputStream(save);
						
			oos.writeObject(g);
			oos.close();
            oos.flush();

			}
	    catch (java.io.IOException e) {
	      e.printStackTrace();
	    }
	}	
	
}
