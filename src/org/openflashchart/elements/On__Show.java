package org.openflashchart.elements;

import org.openflashchart.chart.BaseGraphProperty;
import org.openflashchart.json.JsonHelper;

public class On__Show extends BaseGraphProperty {
	private String type;
	private double cascade;
	private double delay = 0;
	public String getType() {
		return type;
	}
	public On__Show setType(String type) {
		this.type = type;
		return this;
	}
	public double getCascade() {
		return cascade;
	}
	public On__Show setCascade(double cascade) {
		this.cascade = cascade;
		return this;
	}
	public double getDelay() {
		return delay;
	}
	public On__Show setDelay(double delay) {
		this.delay = delay;
		return this;
	}
	public String toString() {
		return JsonHelper.getJsonString(this).substring(11);
	}
	
	
}