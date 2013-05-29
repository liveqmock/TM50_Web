package web.common.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.HTMLNode;
import org.htmlparser.HTMLParser;
import org.htmlparser.HTMLReader;
import org.htmlparser.HTMLRemarkNode;
import org.htmlparser.HTMLStringNode;
import org.htmlparser.tags.HTMLFormTag;
import org.htmlparser.tags.HTMLImageTag;
import org.htmlparser.tags.HTMLInputTag;
import org.htmlparser.tags.HTMLLinkTag;
import org.htmlparser.tags.HTMLTag;
import org.htmlparser.util.DefaultHTMLParserFeedback;
import org.htmlparser.util.HTMLEnumeration;
import org.htmlparser.util.HTMLParserException;
import web.massmail.write.model.MassMailLink;


/**
 * LinkExtractor extracts all the links from the given webpage
 * and prints them on standard output.
 */
@SuppressWarnings("unused")
public class LinkExtractor
{
	
	private final String classname = "LinkExtractor";
	private int massmailID; // 대량메일ID
	private String inputHTML;
	private String rejectLinkURL;
	private HTMLParser parser;
	private HTMLReader reader;
    private String duplicationYN;
	private Logger logger = Logger.getLogger(this.getClass());

	public LinkExtractor(String inputHTML, int massmailID, String rejectLinkURL)
	{
		this.inputHTML = inputHTML;
		this.massmailID = massmailID;
		this.rejectLinkURL = rejectLinkURL;

		StringReader sr = new StringReader(inputHTML);
		reader = new HTMLReader(new BufferedReader(sr), inputHTML.length());
		
		try
		{
			parser = new HTMLParser(reader, new DefaultHTMLParserFeedback());
			parser.registerScanners(); // Register standard scanners (Very Important)
		}
		catch (Exception e)
		{
			logger.error(e);
		}
	}

	
    public LinkExtractor(String inputHTML, String id, String duplicationYN)
    {
        this.inputHTML = inputHTML;
    	
        this.duplicationYN = duplicationYN;

        StringReader sr = new StringReader(inputHTML);
        reader = new HTMLReader(new BufferedReader(sr), inputHTML.length());

        try
        {
            parser = new HTMLParser(reader, new DefaultHTMLParserFeedback());
            parser.registerScanners(); // Register standard scanners (Very Important)
        }
        catch (Exception e)
        {
        	logger.error(e);
        }
    }
   


    
    /**
     * <p>김유근 : 기존 4.5 소스 수정 : massmailInk
     */
    @SuppressWarnings("unchecked")
	public ArrayList<MassMailLink> massMailExtractAllLinks() throws Exception
	{
		HTMLNode node = null;
		HTMLLinkTag linkTag;

		ArrayList<MassMailLink> linkinfoList = new ArrayList<MassMailLink>();
		StringBuffer resulthtml = new StringBuffer();
		
		String currentline = "";
		String link = "";
		String temp = "";
		String link_image = "";

		int count = 0;
		int countInMail = 0;

		for (HTMLEnumeration e = parser.elements(); e.hasMoreNodes();)
		{
			

			try
			{
				node = e.nextHTMLNode(); // Get the next HTML Node
				currentline = node.toHTML();
			}
			catch (Exception ee1)
			{
				logger.error(ee1);
				node = null;
			}

			if (node != null)
			{
				if (node instanceof HTMLLinkTag)
				{
					countInMail++;

					linkTag = (HTMLLinkTag) node; // Downcast to a Link Tag
					boolean isMailLink = (linkTag).isMailLink();

					if ( !isMailLink)
					{
						link = linkTag.getLink();

						//					link = StringUtility.replace(link, "?", "$q$");
						//					link = StringUtility.replace(link, "&", "$a$");
						//					link = StringUtility.replace(link, "=", "$e$");
						//Logger.debug.println("extractAllLinks " + link);

/*						temp_array = new String[5];
						temp_array[0] = link;
						temp_array[1] = Integer.toString(massmailID);
						temp_array[2] = String.valueOf(countInMail);*/
						
						MassMailLink massMailLink = new MassMailLink();
						
						massMailLink.setLinkURL(link);
						massMailLink.setMassmailID(massmailID);
						massMailLink.setLinkCount(countInMail);
						
						if ( link.equals(rejectLinkURL) ) {
							massMailLink.setLinkType("2");
						}

						link_image = "";
						java.util.Enumeration er = linkTag.linkData();
						while (er.hasMoreElements())
						{
							HTMLNode o = (HTMLNode) er.nextElement();

							if (o instanceof HTMLImageTag)
							{
								HTMLImageTag imageTag = (HTMLImageTag) o; // Downcast to a Link Tag
								link_image = imageTag.getImageURL();
							}
						}

						//System.out.println("getLinkText = " + linkTag.getLinkText());

						if (link_image.equals(""))
						{
							//temp_array[3] = linkTag.getLinkText();
							//temp_array[4] = "text";
							massMailLink.setLinkName(linkTag.getLinkText());
							massMailLink.setLinkDesc("text");
							
						}
						else
						{
							//temp_array[3] = link_image;
							//temp_array[4] = "image";
							massMailLink.setLinkName(link_image);
							massMailLink.setLinkDesc("image");
							
						}
						
						linkinfoList.add(massMailLink);
					}
				}
				if (!(node instanceof HTMLRemarkNode) && !(node instanceof HTMLStringNode))
				{
					String tagName = ((HTMLTag) node).getTagName();

					// 2004-02-04 이미지맵 링크 검사 추가
					if (tagName.equalsIgnoreCase("area"))
					{
						try
						{
							currentline =
								"<a " + currentline.trim().substring(currentline.indexOf(" "));
							currentline = currentline + "MAP</A>";
							//Logger.debug.println(classname + "@extractAllLinks: imagemap currentline = " + currentline);

							// 해당 이미맵링크를 일반 링크로 변환 한다.
							StringReader temp_sr = new StringReader(currentline);
							HTMLReader temp_reader =
								new HTMLReader(new BufferedReader(temp_sr), currentline.length());
							HTMLParser temp_parser = null;

							try
							{
								temp_parser =
									new HTMLParser(temp_reader, new DefaultHTMLParserFeedback());
								temp_parser.registerScanners();
								// Register standard scanners (Very Important)
							}
							catch (Exception ee1)
							{
								logger.error(ee1);
							}

							HTMLNode temp_node;
							HTMLLinkTag temp_linkTag;
							String temp_link = "";
							String temp_link_image = "";
							String temp_link_type = "";

							for (HTMLEnumeration e1 = temp_parser.elements(); e1.hasMoreNodes();)
							{
								temp_node = e1.nextHTMLNode(); // Get the next HTML Node
								if (temp_node instanceof HTMLLinkTag)
								{
									countInMail++;
									temp_linkTag = (HTMLLinkTag) temp_node;
									// Downcast to a Link Tag
									temp_link = temp_linkTag.getLink();

//									temp_link = StringUtility.replace(temp_link, "?", "$q$");
//									temp_link = StringUtility.replace(temp_link, "&", "$a$");
//									temp_link = StringUtility.replace(temp_link, "=", "$e$");
									//Logger.debug.println(classname + "@extractAllLinks: ImageMAP Link = " + temp_link);

								//	temp_array = new String[5];
								//	temp_array[0] = temp_link;
								//	temp_array[1] = Integer.toString(massmailID);
								//	temp_array[2] = String.valueOf(countInMail);
									
									MassMailLink massMailLink = new MassMailLink();
									massMailLink.setLinkURL(temp_link);
									massMailLink.setMassmailID(massmailID);
									massMailLink.setLinkCount(countInMail);

									// 해당 링크가 이미지 링크 인지 체크
									temp_link_image = "";
									java.util.Enumeration er1 = temp_linkTag.linkData();
									while (er1.hasMoreElements())
									{
										HTMLNode o = (HTMLNode) er1.nextElement();
										if (o instanceof HTMLImageTag)
										{
											HTMLImageTag imageTag = (HTMLImageTag) o;
											// Downcast to a Link Tag
											temp_link_image = imageTag.getImageURL();
										}
									}

									if (temp_link_image.equals(""))
									{
										//temp_array[3] = temp_linkTag.getLinkText();
										//temp_array[4] = "text";
										massMailLink.setLinkName(temp_linkTag.getLinkText());
										massMailLink.setLinkDesc("text");
									}
									else
									{
										//temp_array[3] = temp_link_image;
										//temp_array[4] = "image";
										massMailLink.setLinkName(temp_link_image);
										massMailLink.setLinkDesc("image");
									}
									

									linkinfoList.add(massMailLink);

								}

							}
						}
						catch (Exception e1)
						{
							logger.error(e1);
						}
					}
				}
			}
		} // end of for

		return linkinfoList;
	}
    
    

