package org.openflashchart.component;


import org.openflashchart.chart.BaseGraphProperty;
import org.openflashchart.json.JsonHelper;

/**
 */
public class Values  extends BaseGraphProperty {
	
	private Double top;
	
	private Double bottom;
	
	private Double x;
	
	private Double y;
	
	private String value;
	
	private String colour;
	
	private String tip;
	
	private Integer halo__size;
	
	private Integer dot__size;
	
	private String label;

	public String getLabel() {
		return label;
	}

	public Values setLabel(String label) {
		this.label = label;
		return this;
	}

	public Double getTop() {
		return top;
	}

	public Values setTop(Double top) {
		this.top = top;
		return this;
	}

	public Double getBottom() {
		return bottom;
	}

	public Values setBottom(Double bottom) {
		this.bottom = bottom;
		return this;
	}

	public Double getX() {
		return x;
	}

	public Values setX(Double x) {
		this.x = x;
		return this;
	}

	public Double getY() {
		return y;
	}

	public Values setY(Double y) {
		this.y = y;
		return this;
	}

	public Double getValue() {
		return Double.parseDouble(value);
	}

	public Values setValue(Double value) {
		this.value = value+"";
		return this;
	}
	
	public Values setValue(String value) {
		this.value = value;
		return this;
	}
	

	public String getColour() {
		return colour;
	}

	public Values setColour(String colour) {
		this.colour = colour;
		return this;
	}

	public String getTip() {
		return tip;
	}

	public Values setTip(String tip) {
		this.tip = tip;
		return this;
	}

	public Integer getHalo__size() {
		return halo__size;
	}

	public Values setHalo__size(Integer halo__size) {
		this.halo__size = halo__size;
		return this;
	}

	public Integer getDot__size() {
		return dot__size;
	}

	public Values setDot__size(Integer dot__size) {
		this.dot__size = dot__size;
		return this;
	}

	public String toString() {
		return JsonHelper.getJsonString(this).substring(9);
	}
	
	
}
