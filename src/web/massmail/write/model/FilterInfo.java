package web.massmail.write.model;

public class FilterInfo {
	
	private int filterType;
	private String content;
	private int contentType;
	private int filterLevel;
	private int filterCount;
	
	public FilterInfo() {
	
	}
	public FilterInfo(int filterType, String content, int contentType, int filterLevel) {
		super();
		this.filterType = filterType;
		this.content = content;
		this.contentType = contentType;
		this.filterLevel = filterLevel;
		
	}
	/**
	 * @return the filterType
	 */
	public int getFilterType() {
		return filterType;
	}
	/**
	 * @param filterType the filterType to set
	 */
	public void setFilterType(int filterType) {
		this.filterType = filterType;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the contentType
	 */
	public int getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the filterLevel
	 */
	public int getFilterLevel() {
		return filterLevel;
	}
	/**
	 * @param filterLevel the filterLevel to set
	 */
	public void setFilterLevel(int filterLevel) {
		this.filterLevel = filterLevel;
	}
	/**
	 * @return the filterCount
	 */
	public int getFilterCount() {
		return filterCount;
	}
	/**
	 * @param filterCount the filterCount to set
	 */
	public void setFilterCount(int filterCount) {
		this.filterCount = filterCount;
	}
	
	
	
	
	
	

}
