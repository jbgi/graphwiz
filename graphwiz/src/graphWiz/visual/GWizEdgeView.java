/*
 * @(#)EdgeView.java	1.0 03-JUL-04
 * 
 * Copyright (c) 2001-2004 Gaudenz Alder
 *  
 */
package graphWiz.visual;

import graphWiz.GWizModelAdapter;
import graphWiz.model.GWizEdge;
import graphWiz.model.GWizVertex;
import graphWiz.model.GWizEdge.Description;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.jgraph.JGraph;
import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;
import org.jgraph.plaf.GraphUI;
import org.jgraph.plaf.basic.BasicGraphUI;

/**
 * The default implementation of an edge view. The getGWizEdgeRenderer method assumes a renderer of type GWizEdgeRenderer. If you provide a custom renderer to a subclass, you must also override the methods that call this method, namely: getShape, getLabelBounds, getExtraLabelBounds, intersects and getBounds.
 * @version  1.0 1/1/02
 * @author  Gaudenz Alder
 */

public class GWizEdgeView extends EdgeView {
	
	EdgeView predStroke;
	
	JGraph graph;
	
	boolean updated =false ;

	private GWizModelAdapter jgAdapter;
	
	/** Renderer for the class. */
	public static transient GWizEdgeRenderer renderer;

	/**
	 * Constructs an empty edge view.
	 */
	public GWizEdgeView(Object cell, GWizModelAdapter jgAdapter) {
		super(cell);
		this.jgAdapter = jgAdapter;
		renderer = new GWizEdgeRenderer();
		
	}

	//
	// Data Source
	//


	/**
	 * Returns a renderer for the class.
	 * @uml.property  name="renderer"
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}
	
	private GWizEdge getModel() {
		return jgAdapter.getCellEdge((DefaultEdge) getCell());
	}
	
	Color getColor(){
		if (getModel()==null) return Color.BLACK;
		if (getModel().getDescription()==Description.PATH)
			return Color.GREEN;
		if (getModel().getDescription()==Description.SELECT)
			return Color.BLUE;
		if (getModel().getDescription()==Description.REGULAR )
			return Color.BLACK;
		if (getModel().getDescription()==Description.EXPLORER )
			return Color.CYAN;
		else return Color.BLACK;
	}
	
	void paintPred(){
		if (getModel()!=null && !updated){
		//	if (jgAdapter.getGWizGraph().getEdgeSource(getModel()) == jgAdapter.getGWizGraph().getEdgeTarget(getModel()).getPred()){
				GraphConstants.setLineBegin(getAllAttributes(), GraphConstants.ARROW_CIRCLE);
		//	}
		jgAdapter.cellsChanged(new Object[] { getCell()});
		updated = true;
		}
		else
			updated = false;
	}
	
	String getWeight() {
		double weight = jgAdapter.getGWizGraph().getEdgeWeight(getModel());
		if (weight == (int) weight)
			return Integer.toString((int) weight);
		else return Double.toString(weight);
	}
	
	
}