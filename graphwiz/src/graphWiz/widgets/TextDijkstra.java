package graphWiz.widgets;

import javax.swing.JList;

public class TextDijkstra {

	private String[] algo = new String[27];
	private JList AlgoDijkstra;
	
	public TextDijkstra(){
		algo = new String[27];
		algo[0] = "  ";
		algo[1] = "Notations: ";
		algo[2] = "  ";
		algo[3] = "	V[x] = valuation du sommet x";
		algo[4] = "  ";
		algo[5] = "	W(x,y) = poids de l'arc (x,y)";
		algo[6] = "  ";
		algo[7] = "    ";
		algo[8] = "Algorithme:";
		algo[9] = "  ";
		algo[10] = "	Initialiser la valuation du sommet ";
		algo[11] =	"   de départ à 0 et celle de tous les autres sommets à INFINI";
		algo[12] = "  ";
		algo[13] = "	Tant que tous les sommets ne sont pas fixés";
		algo[14] = "  ";
		algo[15] = "		Sélectionner le sommet x non fixé de plus petite valuation";
		algo[16] = "  ";
		algo[17] = "		Pour chaque successeur non fixé y de x ";
		algo[18] = "  ";
		algo[19] = "			Si V[x]+W(x,y)<v[y] alors V[y] = V(x)+W(x,y)";
		algo[20] = "  ";
		algo[21] = "		FinPour";
		algo[22] = "  ";
		algo[23] = "		Fixer le sommet x";
		algo[24] = "  ";
		algo[25] = "	Fin Tant Que";
		algo[26] = "Fin de l'algorithme";

		AlgoDijkstra = new JList();
		AlgoDijkstra.setListData(algo);
	}
	
	public JList getAlgoDijkstra(){
		return AlgoDijkstra;
	}
}
