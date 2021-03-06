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
public class GWizVertexView extends VertexView {
	
	private GWizModelAdapter jgAdapter;

	/**
	 */
	public static transient GWizVertexRenderer renderer = new GWizVertexRenderer();

	/**
	 */
	public GWizVertexView() {
		super();
	}

	/**
	 */
	public GWizVertexView(Object cell, GWizModelAdapter jgAdapter) {
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
		if (getModel()==null) return Color.BLUE.darker();
		if (getModel().isStart())
			return Color.ORANGE;
		if (getModel().isEnd())
			return Color.MAGENTA;
		if (getModel().isFixed())
			return Color.LIGHT_GRAY;
		if (getModel().isFixing())
			return Color.CYAN;
		if (getModel().isUpdatedDone())
			return Color.BLUE.brighter().brighter();
		if (getModel().isUpdated())
			return Color.RED;
		else return Color.BLUE.darker();
	}

}