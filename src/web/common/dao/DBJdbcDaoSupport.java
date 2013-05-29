package web.common.dao;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

public class DBJdbcDaoSupport extends SimpleJdbcDaoSupport{

	private MessageSourceAccessor messageSourceAccesor;
	private DataFieldMaxValueIncrementer incrementer; 
	
	public void setIncrementer(DataFieldMaxValueIncrementer incrementer){
		this.incrementer = incrementer;
	}	
	
	protected final DataFieldMaxValueIncrementer getIncrementer(){
		return incrementer;
	}
	
	public void setMessageSourceAccessor(MessageSourceAccessor newMessageSourceAccessor){
		this.messageSourceAccesor = newMessageSourceAccessor;
	}
	
	protected final MessageSourceAccessor getMessageSourceAccessor(){
		return messageSourceAccesor;
	}
	 
}
