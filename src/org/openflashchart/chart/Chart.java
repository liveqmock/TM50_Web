package org.openflashchart.chart;

import java.util.ArrayList;
import java.util.List;

import org.openflashchart.component.Elements;
import org.openflashchart.component.Title;
import org.openflashchart.component.ToolTip;
import org.openflashchart.component.X_Axis;
import org.openflashchart.component.X_Legend;
import org.openflashchart.component.Y_Axis;
import org.openflashchart.component.Y_Axis_Right;
import org.openflashchart.component.Y_Legend;
import org.openflashchart.elements.Area_Hollow;
import org.openflashchart.elements.Bar;
import org.openflashchart.elements.Bar_3d;
import org.openflashchart.elements.Bar_Fade;
import org.openflashchart.elements.Bar_Glass;
import org.openflashchart.elements.Bar_Sketch;
import org.openflashchart.elements.Bar_Stack;
import org.openflashchart.elements.Filled_Bar;
import org.openflashchart.elements.HBar;
import org.openflashchart.elements.Line;
import org.openflashchart.elements.Line_Dot;
import org.openflashchart.elements.Line_Hollow;
import org.openflashchart.elements.Pie;
import org.openflashchart.elements.Scatter;
import org.openflashchart.json.JsonHelper;

/**
 * 
 * Create graphic's entity ,it's contains all elements of graphic that store it
 * by List
 * 
 * @author zhuzhenhua
 * 
 * @time Jul 28, 20084:55:12 PM
 */
public class Chart {

	/** all basic elements's collections */
	private List<BaseGraph> baseGraphs = new ArrayList<BaseGraph>();

	/** all graphic's component */
	private Elements elements = new Elements();

	/** main chart's back ground colour */
	@SuppressWarnings("unused")
	private String bg_colour;

	public Chart() {
		this.baseGraphs.add(elements);
	}

	/**
	 * @param bg_colour
	 *            the bg_colour to set
	 */
	public void setBg_colour(String bg_colour) {
		this.bg_colour = bg_colour;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(Title title) {
		if (!baseGraphs.contains(title)) {
			this.baseGraphs.add(title);
		}
	}

	/**
	 * @param toolTip
	 *            the toolTip to set
	 */
	public void setToolTip(ToolTip toolTip) {
		if (!baseGraphs.contains(toolTip)) {
			this.baseGraphs.add(toolTip);
		}
	}

	/**
	 * @param x_Axis
	 *            the x_Axis to set
	 */
	public void setX_Axis(X_Axis x_Axis) {
		if (!baseGraphs.contains(x_Axis)) {
			this.baseGraphs.add(x_Axis);
		}
	}

	/**
	 * @param x_Legend
	 *            the x_Legend to set
	 */
	public void setX_Legend(X_Legend x_Legend) {
		if (!baseGraphs.contains(x_Legend)) {
			this.baseGraphs.add(x_Legend);
		}
	}

	/**
	 * @param y_Axis
	 *            the y_Axis to set
	 */
	public void setY_Axis(Y_Axis y_Axis) {
		if (!baseGraphs.contains(y_Axis)) {
			this.baseGraphs.add(y_Axis);
		}
	}

	/**
	 * @param y_Legend
	 *            the y_Legend to set
	 */
	public void setY_Legend(Y_Legend y_Legend) {
		if (!baseGraphs.contains(y_Legend)) {
			this.baseGraphs.add(y_Legend);
		}
	}

	/**
	 * @param y_Axis_Rigth
	 *            the y_Axis_Rigth to set
	 */
	public void setY_Axis_Rigth(Y_Axis_Right y_Axis_Rigth) {
		if (!baseGraphs.contains(y_Axis_Rigth)) {
			this.baseGraphs.add(y_Axis_Rigth);
		}
	}

	/**
	 * @param area_Hollow
	 *            the area_Hollow to set
	 */
	public void setArea_Hollow(Area_Hollow area_Hollow) {
		if (!this.elements.containsElement(area_Hollow)) {
			this.elements.addElement(area_Hollow);
		}
	}

	/**
	 * @param bar_3d
	 *            the bar_3d to set
	 */
	public void setBar_3d(Bar_3d bar_3d) {
		if (!this.elements.containsElement(bar_3d)) {
			this.elements.addElement(bar_3d);
		}
	}

	/**
	 * @param filled_Bar
	 *            the filled_Bar to set
	 */
	public void setFilled_Bar(Filled_Bar filled_Bar) {
		if (!this.elements.containsElement(filled_Bar)) {
			this.elements.addElement(filled_Bar);
		}
	}

	/**
	 * @param bar_Glass
	 *            the bar_Glass to set
	 */
	public void setBar_Glass(Bar_Glass bar_Glass) {
		if (!this.elements.containsElement(bar_Glass)) {
			this.elements.addElement(bar_Glass);
		}
	}

	/**
	 * @param bar_Sketch
	 *            the bar_Sketch to set
	 */
	public void setBar_Sketch(Bar_Sketch bar_Sketch) {
		if (!this.elements.containsElement(bar_Sketch)) {
			this.elements.addElement(bar_Sketch);
		}
	}

	/**
	 * @param bar_Stack
	 *            the bar_Stack to set
	 */
	public void setBar_Stack(Bar_Stack bar_Stack) {
		if (!this.elements.containsElement(bar_Stack)) {
			this.elements.addElement(bar_Stack);
		}
	}

	/**
	 * @param bar
	 *            the bar to set
	 */
	public void setBar(Bar bar) {
		if (!this.elements.containsElement(bar)) {
			this.elements.addElement(bar);
		}
	}

	/**
	 * @param hbar
	 *            the hbar to set
	 */
	public void setHBar(HBar hbar) {
		if (!this.elements.containsElement(hbar)) {
			this.elements.addElement(hbar);
		}
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public void setLine(Line line) {
		if (!this.elements.containsElement(line)) {
			this.elements.addElement(line);
		}
	}

	/**
	 * @param line_Dot
	 *            the line_Dot to set
	 */
	public void setLine_Dot(Line_Dot line_Dot) {
		if (!this.elements.containsElement(line_Dot)) {
			this.elements.addElement(line_Dot);
		}
	}

	/**
	 * @param line_Hollow
	 *            the line_Hollow to set
	 */
	public void setLine_Hollow(Line_Hollow line_Hollow) {
		if (!this.elements.containsElement(line_Hollow)) {
			this.elements.addElement(line_Hollow);
		}
	}

	/**
	 * @param pie
	 *            the pie to set
	 */
	public void setPie(Pie pie) {
		if (!this.elements.containsElement(pie)) {
			this.elements.addElement(pie);
		}
	}

	/**
	 * @param scatter
	 *            the scatter to set
	 */
	public void setScatter(Scatter scatter) {
		if (!this.elements.containsElement(scatter)) {
			this.elements.addElement(scatter);
		}
	}

	/**
	 * @param bar_Fade
	 *            the bar_Fade to set
	 */
	public void setBar_Fade(Bar_Fade bar_Fade) {
		if (!this.elements.containsElement(bar_Fade)) {
			this.elements.addElement(bar_Fade);
		}
	}

	public String toString() {
		return JsonHelper.getJsonString(this);
	}

	public String createChart() {
		return JsonHelper.getJsonString(this);
	}
}
