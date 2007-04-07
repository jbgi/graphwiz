package graphWiz.widgets;
import graphWiz.model.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ValPred extends JPanel{

	Box panneau;
	String[] columnNames;
	
	JTable tableVal;
	JTable tablePred;
    DefaultTableModel valData;
    DefaultTableModel predData;
    JScrollPane valPan;
    JScrollPane predPan;
	
	public ValPred(){
		
		panneau = Box.createHorizontalBox();
		panneau.setMaximumSize(new Dimension(400, 600 ));
		
        valData = new DefaultTableModel();
        predData = new DefaultTableModel();
		tableVal = new JTable(valData);
        tablePred = new JTable(predData);
        
        //mise en valeur des vecteurs
        tableVal.setBackground(Color.orange);
        tablePred.setBackground(Color.yellow);
        
        tableVal.setFocusable(false);
        tablePred.setFocusable(false);
        tablePred.setRowHeight(20);
        tableVal.setRowHeight(20);
        
        valPan = new JScrollPane(tableVal);
        valPan.setMaximumSize(new Dimension(200,450));
        valPan.setMinimumSize(new Dimension(100,45));
        valPan.setPreferredSize(new Dimension(200,100));
        valPan.setAutoscrolls(true);
        
        predPan = new JScrollPane(tablePred);
        predPan.setAutoscrolls(true);
        predPan.setMaximumSize(new Dimension(200,450));
        predPan.setMinimumSize(new Dimension(100,45));
        predPan.setPreferredSize(new Dimension(150,100));
        predPan.setAutoscrolls(true);
        
        panneau.add(new JLabel("<html><blockquote><font size=5>  VAL : </font></blockquote></html>"));
        panneau.add(valPan);
        panneau.add(new JLabel("<html><blockquote><font size=5> PRED : </font></blockquote></html>"));
        panneau.add(predPan);
        
        
        add(panneau);
        
	}
	
	public void update(GWizGraph graph, Algorithm algo){
		int NbSommet=graph.vertexSet().size();
        columnNames= new String[NbSommet];
        String floyd = "<html><font size=5>Algorithme de Floyd</font><br><br><I><font size=3><U> Notations:</U>"+
		"<br><font size=2>V[x,y] = valuation du plus court chemin pour aller de x à y </br>"+"<br> par les sommets intermédiaires {1,2,..,k} </br>" +
		"<br><font size=2>W(x,y) = poids de l'arc (x,y) (infini s'il n'existe pas)</br>" +
		"<br></font></I></html>";
        if(algo.getAlgo()[0]!= floyd){        	
        	valData.setColumnCount(NbSommet);
        	valData.setRowCount(1);
        	valPan.setSize(new Dimension((NbSommet)*30,50));
        	predData.setColumnCount(NbSommet);
        	predData.setRowCount(1);
        	predPan.setSize(new Dimension((NbSommet)*30,50));
        	int b=0;
        	Iterator<GWizVertex> i = graph.vertexSet().iterator();
        	while (i.hasNext()){
        		GWizVertex v = i.next();
        		if(v.getValuation() == Double.POSITIVE_INFINITY)
        			tableVal.getModel().setValueAt("<html><font size=5>&#8734</font></html>", 0, b);
        		else
        			tableVal.getModel().setValueAt("<html><font size=5>" + v.getValuation()+"</font></html>", 0, b);
        		if (v.hasPred() && v.getPred()!=null)
        			tablePred.getModel().setValueAt("<html><font size=5>" +v.getPred().getName()+ "</font></html>", 0, b);
        		columnNames[b]= v.getName();
        		b++;
        	}
        	valData.setColumnIdentifiers(columnNames);
        	predData.setColumnIdentifiers(columnNames);
        }
        else update2(graph,(Floyd) algo);
	}
	
	public void update2(GWizGraph graph, Floyd algo){
		System.out.println("updateFloyd");
		int NbSommet=graph.vertexSet().size();
		columnNames= new String[NbSommet+1];
		String[] columnNamesPred = new String[NbSommet+1];
        System.out.println("il y a "+NbSommet+" sommets");
        valData.setColumnCount(NbSommet+1);
        valData.setRowCount(NbSommet);
        predData.setColumnCount(NbSommet+1);
        predData.setRowCount(NbSommet);
        int i=0;
        columnNames[0]="<html>V<sup>"+Integer.toString(algo.getIteration())+"</sup></html>";
        columnNamesPred[0]="";
        for(int a=0;a<NbSommet;a++){
        	tableVal.getModel().setValueAt(""+a, a, 0);
        	tablePred.getModel().setValueAt(""+a, a, 0);
        	for(int b=1;b<NbSommet+1;b++){
        		if(algo.getVal()[a][b-1] == Double.POSITIVE_INFINITY)
        			tableVal.getModel().setValueAt("<html><font size=5>&#8734</font></html>", a, b);
        		else
        			tableVal.getModel().setValueAt("<html><font size=5>" + algo.getVal()[a][b-1]+"</font></html>", a, b);
        		tablePred.getModel().setValueAt("<html><font size=5>" +algo.getPred()[a][b-1]+ "</font></html>", a, b);
        	}
        	if(a>0){
        		columnNames[a]=""+i;
        		columnNamesPred[a]=""+i;
        		i++;
        	}
				
        }
        if(algo.getArrivee()<NbSommet && algo.getDepart()<NbSommet)
        	tableVal.getModel().setValueAt("<html><font size=4 color=#FF0000>"+algo.getVal()[algo.getDepart()][algo.getArrivee()]+"</font></html>", algo.getDepart(), algo.getArrivee()+1);
        columnNames[NbSommet]= ""+(NbSommet-1);
        columnNamesPred[NbSommet]=""+(NbSommet-1);
        valData.setColumnIdentifiers(columnNames);
        predData.setColumnIdentifiers(columnNamesPred);
	}
}

