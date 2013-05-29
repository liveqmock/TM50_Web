package org.openflashchart.chart;

import org.openflashchart.json.JsonHelper;

/**
 * 
 * The basic elements of graphics,contains generally graphics as X or Y Axis etc.
 * @author zhuzhenhua
 */
public abstract class BaseGraphProperty implements BaseGraph {

	/**
	 * @return String return string is formated by JsonHelper
	 */
	public String toString() {
		return JsonHelper.getJsonString(this);
	}
}
