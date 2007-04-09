package graphWiz.visual;

import graphWiz.GWizModelAdapter;
import graphWiz.model.GWizVertex;

import java.awt.Color;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.VertexView;

/**
 * @author  jbg
 */
@SuppressWarnings("serial")
public class GWizVertexValuationView extends VertexView {
	
	private GWizModelAdapter jgAdapter;

	/**
	 */
	public static transient GWizVertexValuationRenderer renderer = new GWizVertexValuationRenderer();

	/**
	 */
	public GWizVertexValuationView() {
		super();
	}

	/**
	 */
	public GWizVertexValuationView(Object cell, GWizModelAdapter jgAdapter) {
		super(cell);
		this.jgAdapter = jgAdapter;
	}

	/**
	 * @uml.property  name="renderer"
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}
	
	private GWizVertex getModel() {
		return jgAdapter.getCellVertex((DefaultGraphCell) getCell());
	}
	
	Color getColor(){
		if (getModel()==null) return Color.BLACK;
		if (getModel().hasPred())
			return Color.RED;
		else return Color.RED.darker();
	}

	public String getValuation() {
		if (getModel().isValuated()){
			double val = getModel().getValuation();
			if (val == Double.POSITIVE_INFINITY)
				return ("+\u221E");
				if (val == (int) val)
					return Integer.toString((int) val);
				else return Double.toString(val);
		}
		else return "";
	}

}