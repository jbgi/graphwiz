package graphWiz.widgets;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class ValPred{

	JTabbedPane tabbedPane;
	
	public ValPred(){

		String[] columnNames = {"1","2", "3", "4","5"}; 
        Object[][] val = {{0,"inf","inf","inf","inf"}};
        Object[][] pred = {{0,"-","-","-","-"}};
        final JTable tableVal = new JTable( val, columnNames);
        final JTable tablePred = new JTable( pred, columnNames);
        //tableVal.setPreferredScrollableViewportSize(new Dimension(500, 70));
        //tablePred.setPreferredScrollableViewportSize(new Dimension(500, 70));
        
        this.tabbedPane = new JTabbedPane();
        
        //JComponent panel1 = makeTextPanel("Panel #1");
        //panel1.add(tableVal);
        tabbedPane.addTab("VAL", null, tableVal,null);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
       // JComponent panel2 = makeTextPanel("Panel #2");
        //panel2.add(tablePred);
        tabbedPane.addTab("PRED", null, tablePred,null);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);      
	}
	
	public JTabbedPane getTabbedPane(){
		return this.tabbedPane;
	}
}
