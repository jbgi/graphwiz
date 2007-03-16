package graphWiz.widgets;

import javax.swing.JList;

public class TextBellman {

	private String[] algo = new String[19];
	private JList AlgoBellman;
	
	public TextBellman(){
		algo = new String[19];
		algo[0] = "  ";
		algo[1] = "<html><I><font size=3><U> Notations:</U>"+
				"<br>V[x] = valuation du sommet x</br>" +
				"<br>W(x,y) = poids de l'arc (x,y)</br>" +
				"<BR> <center> *	*	*	*  </center></BR></font></I></html>";
		algo[2] =  "<html><font size=4><U> <br>Algorithme: </br></U></font></html>";
		algo[4] = "  ";
		algo[4] = "<html><font size=4><Br> Initialiser la valuation du sommet de départ à 0 </br></font></html>";
		algo[5] = "<html><blockquote><font size=4>et celle de tous les autres sommets à + &#8734 </font></blockquote></html>";
		algo[6] = "  ";
		algo[7] = "<html><font size=4>Tant que les valuations sont modifiées d'une itération sur l'autre ou que nbIter&ne;N-1 </font></html>";
		algo[8] = "  ";
		algo[9] = "<html><blockquote><font size=4>Incrémenter nbIter </font></blockquote></html>";
		algo[10] = "  ";
		algo[11] = "<html><blockquote><font size=4>Pour chaque sommet x de 1 à N</font></blockquote></html>";
		algo[12] = "  ";
		algo[13] = "<html><blockquote><blockquote><font size=4>V[x] = min(V[x],min(V[y]+W(y,x), y prédécesseur de x))</font></blockquote></blockquote></html>";
		algo[14] = "  ";
		algo[15] = "<html><blockquote><font size=4>Fin Pour</font></blockquote></html>";
		algo[16] = "  ";
		algo[17] = "<html><font size=4>Fin Tant que</font></html>";
		algo[18] = "<html><font size=4>Fin de l'algorithme</font></html>";
			
		this.AlgoBellman = new JList();
		this.AlgoBellman.setListData(algo);
	}
	
	public JList getAlgoBellman(){
		return this.AlgoBellman;
	}
}
