package org.openflashchart.component;

import org.openflashchart.chart.BaseGraphProperty;

/**
 * @author zhuzhenhua
 * 
 * @time Jul 29, 20081:04:56 PM
 */
public class ToolTip extends BaseGraphProperty {

	/** the ToolTip's shadow, value is true or false */
	private Boolean shadow;

	/** the ToolTip's stroke */
	private Integer stroke;

	/** the ToolTip's colour */
	private String colour;

	/** the ToolTip's back ground */
	private String background;

	/** the ToolTip's title */
	private String title;

	/** the ToolTip's body */
	private String body;

	/**
	 * @return the shadow
	 */
	public Boolean getShadow() {
		return shadow;
	}

	/**
	 * @param shadow
	 *            the shadow to set
	 */
	public void setShadow(Boolean shadow) {
		this.shadow = shadow;
	}

	/**
	 * @return the background
	 */
	public String getBackground() {
		return background;
	}

	/**
	 * @param background
	 *            the background to set
	 */
	public void setBackground(String background) {
		this.background = background;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * @param colour
	 *            the colour to set
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}

	/**
	 * @return the stroke
	 */
	public Integer getStroke() {
		return stroke;
	}

	/**
	 * @param stroke
	 *            the stroke to set
	 */
	public void setStroke(Integer stroke) {
		this.stroke = stroke;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
