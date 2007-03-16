package graphWiz.widgets;

import java.awt.Font;

import javax.swing.JList;

public class TextDijkstra {

	private String[] algo = new String[22];
	private JList AlgoDijkstra;
	
	public TextDijkstra(){
		algo = new String[22];
		algo[0] =   "  ";
		algo[1] = "<html><I><font size=3><U> Notations:</U>"+
				"<br>V[x] = valuation du sommet x</br>" +
				"<br>W(x,y) = poids de l'arc (x,y)</br>" +
				"<BR> <center> *	*	*	*  </center></BR></font></I></html>";
		algo[2] = "<html><font size=4><U> <br>Algorithme: </br></U></font></html>";
		algo[4] =  "  ";
		algo[4] = "<html><font size=4><Br> Initialiser la valuation du sommet de départ à 0 </br></font></html>";
		algo[5] = "<html><blockquote><font size=4>et celle de tous les autres sommets à + &#8734 </font></blockquote></html>";
		algo[6] = "  ";
		algo[7] = "<html><font size=4>Tant que tous les sommets ne sont pas fixés</font></html>";
		algo[8] = "  ";
		algo[9] = "<html><blockquote><font size=4>Sélectionner le sommet x non fixé de plus petite valuation</font></blockquote></html>";
		algo[10] = "  ";
		algo[11] = "<html><blockquote><font size=4>Pour chaque successeur non fixé y de x </font></blockquote></html>";
		algo[12] = "  ";
		algo[13] = "<html><blockquote><blockquote><font size=4>Si  &quot V[x] + W(x,y)"+" &lt "+" v[y]&quot  alors &quot V[y] = V[x] + W(x,y)&quot</blockquote></blockquote></font></html>";
		algo[14] = "  ";
		algo[15] = "<html><blockquote><font size=4>FinPour</font></blockquote></html>";
		algo[16] = "  ";
		algo[17] = "<html><blockquote><font size=4>Fixer le sommet x</font></blockquote></html>";
		algo[18] =  "  ";
		algo[19] =  "<html><font size=4>Fin Tant Que</font></html>";
		algo[20] =  "  ";
		algo[21] =  "<html><font size=4>Fin de l'algorithme</font></html>";

		AlgoDijkstra = new JList();
		AlgoDijkstra.setListData(algo);
	}
	
	public JList getAlgoDijkstra(){
		return AlgoDijkstra;
	}
}
