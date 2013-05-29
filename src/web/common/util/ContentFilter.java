package web.common.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.HTMLNode;
import org.htmlparser.HTMLParser;
import org.htmlparser.HTMLReader;
import org.htmlparser.HTMLRemarkNode;
import org.htmlparser.HTMLStringNode;
import org.htmlparser.tags.HTMLImageTag;
import org.htmlparser.tags.HTMLLinkTag;
import org.htmlparser.tags.HTMLTag;
import org.htmlparser.tags.HTMLScriptTag;
import org.htmlparser.util.DefaultHTMLParserFeedback;
import org.htmlparser.util.HTMLEnumeration;

import web.massmail.write.model.FilterManager;
import web.massmail.write.model.FilterInfo;
import web.massmail.write.model.MassMailLink;

public class ContentFilter {
	
	private String inputHTML;
	private String title;
	private HTMLParser parser;
	private HTMLReader reader;
	
	public ContentFilter(String inputHTML, String title)
	{
		this.inputHTML = inputHTML;
		this.title = title;

		StringReader sr = new StringReader(inputHTML);
		reader = new HTMLReader(new BufferedReader(sr), inputHTML.length());
		
		try
		{
			parser = new HTMLParser(reader, new DefaultHTMLParserFeedback());
			parser.registerScanners(); // Register standard scanners (Very Important)
		}
		catch (Exception e)
		{
			
		}
	}
	
	public List<FilterInfo> getContentExtract( List<FilterManager> contentList) throws Exception
	{
		HTMLNode node = null;		
		List<FilterInfo> filteredList = new ArrayList<FilterInfo>();

		String currentline = "";
		
		for(int i=0;i<contentList.size();i++)
		{
			if((contentList.get(i).getFilterType()!=2 && contentList.get(i).getFilterLevel()!= 3 && contentList.get(i).getContentType()==1) && title.indexOf(contentList.get(i).getContent())>-1)
			{
				FilterInfo fi = new FilterInfo(contentList.get(i).getFilterType(), contentList.get(i).getContent(), contentList.get(i).getContentType(), contentList.get(i).getFilterLevel());
				
				filteredList.add(fi);							
				
			}			
		}

		for (HTMLEnumeration e = parser.elements(); e.hasMoreNodes();)
		{
			try
			{
				node = e.nextHTMLNode(); // Get the next HTML Node				
			}
			catch (Exception ee1)
			{
				
				node = null;
			}
			if(node != null && !(node instanceof HTMLRemarkNode))
			{
				if (node instanceof HTMLScriptTag )
				{
					currentline = node.toHTML();
					FilterInfo fi = new FilterInfo(2, currentline, 2, 1);
					
					filteredList.add(fi);
										
				}
				else if ((node instanceof HTMLStringNode))
				{
					currentline = node.toHTML();
					
					for(int i=0;i<contentList.size();i++)
					{
						if((contentList.get(i).getFilterType()!=1 && contentList.get(i).getFilterLevel()!= 3 && contentList.get(i).getContentType()==1) && currentline.indexOf(contentList.get(i).getContent())>-1)
						{
							FilterInfo fi = new FilterInfo(contentList.get(i).getFilterType(), contentList.get(i).getContent(), contentList.get(i).getContentType(), contentList.get(i).getFilterLevel());
							
							filteredList.add(fi);							
							
						}
					}					
					
				}
			
				else if (node instanceof HTMLTag )
				{
					currentline = node.toHTML();
				
					for(int i=0;i<contentList.size();i++)
					{
						if((contentList.get(i).getFilterType()!=1 && contentList.get(i).getFilterLevel()!= 3 && contentList.get(i).getContentType()!=1) && currentline.indexOf(contentList.get(i).getContent())>-1)
						{
							FilterInfo fi = new FilterInfo(contentList.get(i).getFilterType(), contentList.get(i).getContent(), contentList.get(i).getContentType(), contentList.get(i).getFilterLevel());
							
							filteredList.add(fi);
							
						}
					}
				}				
			}
		} 

		return filteredList;
	}
	
	
	

}
