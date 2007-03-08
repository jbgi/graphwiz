package graphWiz;

import javax.swing.JList;

public class TextBellman {

	private String[] algo = new String[27];
	private JList AlgoBellman;
	
	public TextBellman(){
		this.algo = new String[27];
		this.algo[0] = "  ";
		this.algo[1] = "Notations: ";
		this.algo[2] = "  ";
		this.algo[3] = "V[x] = la valuation du sommet x";
		this.algo[4] = "  ";
		this.algo[5] = "W(x,y) = le poids de l’arc (x,y)";
		this.algo[6] = "  ";
		this.algo[7] = "    ";
		this.algo[8] = "Algorithme:";
		this.algo[9] = "  ";
		this.algo[10] = "Initialisation de la valuation du sommet de départ à 0";
		this.algo[11] =	"et de tous les autres sommets à l'infini";
		this.algo[12] = "  ";
		this.algo[13] = "nbIter :=0";
		this.algo[14] = "  ";
		this.algo[15] = "Tant que les valuations sont modifiées d’une itération sur l’autre ou que nbIter != N-1 ";
		this.algo[16] = "  ";
		this.algo[17] = "Incrémenter nbIter ";
		this.algo[18] = "  ";
		this.algo[19] = "Pour chaque sommet x de 1 à N";
		this.algo[20] = "  ";
		this.algo[21] = "V[x] = min(V[x],  )";
		this.algo[22] = "  ";
		this.algo[23] = "Fin Pour";
		this.algo[24] = "  ";
		this.algo[25] = "Fin Tant que";
		this.algo[26] = "Fin de l'algorithme";
			
		this.AlgoBellman = new JList();
		this.AlgoBellman.setListData(algo);
	}
	
	public JList getAlgoBellman(){
		return this.AlgoBellman;
	}
}
