package graphWiz.widgets;

import javax.swing.JList;

public class TextFloyd {

	private String[] algo = new String[27];
	private JList AlgoFloyd;
	
	public TextFloyd(){
		this.algo = new String[27];
		algo[0] = "  ";
		algo[1] = "<html><I><font size=3><U> Notations:</U>"+
				"<br>V[x] = valuation du sommet x</br>" +
				"<br>W(x,y) = poids de l'arc (x,y)</br>" +
				"<BR> <center> *	*	*	*  </center></BR></font></I></html>";
		algo[2] =  "<html><font size=4><U> <br>Algorithme: </br></U></font></html>";
		algo[4] = "  ";
		algo[4] = "<html><font size=4><Br> Initialiser la valuation du sommet de départ à 0 </br></font></html>";
		this.algo[5] = "  ";
		/**this.algo[13] = " ";
		this.algo[14] = "  ";
		this.algo[15] = "		Sélectionner le sommet x non fixé de plus petite valuation";
		this.algo[16] = "  ";
		this.algo[17] = "		Pour chaque successeur non fixé y de x ";
		this.algo[18] = "  ";
		this.algo[19] = "			Si V[x]+W(x,y)<v[y] alors V[y] = V(x)+W(x,y)";
		this.algo[20] = "  ";
		this.algo[21] = "		FinPour";
		this.algo[22] = "  ";
		this.algo[23] = "		Fixer le sommet x";
		this.algo[24] = "  ";
		this.algo[25] = "	Fin Tant Que";
		this.algo[26] = "Fin de l'algorithme";**/

		this.AlgoFloyd = new JList();
		this.AlgoFloyd.setListData(algo);
	}
	
	public JList getAlgoFloyd(){
		return this.AlgoFloyd;
	}
}
