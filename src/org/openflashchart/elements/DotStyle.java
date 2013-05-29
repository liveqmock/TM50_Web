package org.openflashchart.elements;

import org.openflashchart.chart.BaseGraphProperty;
import org.openflashchart.json.JsonHelper;

public class DotStyle extends BaseGraphProperty {
	private String type;
	private String colour;
	private int dot__size;
	private String tip;
	public String getType() {
		return type;
	}
	public DotStyle setType(String type) {
		this.type = type;
		return this;
	}
	public String getColour() {
		return colour;
	}
	public DotStyle setColour(String colour) {
		this.colour = colour;
		return this;
	}
	public int getDot__size() {
		return dot__size;
	}
	public DotStyle setDot__size(int dot__size) {
		this.dot__size = dot__size;
		return this;
	}
	public String getTip() {
		return tip;
	}
	public DotStyle setTip(String tip) {
		this.tip = tip;
		return this;
	}
	public String toString() {
		return JsonHelper.getJsonString(this).substring(11);
	}
	
}