package graphWiz.widgets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

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
	
	public ValPred(){
		
		super();
		
		panneau = Box.createHorizontalBox();
		
		//création des vecteurs
		String[] columnNames = {"1","2", "3", "4","5"}; 
        Object[][] val = {{"<html><font size=5> 0 </font></html>","<html><font size=5> &#8734 </font></html>","<html><font size=5> &#8734 </font></html>","<html><font size=5> &#8734 </font></html>","<html><font size=5> &#8734 </font></html>"}};
        Object[][] pred = {{"<html><font size=5> 0 </font></html>","<html><font size=5> &#150 </font></html>","<html><font size=5> &#150 </font></html>","<html><font size=5> &#150 </font></html>","<html><font size=5> &#150 </font></html>"}};
        final JTable tableVal = new JTable( val, columnNames);
        final JTable tablePred = new JTable( pred, columnNames);
        
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
        panneau.add(new JLabel("<html><font size=5>  VAL : </font></html>"));
        panneau.add(tableVal);
        panneau.add(new JLabel("<html><blockquote><font size=5> PRED : </font></blockquote></html>"));
        panneau.add(tablePred);  
        
        add(panneau);
        
	}
	
	
}
