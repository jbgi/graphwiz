/*
 * @(#)EdgeRenderer.java	1.0 03-JUL-04
 * 
 * Copyright (c) 2001-2005 Gaudenz Alder
 *  
 * See LICENSE file in distribution for licensing details of this source file
 */
package graphWiz.visual;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.jgraph.JGraph;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.util.Bezier;
import org.jgraph.util.Spline2D;

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
		//((GWizEdgeView) view).paintPred();
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
				g.setColor(((GWizEdgeView) view).getColor());
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
				JGraph graph = (JGraph)this.graph.get();
				if (graph.getEditingCell() != view.getCell()) {
					g.setFont(getFont());
					Object label = ((GWizEdgeView) view).getWeight();
					fontColor = ((GWizEdgeView) view).getColor().darker();
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
	
	/**
	 * Paint the specified label for the current edgeview.
	 */
	protected void paintLabel(Graphics g, String label, Point2D p,
			boolean mainLabel) {
		if (p != null && label != null && label.length() > 0 && metrics != null) {
			int sw = metrics.stringWidth(label);
			int sh = metrics.getHeight();
			Graphics2D g2 = (Graphics2D) g;
			double angle = 0;
			int dx = -sw / 2;
			int offset = isMoveBelowZero || mainLabel ? 0 : Math
					.min(0, (int) (dx + p.getX()));

			g2.translate(p.getX() - offset, p.getY());
			if (mainLabel) {
				angle = getLabelAngle(label);
				g2.rotate(angle);
			}
			if (isOpaque() && mainLabel) {
				g.setColor(getBackground());
				g.fillRect(-sw / 2 - 1, -sh / 2 - 1, sw + 2,
						sh + 2);
			}
			if (borderColor != null && mainLabel) {
				g.setColor(borderColor);
				g.drawRect(-sw / 2 - 1, -sh / 2 - 1, sw + 2,
						sh + 2);
			}

			int dy = +sh / 4;
			g.setColor(((GWizEdgeView) view).getColor().darker());
			if (mainLabel && borderColor == null && !isOpaque()) {
				// Shift label perpendicularly by the descent so it
				// doesn't cross the line.
				dy = -metrics.getDescent();
			}
			g.drawString(label, dx, dy);
			if (mainLabel) {
				// Undo the transform
				g2.rotate(-angle);
			}
			g2.translate(-p.getX() + offset, -p.getY());
		}
	}
	
	private boolean isLabelTransformEnabled() {
		return labelTransformEnabled;
	}

	/**
	 * Estimates whether the transform for label should be applied. With the
	 * transform, the label will be painted along the edge. To apply transform,
	 * rotate graphics by the angle returned from {@link #getLabelAngle}
	 * 
	 * @return true, if transform can be applied, false otherwise
	 */
	private boolean isLabelTransform(String label) {
		if (!isLabelTransformEnabled()) {
			return false;
		}
		Point2D p = getLabelPosition(view);
		if (p != null && label != null && label.length() > 0) {
			int sw = metrics.stringWidth(label);
			Point2D p1 = view.getPoint(0);
			Point2D p2 = view.getPoint(view.getPointCount() - 1);
			double length = Math.sqrt((p2.getX() - p1.getX())
					* (p2.getX() - p1.getX()) + (p2.getY() - p1.getY())
					* (p2.getY() - p1.getY()));
			if (!(length <= Double.NaN || length < sw)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calculates the angle at which graphics should be rotated to paint label
	 * along the edge. Before calling this method always check that transform
	 * should be applied using {@linkisLabelTransform}
	 * 
	 * @return the value of the angle, 0 if the angle is zero or can't be
	 *         calculated
	 */
	private double getLabelAngle(String label) {
		Point2D p = getLabelPosition(view);
		double angle = 0;
		if (p != null && label != null && label.length() > 0) {
			int sw = metrics.stringWidth(label);
			// Note: For control points you may want to choose other
			// points depending on the segment the label is in.
			Point2D p1 = view.getPoint(0);
			Point2D p2 = view.getPoint(view.getPointCount() - 1);
			// Length of the edge
			double length = Math.sqrt((p2.getX() - p1.getX())
					* (p2.getX() - p1.getX()) + (p2.getY() - p1.getY())
					* (p2.getY() - p1.getY()));
			if (!(length <= Double.NaN || length < sw)) { // Label fits into
				// edge's length

				// To calculate projections of edge
				double cos = (p2.getX() - p1.getX()) / length;
				double sin = (p2.getY() - p1.getY()) / length;

				// Determine angle
				angle = Math.acos(cos);
				if (sin < 0) { // Second half
					angle = 2 * Math.PI - angle;
				}
			}
			if (angle > Math.PI / 2 && angle <= Math.PI * 3 / 2) {
				angle -= Math.PI;
			}
		}
		return angle;
	}
	
	/**
	 * Returns the shape that represents the current edge in the context of the
	 * current graph. This method sets the global beginShape, lineShape and
	 * endShape variables as a side-effect.
	 */
	protected Shape createShape() {
		int n = view.getPointCount();
		System.out.println(n);
		if (n > 1) {
			// Following block may modify static vars as side effect (Flyweight
			// Design)
			EdgeView tmp = view;
			Point2D[] p = null;
			p = new Point2D[n];
			for (int i = 0; i < n; i++) {
				Point2D pt = tmp.getPoint(i);
				if (pt == null)
					return null; // exit
				else
					p[i] = new Point2D.Double(pt.getX(), pt.getY());
			}

			// End of Side-Effect Block
			// Undo Possible MT-Side Effects
			if (view != tmp) {
				view = tmp;
				installAttributes(view);
			}
			// End of Undo
			if (view.sharedPath == null) {
				view.sharedPath = new GeneralPath(GeneralPath.WIND_NON_ZERO, n);
			} else {
				view.sharedPath.reset();
			}
			view.beginShape = view.lineShape = view.endShape = null;
			Point2D p0 = p[0];
			Point2D pe = p[n - 1];
			Point2D p1 = p[1];
			Point2D p2 = p[n - 2];
			
			if (lineStyle == GraphConstants.STYLE_BEZIER && n > 2) {
				bezier = new Bezier(p);
				p2 = bezier.getPoint(bezier.getPointCount() - 1);
			} else if (lineStyle == GraphConstants.STYLE_SPLINE && n > 2) {
				spline = new Spline2D(p);
				double[] point = spline.getPoint(0.9875);
				// Extrapolate p2 away from the end point, pe, to avoid integer
				// rounding errors becoming too large when creating the line end
				double scaledX = pe.getX() - ((pe.getX() - point[0]) * 128);
				double scaledY = pe.getY() - ((pe.getY() - point[1]) * 128);
				p2.setLocation(scaledX, scaledY);
			}

			if (beginDeco != GraphConstants.ARROW_NONE) {
				view.beginShape = createLineEnd(beginSize, beginDeco, p1, p0);
			}
			if (endDeco != GraphConstants.ARROW_NONE) {
				view.endShape = createLineEnd(endSize, endDeco, p2, pe);
			}
			view.sharedPath.moveTo((float) p0.getX(), (float) p0.getY());
			/* THIS CODE WAS ADDED BY MARTIN KRUEGER 10/20/2003 */
			if (lineStyle == GraphConstants.STYLE_BEZIER && n > 2) {
				Point2D[] b = bezier.getPoints();
				view.sharedPath.quadTo((float) b[0].getX(),
						(float) b[0].getY(), (float) p1.getX(), (float) p1
								.getY());
				for (int i = 2; i < n - 1; i++) {
					Point2D b0 = b[2 * i - 3];
					Point2D b1 = b[2 * i - 2];
					view.sharedPath.curveTo((float) b0.getX(), (float) b0
							.getY(), (float) b1.getX(), (float) b1.getY(),
							(float) p[i].getX(), (float) p[i].getY());
				}
				view.sharedPath.quadTo((float) b[b.length - 1].getX(),
						(float) b[b.length - 1].getY(),
						(float) p[n - 1].getX(), (float) p[n - 1].getY());
			} else if (lineStyle == GraphConstants.STYLE_SPLINE && n > 2) {
				for (double t = 0; t <= 1; t += 0.0125) {
					double[] xy = spline.getPoint(t);
					view.sharedPath.lineTo((float) xy[0], (float) xy[1]);
				}
			}
			/* END */
			else {
				for (int i = 1; i < n - 1; i++)
					view.sharedPath.lineTo((float) p[i].getX(), (float) p[i]
							.getY());
				view.sharedPath.lineTo((float) pe.getX(), (float) pe.getY());
			}
			view.sharedPath.moveTo((float) pe.getX(), (float) pe.getY());
			if (view.endShape == null && view.beginShape == null) {
				// With no end decorations the line shape is the same as the
				// shared path and memory
				view.lineShape = view.sharedPath;
			} else {
				view.lineShape = (GeneralPath) view.sharedPath.clone();
				if (view.endShape != null)
					view.sharedPath.append(view.endShape, true);
				if (view.beginShape != null)
					view.sharedPath.append(view.beginShape, true);
			}
			return view.sharedPath;
		}
		return null;
	}

}
