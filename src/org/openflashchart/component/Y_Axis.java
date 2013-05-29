package org.openflashchart.component;

/**
 * The chart's Y Axis
 * 
 * @author zhuzhenhua
 * 
 * @time Jul 29, 200810:12:16 AM
 */
public class Y_Axis extends BaseAxis {

	private Integer stroke;

	private Integer tick_length;

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
	 * @return the tick_length
	 */
	public Integer getTick_length() {
		return tick_length;
	}

	/**
	 * @param tick_length
	 *            the tick_length to set
	 */
	public void setTick_length(Integer tick_length) {
		this.tick_length = tick_length;
	}
	

	public void setAutoMax(double value) {
		super.setAutoMax(value);
	}
}
