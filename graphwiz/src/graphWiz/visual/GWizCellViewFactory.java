package graphWiz.visual;
import org.jgraph.graph.*;
@SuppressWarnings("serial")
public class GWizCellViewFactory extends DefaultCellViewFactory {
	protected VertexView createVertexView(Object cell) {
		return new GWizVertexView(cell);
	}
	
	/**
	 * Constructs an EdgeView view for the specified object.
	 */
	protected EdgeView createEdgeView(Object cell) {
			return new GWizEdgeView(cell);
	}
}
