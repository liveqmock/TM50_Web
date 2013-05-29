package org.openflashchart.component;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.openflashchart.chart.BaseGraphProperty;

/**
 * @author zhuzhenhua
 * 
 * @time Aug 5, 200812:56:32 PM
 */
public abstract class BaseAxis extends BaseGraphProperty {

	private Integer stroke;
	
	private Integer tick__height;
	
	/** the basic steps*/
	private String steps;

	/** the offset of Axis*/
	private Boolean offset;

	/** value is visible*/
	private Boolean visible;

	/** Axis's colour*/
	private String colour;

	/** parallel's colour*/
	private String grid__colour;

	/** the min scale*/
	private Integer min;

	/** the max scale*/
	private String max;

	/** the labels of Axis*/
	private Labels labels;

	/** the labels of Axis*/
	private Object list_labels;

	/** 3D purpose*/
	private Integer ___3d;
	
	private String rotate;

	public String getRotate() {
		return rotate;
	}

	public void setRotate(String rotate) {
		this.rotate = rotate;
	}

	/**
	 * @return the ___3d
	 */
	public Integer get___3d() {
		return ___3d;
	}

	/**
	 * @param ___3d
	 *            the ___3d to set
	 */
	public void set___3d(Integer ___3d) {
		this.___3d = ___3d;
	}

	/**
	 * @return the offset
	 */
	public Boolean getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(Boolean offset) {
		this.offset = offset;
	}

	/**
	 * @return the visible
	 */
	public Boolean getVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the list_labels
	 */
	public Object getList_labels() {
		return list_labels;
	}

	/**
	 * @param list_labels
	 *            the list_labels to set
	 */
	@SuppressWarnings("unchecked")	
	public void setList_labels(List list_labels) {
		this.list_labels = (Object)list_labels;
  	}

	public void setList_labels(Labels list_labels) {
		this.list_labels = (Object)list_labels;
  	}
	
	/**
	 * set list_lables by array
	 * @param objArray labels's array
	 */
	@SuppressWarnings("unchecked")
	public void setList_labels(Object[] objArray) {
		if (null == this.list_labels) {
			this.list_labels = new ArrayList();
		}
		for (Object obj : objArray) {
			if (obj instanceof Object[]) {
				List objList = new ArrayList();
				this.objArrayToList((Object[]) obj, objList);
				((ArrayList)this.list_labels).add(objList);
			} else {
				((ArrayList)this.list_labels).add(obj);
			}
		}
	}

	/**
	 * change array to list
	 * @param objArray labels's array
	 * @param objList labels's list is changed by this method 
	 */
	@SuppressWarnings("unchecked")
	private void objArrayToList(Object[] objArray, List objList) {
		for (Object o : objArray) {
			if (o instanceof Object[]) {
				this.objArrayToList((Object[]) o, objList);
			} else {
				objList.add(o);
			}
		}
	}

	/**
	 * @return the labels
	 */
	public Labels getLabels() {
		return labels;
	}

	/**
	 * @param labels
	 *            the labels to set
	 */
	public void setLabels(Labels labels) {
		this.labels = labels;
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
	 * @return the grid_colour
	 */
	public String getGrid__colour() {
		return grid__colour;
	}

	/**
	 * @param grid_colour
	 *            the grid_colour to set
	 */
	public void setGrid__colour(String grid__colour) {
		this.grid__colour = grid__colour;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public Integer getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(Integer min) {
		this.min = min;
	}

	/**
	 * @return the steps
	 */
	public String getSteps() {
		return steps;
	}

	/**
	 * @param steps
	 *            the steps to set
	 */
	public void setSteps(String steps) {
		this.steps = steps;
	}
	
	public Integer getStroke() {
		return stroke;
	}

	public void setStroke(Integer stroke) {
		this.stroke = stroke;
	}

	public Integer getTick__height() {
		return tick__height;
	}

	public void setTick__height(Integer tick__height) {
		this.tick__height = tick__height;
	}
	
	protected void setAutoMax( double value ) {
		double base = value + (value * 0.2);
		int step = 0;
		String tmp = "", start = "5";
		
		if( value <= 100 ) {
			this.setMax("100");
			this.setSteps("10");
		} else {
			
			this.setMax(base+"");
			tmp = ((int)(base/20))+"";
			
			String zero = "";   
			for (int i = 0; i < tmp.length()-1; i++) {   
			  zero += "0";   
			}
			if(Integer.parseInt(tmp.substring(0,1)) > 5) {
				zero += "0";
				start = "1";
			}
			DecimalFormat df = new DecimalFormat(zero);   
			
			tmp = start+df.format(0);
			step = Integer.parseInt(tmp);
			this.setSteps(step+"");
		}
		
	}
	
	
}
