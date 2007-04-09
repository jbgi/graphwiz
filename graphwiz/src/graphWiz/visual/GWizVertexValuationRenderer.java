package graphWiz.visual;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

/**
 */
public class GWizVertexValuationRenderer extends VertexRenderer {

	/**
	 * Return a slightly larger preferred size than for a rectangle.
	 */
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.width *= (4);
		d.height *= (1.5);
		return d;
	}
	
	/**
	 * Returns the intersection of the bounding rectangle and the
	 * straight line between the source and the specified point p.
	 * The specified point is expected not to intersect the bounds.
	 */
	public Point2D getPerimeterPoint(VertexView edgeView, Point2D source, Point2D p) {
		Rectangle2D r = edgeView.getBounds();

		double x = r.getX();
		double y = r.getY();
		double a = (r.getWidth() + 1) / 2;
		double b = (r.getHeight() + 1) / 2;

		// x0,y0 - center of ellipse
		double x0 = x + a;
		double y0 = y + b;

		// x1, y1 - point
		double x1 = p.getX();
		double y1 = p.getY();

		// Calculates straight line equation through point and ellipse center
		// y = d * x + h
		double dx = x1 - x0;
		double dy = y1 - y0;

		if (dx == 0)
			return new Point((int) x0, (int) (y0 + b * dy / Math.abs(dy)));

		double d = dy / dx;
		double h = y0 - d * x0;

		// Calculates intersection
		double e = a * a * d * d + b * b;
		double f = -2 * x0 * e;
		double g = a * a * d * d * x0 * x0 + b * b * x0 * x0 - a * a * b * b;

		double det = Math.sqrt(f * f - 4 * e * g);

		// Two solutions (perimeter points)
		double xout1 = (-f + det) / (2 * e);
		double xout2 = (-f - det) / (2 * e);
		double yout1 = d * xout1 + h;
		double yout2 = d * xout2 + h;

		double dist1 = Math.sqrt(Math.pow((xout1 - x1), 2)
				+ Math.pow((yout1 - y1), 2));
		double dist2 = Math.sqrt(Math.pow((xout2 - x1), 2)
				+ Math.pow((yout2 - y1), 2));

		// Correct solution
		double xout, yout;

		if (dist1 < dist2) {
			xout = xout1;
			yout = yout1;
		} else {
			xout = xout2;
			yout = yout2;
		}

		return new Point2D.Double(xout, yout);
	}


	/**
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int l = ((GWizVertexValuationView) view).getValuation().length();
		g2.scale(0.8, 0.8);
		g2.setColor(((GWizVertexValuationView) view).getColor());
		g2.drawString(((GWizVertexValuationView) view).getValuation(), 0, 10);
	}
}