    @SuppressWarnings("unchecked")
	public Collection extractAllLinks() throws HTMLParserException, Exception
	{
		HTMLNode node = null;
		HTMLLinkTag linkTag;

		Collection linkinfoList = new ArrayList();
		StringBuffer resulthtml = new StringBuffer();
		String[] temp_array = null;
		String currentline = "";
		String link = "";
		String temp = "";
		String link_image = "";

		int count = 0;
		int countInMail = 0;

		for (HTMLEnumeration e = parser.elements(); e.hasMoreNodes();)
		{
			try
			{
				node = e.nextHTMLNode(); // Get the next HTML Node
				currentline = node.toHTML();
			}
			catch (Exception ee1)
			{
				logger.error(e);
				node = null;
			}

			if (node != null)
			{
				if (node instanceof HTMLLinkTag)
				{
					countInMail++;

					linkTag = (HTMLLinkTag) node; // Downcast to a Link Tag
					boolean isMailLink = (linkTag).isMailLink();

					if ( !isMailLink)
					{
						link = linkTag.getLink();

						//					link = StringUtility.replace(link, "?", "$q$");
						//					link = StringUtility.replace(link, "&", "$a$");
						//					link = StringUtility.replace(link, "=", "$e$");
						//Logger.debug.println("extractAllLinks " + link);

						temp_array = new String[5];
						temp_array[0] = link;
						temp_array[1] = Integer.toString(massmailID);
						temp_array[2] = String.valueOf(countInMail);

						link_image = "";
						java.util.Enumeration er = linkTag.linkData();
						while (er.hasMoreElements())
						{
							HTMLNode o = (HTMLNode) er.nextElement();

							if (o instanceof HTMLImageTag)
							{
								HTMLImageTag imageTag = (HTMLImageTag) o; // Downcast to a Link Tag
								link_image = imageTag.getImageURL();
							}
						}

						//System.out.println("getLinkText = " + linkTag.getLinkText());

						if (link_image.equals(""))
						{
							temp_array[3] = linkTag.getLinkText();
							temp_array[4] = "text";
						}
						else
						{
							temp_array[3] = link_image;
							temp_array[4] = "image";
						}

						linkinfoList.add(temp_array);
					}
				}
				if (!(node instanceof HTMLRemarkNode) && !(node instanceof HTMLStringNode))
				{
					String tagName = ((HTMLTag) node).getTagName();

					// 2004-02-04 이미지맵 링크 검사 추가
					if (tagName.equalsIgnoreCase("area"))
					{
						try
						{
							currentline =
								"<a " + currentline.trim().substring(currentline.indexOf(" "));
							currentline = currentline + "MAP</A>";
							//Logger.debug.println(classname + "@extractAllLinks: imagemap currentline = " + currentline);

							// 해당 이미맵링크를 일반 링크로 변환 한다.
							StringReader temp_sr = new StringReader(currentline);
							HTMLReader temp_reader =
								new HTMLReader(new BufferedReader(temp_sr), currentline.length());
							HTMLParser temp_parser = null;

							try
							{
								temp_parser =
									new HTMLParser(temp_reader, new DefaultHTMLParserFeedback());
								temp_parser.registerScanners();
								// Register standard scanners (Very Important)
							}
							catch (Exception ee1)
							{
								logger.error(e);
							}

							HTMLNode temp_node;
							HTMLLinkTag temp_linkTag;
							String temp_link = "";
							String temp_link_image = "";
							String temp_link_type = "";

							for (HTMLEnumeration e1 = temp_parser.elements(); e1.hasMoreNodes();)
							{
								temp_node = e1.nextHTMLNode(); // Get the next HTML Node
								if (temp_node instanceof HTMLLinkTag)
								{
									countInMail++;
									temp_linkTag = (HTMLLinkTag) temp_node;
									// Downcast to a Link Tag
									temp_link = temp_linkTag.getLink();

//									temp_link = StringUtility.replace(temp_link, "?", "$q$");
//									temp_link = StringUtility.replace(temp_link, "&", "$a$");
//									temp_link = StringUtility.replace(temp_link, "=", "$e$");
									//Logger.debug.println(classname + "@extractAllLinks: ImageMAP Link = " + temp_link);

									temp_array = new String[5];
									temp_array[0] = temp_link;
									temp_array[1] = Integer.toString(massmailID);
									temp_array[2] = String.valueOf(countInMail);

									// 해당 링크가 이미지 링크 인지 체크
									temp_link_image = "";
									java.util.Enumeration er1 = temp_linkTag.linkData();
									while (er1.hasMoreElements())
									{
										HTMLNode o = (HTMLNode) er1.nextElement();
										if (o instanceof HTMLImageTag)
										{
											HTMLImageTag imageTag = (HTMLImageTag) o;
											// Downcast to a Link Tag
											temp_link_image = imageTag.getImageURL();
										}
									}

									if (temp_link_image.equals(""))
									{
										temp_array[3] = temp_linkTag.getLinkText();
										temp_array[4] = "text";
									}
									else
									{
										temp_array[3] = temp_link_image;
										temp_array[4] = "image";
									}
									linkinfoList.add(temp_array);

								}

							}
						}
						catch (Exception e1)
						{
							logger.error(e);
						}
					}
				}
			}
		} // end of for

		return linkinfoList;
	}

