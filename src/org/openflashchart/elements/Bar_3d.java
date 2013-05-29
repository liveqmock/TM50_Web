package org.openflashchart.elements;

/**
 * The grahic have 3d purpose of bar
 * 3d
 * {
 "bg_colour":	"#EEEEEE",
 "elements":	[{
 "font-size":	10,
 "text":	"My name is Bar_3d",
 "type":	"bar_3d",
 "values":	[7,9,6,11,8,4],
 "bar_3d":	true,
 "colour":	"#9933CC",
 "alpha":	0.5
 }],

 "x_axis":{
 "steps":	1,
 "3d":	6,
 "colour":	"#00EE00",
 "labels":	[2,3,4,5]
 },

 "y_axis":{
 "max":	17
 }

 }

 * @author zhuzhenhua
 *
 * @time Jul 30, 200810:47:24 AM
 */
public class Bar_3d extends BaseBar {

	public Bar_3d() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}

	public Bar_3d(String text) {
		this();
		this.text = text;
	}

}
