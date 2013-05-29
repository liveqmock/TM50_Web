package web.target.targetlist.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FileHeaderInfo  implements Serializable{
	
	private int columnNumber;
	private String title;

	
	public void setColumnNumber(int columnNumber){
		this.columnNumber = columnNumber;
	}
	public int getColumnNumber(){
		return columnNumber;
	}	
	
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return title;
	}	
	
}