    @SuppressWarnings("unchecked")
	public String extractLinks() throws HTMLParserException
	{
		HTMLNode node;
		HTMLLinkTag linkTag;

		StringBuffer resulthtml = new StringBuffer();
		String currentline;
		String link;
		String temp;
		String link_image;
		String link_type;
		String encode_link_image;

		int count = 0; // target 이 있는 칸수
		int countInMail = 0; // 해당 메일에서의 링크의 고유 번호

		for (HTMLEnumeration e = parser.elements(); e.hasMoreNodes();)
		{
			node = e.nextHTMLNode(); // Get the next HTML Node
			if (node instanceof HTMLLinkTag)
			{
				countInMail++;
				linkTag = (HTMLLinkTag) node; // Downcast to a Link Tag
				link = linkTag.getLink();

				// 2004-04-28, escape 로 인코딩 하기 때무에 필요 없음
				//				link = StringUtility.replace(link, "?", "$q$");
				//				link = StringUtility.replace(link, "&", "$a$");
				//				link = StringUtility.replace(link, "=", "$e$");

				//Logger.debug.println(classname + "@extractLinks: HTMLLinkTag = " + link);

				link_image = "";

				java.util.Enumeration er = linkTag.linkData();
				while (er.hasMoreElements())
				{
					HTMLNode o = (HTMLNode) er.nextElement();
					if (o instanceof HTMLImageTag)
					{
						HTMLImageTag imageTag = (HTMLImageTag) o; // Downcast to a Link Tag
						link_image = imageTag.getImageURL();
					}
				}

				if (link_image.equals(""))
				{
					link_image = linkTag.getLinkText();
					link_type = "text";
				}
				else
				{
					link_type = "image";
				}

				try
				{
					//encode_link_image = URLEncoder.encode( link_image, "UTF-8" );
					//encode_link_image = URLEncoder.encode( link_image );
					//2004-04-28, 따옴표나, 하나짜리 따옴표가 있을 경우
					// 스크립트가 제대로 작동을 안하는 경우가 있기 때문에
					// 미리 제거해 줍니다.
					encode_link_image = StringUtil.replace(link_image, "\"", "");
					encode_link_image = StringUtil.replace(encode_link_image, "'", "");
					encode_link_image = StringUtil.replace(encode_link_image, "·", "");
				}
				catch (Exception e1)
				{
					encode_link_image = link_image;
				}

				linkTag.setLink(
					"javascript:onOpen('"
						+ link
						+ "','"
						+ Integer.toString(massmailID)
						+ "','"
						+ countInMail
						+ "','"
						+ link_type
						+ "','"
						+ encode_link_image
						+ "')");

				// taget 이 없을 경우는 javascript 를 이용해서
				// POPUP
				if (linkTag.getParameter("target") != null)
				{
					currentline = node.toHTML();
					//Logger.debug.println(classname + "@extractLinks: HTMLLinkTag = " + currentline);
					temp = currentline.toLowerCase();
					count = temp.indexOf("target");
					//2004-04-26, target 항목을 없애기 위해 아래가 피요합니다.
					// 주석 처리 하면 안됩니다. 다시 주석 풀어 놓습니다. 김영문
//					currentline =
//						currentline.substring(0, count)
//							+ currentline.substring(count).replaceFirst(linkTag.getParameter("target"),"");
					//Logger.debug.println(classname + "@extractLinks: HTMLLinkTag = " + currentline + "count : " + count  );
					currentline =
						currentline.substring(0, count) + currentline.substring(count + 9);
				}
				else
				{
					currentline = node.toHTML();
				}
			}
			else
			{
				currentline = node.toHTML();
			}

			// 엔터처리
			if (node instanceof HTMLRemarkNode)
			{
				currentline = currentline + "\r\n";
			}
			else if (!(node instanceof HTMLStringNode))
			{
				String tagName = ((HTMLTag) node).getTagName();

				// 2004-02-04 이미지맵 링크 검사 추가
				if (tagName.equalsIgnoreCase("area"))
				{
					try
					{
						currentline =
							"<a " + currentline.trim().substring(currentline.indexOf(" "));
						currentline = currentline + "MAP</A>";
						logger.error(e);
						// 해당 이미맵링크를 일반 링크로 변환 한다.
						StringReader temp_sr = new StringReader(currentline);
						HTMLReader temp_reader =
							new HTMLReader(new BufferedReader(temp_sr), currentline.length());
						HTMLParser temp_parser = null;

						try
						{
							temp_parser =
								new HTMLParser(temp_reader, new DefaultHTMLParserFeedback());
							temp_parser.registerScanners();
							// Register standard scanners (Very Important)
						}
						catch (Exception ee1)
						{
							logger.error(e);
						}

						HTMLNode temp_node;
						HTMLLinkTag temp_linkTag;
						String temp_link = "";
						String temp_link_image = "";
						String temp_link_type = "";

						for (HTMLEnumeration e1 = temp_parser.elements(); e1.hasMoreNodes();)
						{
							temp_node = e1.nextHTMLNode(); // Get the next HTML Node
							if (temp_node instanceof HTMLLinkTag)
							{
								countInMail++;
								temp_linkTag = (HTMLLinkTag) temp_node; // Downcast to a Link Tag
								temp_link = temp_linkTag.getLink();

								//								temp_link = StringUtility.replace(temp_link, "?", "$q$");
								//								temp_link = StringUtility.replace(temp_link, "&", "$a$");
								//								temp_link = StringUtility.replace(temp_link, "=", "$e$");
								logger.error(e);
								// 해당 링크가 이미지 링크 인지 체크
								temp_link_image = "";
								java.util.Enumeration er1 = temp_linkTag.linkData();
								while (er1.hasMoreElements())
								{
									HTMLNode o = (HTMLNode) er1.nextElement();
									if (o instanceof HTMLImageTag)
									{
										HTMLImageTag imageTag = (HTMLImageTag) o;
										// Downcast to a Link Tag
										temp_link_image = imageTag.getImageURL();
									}
								}

								if (temp_link_image.equals(""))
								{
									temp_link_image = temp_linkTag.getLinkText();
									temp_link_type = "text";
								}
								else
								{
									temp_link_type = "image";
								}

								try
								{
									//encode_link_image = URLEncoder.encode( link_image, "UTF-8" );
									//encode_link_image = URLEncoder.encode( link_image );
									//2004-04-28, 따옴표나, 하나짜리 따옴표가 있을 경우
									// 스크립트가 제대로 작동을 안하는 경우가 있기 때문에
									// 미리 제거해 줍니다.
									temp_link_image =
										StringUtil.replace(temp_link_image, "\"", "");
									temp_link_image =
										StringUtil.replace(temp_link_image, "'", "");
									temp_link_image =
										StringUtil.replace(temp_link_image, "·", "");
								}
								catch (Exception e2)
								{
									//temp_link_image = temp_link_image;
								}

								temp_linkTag.setLink(
									"javascript:onOpen('"
										+ temp_link
										+ "','"
										+ Integer.toString(massmailID)
										+ "','"
										+ countInMail
										+ "','"
										+ temp_link_type
										+ "','"
										+ temp_link_image
										+ "_"
										+ countInMail
										+ "')");

								// taget 이 없을 경우는 javascript 를 이용해서
								// POPUP
								if (temp_linkTag.getParameter("target") != null)
								{
									currentline = temp_node.toHTML();
									temp = currentline.toLowerCase();
									count = temp.indexOf("target");
									currentline =
										currentline.substring(0, count)
											+ currentline.substring(count + 6);
									currentline = StringUtil.replace(currentline, "MAP</A>", "");
									currentline =
										StringUtil.replace(currentline, "<A ", "\r\n<AREA ");
								}
								else
								{
									currentline = temp_node.toHTML();
									currentline = StringUtil.replace(currentline, "MAP</A>", "");
									currentline =
										StringUtil.replace(currentline, "<A ", "\r\n<AREA ");
								}
							}
							else
							{
								currentline = temp_node.toHTML();
							}
						}
					}
					catch (Exception e1)
					{
						logger.error(e);
					}
					currentline = currentline + "\r\n";
				}

				if (tagName.equalsIgnoreCase("br"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("p"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("script"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("style"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("html"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("head"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("title"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("body"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("table"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("tr"))
				{
					currentline = currentline + "\r\n";
				}
				else if (tagName.equalsIgnoreCase("td"))
				{
					currentline = currentline + "\r\n";
				}
			}

			resulthtml.append(currentline);
		} // end of for

		//Logger.debug.println(resulthtml);
		return resulthtml.toString();
	}

}