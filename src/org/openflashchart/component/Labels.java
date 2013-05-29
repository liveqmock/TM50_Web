package org.openflashchart.component;

import java.util.ArrayList;
import java.util.List;

import org.openflashchart.chart.BaseGraphProperty;
import org.openflashchart.json.JsonHelper;

/**
 * @author zhuzhenhua
 * 
 * @time Jul 29, 20089:08:43 AM
 */
public class Labels extends BaseGraphProperty {

	private Boolean visible;

	private Integer steps = null;

	private String visible__steps = null;
	
	private String text = null;
	
	private Integer rotate = null;

	private Integer size = null;
	
	@SuppressWarnings("unchecked")	
	private List labels = null;

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
	public Labels setVisible(Boolean visible) {
		this.visible = visible;
		return this;
	}

	/**
	 * @return the labels
	 */
	@SuppressWarnings("unchecked")	
	public List getLabels() {
		return labels;
	}

	/**
	 * @param labels
	 *            the labels to set
	 */
	@SuppressWarnings("unchecked")	
	public Labels setLabels(List labels) {
		this.labels = labels;
		return this;
	}

	@SuppressWarnings("unchecked")
	public Labels setLabels(Object[] objArray) {
		if (null == this.labels) {
			this.labels = new ArrayList();
		}
		for (Object obj : objArray) {
			if (obj instanceof Object[]) {
				List objList = new ArrayList();
				this.objArrayToList((Object[]) obj, objList);
				this.labels.add(objList);
			} else {
				this.labels.add(obj);
			}

		}

		return this;
	}

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
	 * @return the rotate
	 */
	public Integer getRotate() {
		return rotate;
	}

	/**
	 * @param rotate
	 *            the rotate to set
	 */
	public Labels setRotate(Integer rotate) {
		this.rotate = rotate;
		return this;
	}

	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public Labels setSize(Integer size) {
		this.size = size;
		return this;
	}

	/**
	 * @return the steps
	 */
	public Integer getSteps() {
		return steps;
	}

	/**
	 * @param steps
	 *            the steps to set
	 */
	public Labels setSteps(Integer steps) {
		this.steps = steps;
		return this;
	}

	public String getVisible__steps() {
		return visible__steps;
	}

	public Labels setVisible__steps(String visible__steps) {
		this.visible__steps = visible__steps;
		return this;
	}

	public String getText() {
		return text;
	}

	public Labels setText(String text) {
		this.text = text;
		return this;
	}
	
	public String toString() {
		return JsonHelper.getJsonString(this).substring(9);
	}
	
	
}
