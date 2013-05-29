package org.openflashchart.elements;

/**
 * The line graphic
 * {
 "elements":	[{
 "dot-size":	10,
 "font-size":	12,
 "text":	"My name is Line",
 "width":	4,
 "type":	"line",
 "values":	[4,5,7,11,16,17,17.3,18,18.5],
 "colour":	"#CCDDEE"
 }],

 "title":{
 "text":	"My name is Title",
 "style":	"{color: #736AFF;font-size: 30px;}"
 },

 "x_axis":{
 "colour":	"#FF0000",
 "labels":{
 "labels":	["a","b","c","d","e","f","g","h","i"]
 }
 },

 "y_axis":{
 "steps":	2,
 "colour":	"#00FF00",
 "max":	20
 }

 }

 * 
 * @author zhuzhenhua
 * 
 * @time Jul 30, 200810:45:51 AM
 */
public class Line extends BaseLine {

	public Line() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}

	public Line(String text) {
		this();
		this.text = text;
	}

}
