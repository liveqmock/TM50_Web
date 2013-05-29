package web.common.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import org.htmlparser.HTMLNode;
import org.htmlparser.HTMLParser;
import org.htmlparser.HTMLReader;
import org.htmlparser.HTMLRemarkNode;
import org.htmlparser.HTMLStringNode;
import org.htmlparser.tags.HTMLImageTag;
import org.htmlparser.tags.HTMLTag;
import org.htmlparser.util.DefaultHTMLParserFeedback;
import org.htmlparser.util.HTMLEnumeration;
import org.htmlparser.util.HTMLParserException;
/**
 * ImageExtractor extracts all the links from the given webpage
 * and prints them on standard output.
 */
public class ImageExtractor
{
	private String inputHTML;
	private HTMLParser parser;
	private HTMLReader reader;


	public ImageExtractor(String inputHTML)
	{
		this.inputHTML = inputHTML;
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

	public Collection<String[]> extractAllImages() throws HTMLParserException, Exception
	{
		HTMLNode node = null;
		HTMLImageTag imgTag;
		
		Collection<String[]> imageinfoList = new ArrayList<String[]>();
		String[] temp_array = null;
	
		String imgName = "";
		String bgImgName = "";

		for (HTMLEnumeration e = parser.elements(); e.hasMoreNodes();)
		{
			try
			{
				node = e.nextHTMLNode(); // Get the next HTML Node
			}
			catch (Exception e1)
			{
				node = null;
			}
			if (node != null)
			{
				if (node instanceof HTMLImageTag)
				{
					imgTag = (HTMLImageTag) node;
					imgName = imgTag.getImageURL();
				
					if (imgName.indexOf(":") > 0 &&  imgName.indexOf("http") < 0)
					{
						java.io.File file = new java.io.File(imgName);
						temp_array = new String[3];
						imgName = web.common.util.StringUtil.rplc(imgName,"\\","\\\\");
						temp_array[0] = imgName;
						temp_array[1] = file.getName();
						imageinfoList.add(temp_array);
	
					}
				}
				//background 이미지 검사
				if (!(node instanceof HTMLRemarkNode) && !(node instanceof HTMLStringNode))
				{
					bgImgName = ((HTMLTag) node).getParameter("background");
					try{
							if (!bgImgName.equalsIgnoreCase(null) && bgImgName.indexOf(":") > 0 &&  bgImgName.indexOf("http") < 0)
							{
								java.io.File file = new java.io.File(bgImgName);
								temp_array = new String[3];
								bgImgName = web.common.util.StringUtil.rplc(bgImgName,"\\","\\\\");
								temp_array[0] = bgImgName;
								temp_array[1] = file.getName();
								imageinfoList.add(temp_array);
						
							}
					}catch (Exception e2){}
				}
			}
		}
		
		return imageinfoList;
	}
	
	public String changeImageURL(ArrayList<?> linkinfoList, String baseURL) throws HTMLParserException
	{
		String resulthtml = this.inputHTML;
		String[] temp_array = null;
		String image_path = "";
		String saveUrl = "";
		for(int i=0;i < linkinfoList.size();i++){
			
			temp_array = (String[]) linkinfoList.get(i);
			image_path = temp_array[0];
			saveUrl = baseURL + temp_array[1];
			image_path = web.common.util.StringUtil.rplc(image_path,"\\\\","\\");
			resulthtml = web.common.util.StringUtil.replace(resulthtml, image_path, saveUrl);
		}
		return resulthtml;
	}
}
