package graphWiz.visual;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

/**
 * @author  jbg
 */
@SuppressWarnings("serial")
public class GWizVertexView extends VertexView {

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
	public GWizVertexView(Object cell) {
		super(cell);
	}

	/**
	 * @uml.property  name="renderer"
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}

}