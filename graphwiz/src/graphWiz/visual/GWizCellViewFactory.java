package graphWiz.visual;
import org.jgraph.graph.*;
@SuppressWarnings("serial")
public class GWizCellViewFactory extends DefaultCellViewFactory {
	protected VertexView createVertexView(Object cell) {
		return new GWizVertexView(cell);
	}
}
