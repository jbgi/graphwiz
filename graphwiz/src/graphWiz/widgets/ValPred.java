package graphWiz.widgets;
import graphWiz.model.GWizVertex;
import graphWiz.model.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

public class ValPred extends JPanel{

	Box panneau;
	String[] columnNames;
	Object[][] val;
	Object[][] pred;
	final JTable tableVal;
	final JTable tablePred;
	
	public ValPred(){
		
		super();
		
		panneau = Box.createHorizontalBox();
		
		//création des vecteurs
		//String[] columnNames = {"1","2", "3", "4","5"}; 
        //Object[][] val = {{"<html><font size=5> 0 </font></html>","<html><font size=5> &#8734 </font></html>","<html><font size=5> &#8734 </font></html>","<html><font size=5> &#8734 </font></html>","<html><font size=5> &#8734 </font></html>"}};
        //Object[][] pred = {{"<html><font size=5> 0 </font></html>","<html><font size=5> &#150 </font></html>","<html><font size=5> &#150 </font></html>","<html><font size=5> &#150 </font></html>","<html><font size=5> &#150 </font></html>"}};
        columnNames= new String[5];
        val = new Object[1][5];
        pred=new Object[1][5];
        val[0][0] = 0; pred[0][0]=0;columnNames[0]="1";
        val[0][1] = 0; pred[0][1]=0;columnNames[1]="2";
        val[0][2] = 0; pred[0][2]=0;columnNames[2]="3";
        val[0][3] = 0; pred[0][3]=0;columnNames[3]="4";
        val[0][4] = 0; pred[0][4]=0;columnNames[4]="5";
		tableVal = new JTable( val, columnNames);
        tablePred = new JTable( pred, columnNames);
        
        //mise en valeur des vecteurs
        tableVal.setBackground(Color.orange);
        tablePred.setBackground(Color.yellow);
        tableVal.setFocusable(false);
        tablePred.setFocusable(false);
        
        //dimensionnement des colonnes
        int vColIndex;
        int width = 25;
        for(vColIndex=0;vColIndex<tableVal.getColumnCount();vColIndex++){
        TableColumn colVal = tableVal.getColumnModel().getColumn(vColIndex);
        TableColumn colPred = tablePred.getColumnModel().getColumn(vColIndex);
        colVal.setPreferredWidth(width);
        colPred.setPreferredWidth(width);
        }
        panneau.add(new JLabel("<html><blockquote><font size=5>  VAL : </font></blockquote></html>"));
        panneau.add(tableVal);
        panneau.add(new JLabel("<html><blockquote><font size=5> PRED : </font></blockquote></html>"));
        panneau.add(tablePred);  
        
        add(panneau);
        
	}
	
	public void update(int NbSommet,GWizGraph graph){
        columnNames= new String[NbSommet];
        val = new Object[1][NbSommet];
        pred=new Object[1][NbSommet];
        int b=0;
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		while (i.hasNext()&&b<NbSommet){
			val[0][b] =((int)(i.next().getValuation()));
			pred[0][b] = i.next().getPred();
			columnNames[b]= Integer.toString(b);
			b++;
		}
	}
	
}
