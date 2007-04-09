package graphWiz.visual;
import graphWiz.GWizModelAdapter;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;
/**
 * @author  jbg
 */
@SuppressWarnings("serial")
public class GWizCellViewFactory extends DefaultCellViewFactory {
	
	private GWizModelAdapter jgAdapter;
	
	public GWizCellViewFactory(GWizModelAdapter jgAdapter){
		super();
		this.jgAdapter=jgAdapter;
	}
	
	protected VertexView createVertexView(Object cell) {
		return new GWizVertexView(cell, jgAdapter);
		
	}
	
	/**
	 * Constructs an EdgeView view for the specified object.
	 */
	protected EdgeView createEdgeView(Object cell) {
			return new GWizEdgeView(cell, jgAdapter);
	}
}
