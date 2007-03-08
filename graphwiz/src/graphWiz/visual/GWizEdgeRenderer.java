/*
 * @(#)EdgeRenderer.java	1.0 03-JUL-04
 * 
 * Copyright (c) 2001-2005 Gaudenz Alder
 *  
 * See LICENSE file in distribution for licensing details of this source file
 */
package graphWiz.visual;

import graphWiz.GWizModelAdapter;
import graphWiz.model.GWizEdge;
import graphWiz.model.GWizEdge.Description;
import graphWiz.model.GWizGraph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.UIManager;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.util.Bezier;
import org.jgraph.util.Spline2D;
import org.jgrapht.ext.JGraphModelAdapter;

/**
 * This renderer displays entries that implement the CellView interface.
 * @version  1.0 1/1/02
 * @author  Gaudenz Alder
 */

public class GWizEdgeRenderer extends EdgeRenderer {

	/**
	 * Constructs a renderer that may be used to render edges.
	 */
	public GWizEdgeRenderer() {
		super();
	}
	
	/**
	 * Paint the renderer.
	 */
	public void paint(Graphics g) {
		if (view.isLeaf()) {
			Shape edgeShape = view.getShape();
			// Sideeffect: beginShape, lineShape, endShape
			if (edgeShape != null) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
						RenderingHints.VALUE_STROKE_PURE);
				int c = BasicStroke.CAP_BUTT;
				int j = BasicStroke.JOIN_MITER;
				setOpaque(false);
				//super.paint(g);
				translateGraphics(g);
				g.setColor(((GWizEdgeView) view).getEdgeColor());
				if (lineWidth > 0) {
					g2.setStroke(new BasicStroke(lineWidth, c, j));
					if (gradientColor != null && !preview) {
						g2.setPaint(new GradientPaint(0, 0, getBackground(),
								getWidth(), getHeight(), gradientColor, true));
					}
					if (view.beginShape != null) {
						if (beginFill)
							g2.fill(view.beginShape);
						g2.draw(view.beginShape);
					}
					if (view.endShape != null) {
						if (endFill)
							g2.fill(view.endShape);
						g2.draw(view.endShape);
					}
					if (lineDash != null) // Dash For Line Only
						g2.setStroke(new BasicStroke(lineWidth, c, j, 10.0f,
								lineDash, dashOffset));
					if (view.lineShape != null)
						g2.draw(view.lineShape);
				}

				if (selected) { // Paint Selected
					g2.setStroke(GraphConstants.SELECTION_STROKE);
					g2.setColor(highlightColor);
					if (view.beginShape != null)
						g2.draw(view.beginShape);
					if (view.lineShape != null)
						g2.draw(view.lineShape);
					if (view.endShape != null)
						g2.draw(view.endShape);
				}
				g2.setStroke(new BasicStroke(1));
				g
						.setFont((extraLabelFont != null) ? extraLabelFont
								: getFont());
				Object[] labels = GraphConstants.getExtraLabels(view
						.getAllAttributes());
				JGraph graph = (JGraph)this.graph.get();
				if (labels != null) {
					System.out.println(labels[0]);
					for (int i = 0; i < labels.length; i++){
						paintLabel(g, graph.convertValueToString(labels[i]),
								getExtraLabelPosition(view, i),
								false || !simpleExtraLabels);
						System.out.println(getExtraLabelPosition(view, i));
					}
				}
				if (graph.getEditingCell() != view.getCell()) {
					g.setFont(getFont());
					Object label = ((GWizEdgeView) view).getWeight();
					if (label != null) {
						paintLabel(g, label.toString(), getLabelPosition(view),
								true);
					}
				}
			}
		} else {
			paintSelectionBorder(g);
		}
	}

}
