package org.openflashchart.elements;

/**
 * The base class of each of Bars
 * 
 * @author zhuzhenhua
 * 
 * @time Aug 1, 20084:52:30 PM
 */
public abstract class BaseBar extends Element {

	private Boolean bar_3d;

	private Double alpha;

	/**
	 * @return the alpha
	 */
	public Double getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha
	 *            the alpha to set
	 */
	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @return the bar_3d
	 */
	public Boolean getBar_3d() {
		return bar_3d;
	}

	/**
	 * @param bar_3d
	 *            the bar_3d to set
	 */
	public void setBar_3d(Boolean bar_3d) {
		this.bar_3d = bar_3d;
	}
}
