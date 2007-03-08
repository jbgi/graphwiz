package widgets;

import javax.swing.JList;

public class TextFloyd {

	private String[] algo = new String[27];
	private JList AlgoFloyd;
	
	public TextFloyd(){
		this.algo = new String[27];
		this.algo[0] = "  ";
		this.algo[1] = "Notations: ";
		this.algo[2] = "  ";
		this.algo[3] = "	V[x] = valuation du sommet x";
		this.algo[4] = "  ";
		this.algo[5] = "	W(x,y) = poids de l’arc (x,y)";
		this.algo[6] = "  ";
		this.algo[7] = "    ";
		this.algo[8] = "Algorithme:";
		this.algo[9] = "  ";
		this.algo[10] = "	Initialiser la valuation du sommet ";
		this.algo[11] =	"   de départ à 0 et celle de tous les autres sommets à INFINI";
		this.algo[12] = "  ";
		this.algo[13] = "	Tant que tous les sommets ne sont pas fixés";
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
		this.algo[26] = "Fin de l'algorithme";

		this.AlgoFloyd = new JList();
		this.AlgoFloyd.setListData(algo);
	}
	
	public JList getAlgoFloyd(){
		return this.AlgoFloyd;
	}
}
