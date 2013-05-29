package web.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;
import org.openflashchart.chart.Chart;
import org.openflashchart.component.Labels;
import org.openflashchart.component.Title;
import org.openflashchart.component.ToolTip;
import org.openflashchart.component.Values;
import org.openflashchart.component.X_Axis;
import org.openflashchart.component.Y_Axis;
import org.openflashchart.component.Y_Axis_Right;
import org.openflashchart.component.Y_Legend;
import org.openflashchart.elements.Bar_Glass;
import org.openflashchart.elements.DotStyle;
import org.openflashchart.elements.Line;
import org.openflashchart.elements.On__Show;
import org.openflashchart.elements.Pie;

import web.massmail.statistic.model.MassMailStatistic;
import web.massmail.statistic.model.MassMailReportMonth;


/**
 * <p>차트를 만드는 유틸 
 *
 */
@SuppressWarnings("unchecked")
public class ChartUtil {
	
	
	
	/**
	 * <p>파이 공통 함수 
	 * @return
	 */
	private static Pie commonPieTypeA(){
		Pie pie = new Pie();	
		pie.setBorder(2.0);
		
		pie.setColours(new Object[] {
				"0x336699", "0x88AACC", "0x999933", "0x666699",
				"0xCC9933", "0x006666", "0x3399FF", "0x993300",
				"0xAAAA77", "0x666666", "0xFFCC66", "0x6699CC",
				"0x663366", "0x9999CC", "0xAAAAAA", "0x669999",
				"0xBBBB55", "0xCC6600", "0x9999FF", "0x0066CC",
				"0x99CCCC", "0x999999", "0xFFCC00", "0x009999",
		});
		
		pie.setAlpha(0.2);
		pie.setStart__angle(135.0);
		pie.setAnimate(true);
		pie.setNo__labels(false);
		return pie;
	}
	
	
	/**
	 * <p>바 설정
	 * @param alpha
	 * @param color
	 * @param text
	 * @param type
	 * @param cascade
	 * @param delay
	 * @param tip
	 * @return
	 */
	private static Bar_Glass setBar(double alpha, String color, String text, String type, double cascade, double delay, String tip){
		Bar_Glass setBar = new Bar_Glass();
		setBar.setAlpha(alpha);
		setBar.setColour(color);
		setBar.setText(text);
		setBar.setOn__show(new On__Show().setType(type).setCascade(cascade).setDelay(delay));
		setBar.setTip(tip);
		return setBar;
	}
	
	
	/**
	 * <p>측정 바가 고정되어 있지 않고 최고값을 별도로 표시하는 바차트(일자별, 도메인별등....)
	 * @return
	 */
	public static String createMailChartAllBar(ArrayList<Map<String,Object>> statisticArrayList) throws Exception{
		
		Chart chart = new Chart();
		
		Title title = new Title();
		title.setText("");
		
		Y_Legend yLegend = new Y_Legend();
		yLegend.setText("건수");
		yLegend.setStyle("{color: #909090; font-size: 12px;}");
		
		Bar_Glass successBar = setBar(0.5,"#3334AD","성공","pop",1,1.5,"#val# 건 성공");		
		Bar_Glass failBar = setBar(0.5,"#FF6600","실패","drop",0.9,0,"#val# 건 실패");
		Bar_Glass openBar = setBar(0.5,"#66CC00","오픈","grow-up",0.8,0,"#val# 건 오픈");
		Bar_Glass clickBar = setBar(0.5,"#FFBF55","클릭","drop",0.7,0,"#val# 건 클릭");
		Bar_Glass rejectBar = setBar(0.5,"#FF0000","수신거부","grow-up",0.6,0,"#val# 건 수신거부");

		
		Line line = new Line();
		line.setText("성공율(%)");
		line.setColour("#AAAAAA");
		line.setWidth(1);
		
		line.setOn__show(new On__Show().setType("shrink-in").setCascade(1).setDelay(2.5));
		line.setDot__style(new DotStyle().setType("solid-dot").setColour("#a44a80").setDot__size(3).setTip("#val#% 성공 <br>#x_label#"));
		line.setAxis("right");
		
		X_Axis xAxis = new X_Axis();
		xAxis.setStroke(1);
		xAxis.setTick__height(10);
		xAxis.setColour("#909090");
		xAxis.setGrid__colour("#C8D0D2");
		
		Y_Axis yAxis = new Y_Axis();
		yAxis.setStroke(2);
		yAxis.setTick_length(10);
		yAxis.setColour("#909090");
		yAxis.setGrid__colour("#C8D0D2");
		yAxis.setOffset(false);
		
		Y_Axis_Right yAxisRight = new Y_Axis_Right();
		yAxisRight.setStroke(2);
		yAxisRight.setSteps("10");
		yAxisRight.setTick_length(10);
		yAxisRight.setColour("#909090");
		yAxisRight.setGrid__colour("#C8D0D2");
		yAxisRight.setOffset(false);
		yAxisRight.setMax("100");
		
		
		ToolTip toolTip = new ToolTip();
		toolTip.setShadow(true);
		toolTip.setStroke(2);
		toolTip.setColour("#C5ADAB");
		toolTip.setBackground("#FFFFEE");
		toolTip.setTitle("{font-size: 12px; font-weight: bold; color: #0000FF;}");
		toolTip.setBody("{font-size: 11px; color: #000000;}");
		
		ArrayList lineValues = new ArrayList();
		ArrayList successValues = new ArrayList();
		ArrayList openValues = new ArrayList();
		ArrayList failValues = new ArrayList();
		ArrayList clickValues = new ArrayList();
		ArrayList rejectValues = new ArrayList();
		
		Labels xAisLabel = new Labels().setRotate(270);
		ArrayList xAxisLabels = new ArrayList();
		
		for( Map<String,Object> statistic :statisticArrayList ){
			xAxisLabels.add(statistic.get("standard"));			
			failValues.add(statistic.get("failTotal"));
			lineValues.add(statistic.get("successRatio"));
			successValues.add(statistic.get("successTotal") );
			openValues.add(statistic.get("openTotal") );
			clickValues.add(statistic.get("clickTotal"));
			rejectValues.add(statistic.get("rejectTotal"));
		}
		

		successBar.setValues(successValues);
		openBar.setValues(openValues);
		failBar.setValues(failValues);
		clickBar.setValues(clickValues);
		rejectBar.setValues(rejectValues);
		line.setValues(lineValues);

		xAisLabel.setLabels(xAxisLabels);
		xAxis.setList_labels(xAisLabel);
		
		
		// x좌표의 최고값 찾기
		BigDecimal curValue = successBar.getMaxValue();
		BigDecimal maxValue = new BigDecimal(0);
		if(curValue.compareTo(maxValue) > 0 ) maxValue = curValue;
		curValue = failBar.getMaxValue();
		if(curValue.compareTo(maxValue) > 0 ) maxValue = curValue;
		curValue = openBar.getMaxValue();
		if(curValue.compareTo(maxValue) > 0 ) maxValue = curValue;

		yAxis.setAutoMax(maxValue.doubleValue());
		
		chart.setTitle(title);
		chart.setBg_colour("#ffffff");
		chart.setY_Legend(yLegend);
		
		chart.setLine(line);
		chart.setX_Axis(xAxis);
		chart.setY_Axis(yAxis);
		chart.setY_Axis_Rigth(yAxisRight);
		
		chart.setBar_Glass(successBar);
		chart.setBar_Glass(failBar);
		chart.setBar_Glass(openBar);
		chart.setBar_Glass(clickBar);
		chart.setBar_Glass(rejectBar);
		
		chart.setToolTip(toolTip);
		
		return chart.createChart();
		
	}
	
	
	/**
	 * <p>시간별 차트 
	 * @param statisticArrayList
	 * @return
	 * @throws Exception
	 */
	public static String createMailChartTimeBar(Map<Integer, MassMailStatistic> map) throws Exception{
		Chart chart = new Chart();
		
		Title title = new Title();
		title.setText("");
		
		Y_Legend yLegend = new Y_Legend();
		yLegend.setText("건수");
		yLegend.setStyle("{color: #909090; font-size: 12px;}");
		
		Bar_Glass successBar = setBar(0.5,"#3334AD","성공","pop",1,1.5,"#val# 건 성공");		
		Bar_Glass failBar = setBar(0.5,"#FF6600","실패","drop",0.9,0,"#val# 건 실패");
		Bar_Glass openBar = setBar(0.5,"#66CC00","오픈","grow-up",0.8,0,"#val# 건 오픈");
		Bar_Glass clickBar = setBar(0.5,"#FFBF55","클릭","drop",0.7,0,"#val# 건 클릭");
		Bar_Glass rejectBar = setBar(0.5,"#FF0000","수신거부","grow-up",0.6,0,"#val# 건 수신거부");
		
		Line line = new Line();
		line.setText("성공율(%)");
		line.setColour("#AAAAAA");
		line.setWidth(1);
		
		line.setOn__show(new On__Show().setType("shrink-in").setCascade(1).setDelay(2.5));
		line.setDot__style(new DotStyle().setType("solid-dot").setColour("#a44a80").setDot__size(3).setTip("#val#% 성공 <br>#x_label#"));
		line.setAxis("right");
		
		X_Axis xAxis = new X_Axis();
		xAxis.setStroke(1);
		xAxis.setTick__height(10);
		xAxis.setColour("#909090");
		xAxis.setGrid__colour("#C8D0D2");
		
		Y_Axis yAxis = new Y_Axis();
		yAxis.setStroke(2);
		yAxis.setTick_length(10);
		yAxis.setColour("#909090");
		yAxis.setGrid__colour("#C8D0D2");
		yAxis.setOffset(false);
		
		Y_Axis_Right yAxisRight = new Y_Axis_Right();
		yAxisRight.setStroke(2);
		yAxisRight.setSteps("10");
		yAxisRight.setTick_length(10);
		yAxisRight.setColour("#909090");
		yAxisRight.setGrid__colour("#C8D0D2");
		yAxisRight.setOffset(false);
		yAxisRight.setMax("100");
				
		ToolTip toolTip = new ToolTip();
		toolTip.setShadow(true);
		toolTip.setStroke(2);
		toolTip.setColour("#C5ADAB");
		toolTip.setBackground("#FFFFEE");
		toolTip.setTitle("{font-size: 12px; font-weight: bold; color: #0000FF;}");
		toolTip.setBody("{font-size: 11px; color: #000000;}");
		
		//Map<Integer, ArrayList<Map<String,Object>>>map = new HashMap<Integer, ArrayList<Map<String,Object>>>(); 
		//for(Map<String,Object> statistic : statisticArrayList)
		//{
			//map.put(Integer.parseInt(String.valueOf(statistic.get("standard"))),statisticArrayList);
		//}
		ArrayList lineValues = new ArrayList();
		ArrayList successValues = new ArrayList();
		ArrayList openValues = new ArrayList();
		ArrayList failValues = new ArrayList();
		ArrayList clickValues = new ArrayList();
		ArrayList rejectValues = new ArrayList();
		
		Labels xAisLabel = new Labels().setRotate(270);
		ArrayList xAxisLabels = new ArrayList();
							
		for(int i = 0; i < 24; i ++) {
			xAxisLabels.add(StringUtil.toFormatStr("00",(double)i)+"~"+StringUtil.toFormatStr("00",(double)i+1)+"시"); 
			
			if(map.containsKey(i)) {				
				// 통계 담기	
				failValues.add(map.get(i).getFailTotal());
				lineValues.add( map.get(i).getSuccessRatio() );
				successValues.add( map.get(i).getSuccessTotal() );
				openValues.add( map.get(i).getOpenTotal() );
				clickValues.add(map.get(i).getClickTotal());
				rejectValues.add(map.get(i).getRejectcallTotal());
		
				
			} else {
				lineValues.add(0);
				successValues.add(0);
				failValues.add(0);
				openValues.add(0);
				clickValues.add(0);
				rejectValues.add(0);
			}
			
		}

		successBar.setValues(successValues);
		openBar.setValues(openValues);
		failBar.setValues(failValues);
		clickBar.setValues(clickValues);
		rejectBar.setValues(rejectValues);
		line.setValues(lineValues);

		xAisLabel.setLabels(xAxisLabels);
		xAxis.setList_labels(xAisLabel);
		
		
		// x좌표의 최고값 찾기
		BigDecimal curValue = successBar.getMaxValue();
		BigDecimal maxValue = new BigDecimal(0);
		if(curValue.compareTo(maxValue) > 0 ) maxValue = curValue;
		curValue = failBar.getMaxValue();
		if(curValue.compareTo(maxValue) > 0 ) maxValue = curValue;
		curValue = openBar.getMaxValue();
		if(curValue.compareTo(maxValue) > 0 ) maxValue = curValue;

		yAxis.setAutoMax(maxValue.doubleValue());
		
		chart.setTitle(title);
		chart.setBg_colour("#ffffff");
		chart.setY_Legend(yLegend);
		
		chart.setLine(line);
		chart.setX_Axis(xAxis);
		chart.setY_Axis(yAxis);
		chart.setY_Axis_Rigth(yAxisRight);
		
		chart.setBar_Glass(successBar);
		chart.setBar_Glass(failBar);
		chart.setBar_Glass(openBar);
		chart.setBar_Glass(clickBar);
		chart.setBar_Glass(rejectBar);
		
		chart.setToolTip(toolTip);
		
		return chart.createChart();
	}
	

	/**
	 * <p>시간별 차트 
	 * @param statisticArrayList
	 * @return
	 * @throws Exception
	 */
	public static String createMailMonthChartTimeBar(Map<Integer, MassMailReportMonth> map) throws Exception{
		Chart chart = new Chart();
		
		Title title = new Title();
		title.setText("");
		
		Y_Legend yLegend = new Y_Legend();
		yLegend.setText("건수");
		yLegend.setStyle("{color: #909090; font-size: 12px;}");
		
		Bar_Glass successBar = setBar(0.5,"#3334AD","성공","pop",1,1.5,"#val# 건 성공");		
		Bar_Glass failBar = setBar(0.5,"#FF6600","실패","drop",0.9,0,"#val# 건 실패");
		Bar_Glass openBar = setBar(0.5,"#66CC00","오픈","grow-up",0.8,0,"#val# 건 오픈");
		Bar_Glass clickBar = setBar(0.5,"#FFBF55","클릭","drop",0.7,0,"#val# 건 클릭");
		Bar_Glass rejectBar = setBar(0.5,"#FF0000","수신거부","grow-up",0.6,0,"#val# 건 수신거부");
		
		X_Axis xAxis = new X_Axis();
		xAxis.setStroke(1);
		xAxis.setTick__height(10);
		xAxis.setColour("#909090");
		xAxis.setGrid__colour("#C8D0D2");
		
		Y_Axis yAxis = new Y_Axis();
		yAxis.setStroke(2);
		yAxis.setTick_length(10);
		yAxis.setColour("#909090");
		yAxis.setGrid__colour("#C8D0D2");
		yAxis.setOffset(false);
		
		Y_Axis_Right yAxisRight = new Y_Axis_Right();
		yAxisRight.setStroke(2);
		yAxisRight.setSteps("10");
		yAxisRight.setTick_length(10);
		yAxisRight.setColour("#909090");
		yAxisRight.setGrid__colour("#C8D0D2");
		yAxisRight.setOffset(false);
		yAxisRight.setMax("100");
				
		ToolTip toolTip = new ToolTip();
		toolTip.setShadow(true);
		toolTip.setStroke(2);
		toolTip.setColour("#C5ADAB");
		toolTip.setBackground("#FFFFEE");
		toolTip.setTitle("{font-size: 12px; font-weight: bold; color: #0000FF;}");
		toolTip.setBody("{font-size: 11px; color: #000000;}");
		
		ArrayList successValues = new ArrayList();
		ArrayList openValues = new ArrayList();
		ArrayList failValues = new ArrayList();
		ArrayList clickValues = new ArrayList();
		ArrayList rejectValues = new ArrayList();
		
		Labels xAisLabel = new Labels().setRotate(270);
		ArrayList xAxisLabels = new ArrayList();
							
		for(int i = 0; i < 24; i ++) {
			xAxisLabels.add(StringUtil.toFormatStr("00",(double)i)+"~"+StringUtil.toFormatStr("00",(double)i+1)+"시"); 
			
			if(map.containsKey(i)) {				
				// 통계 담기	
				failValues.add(map.get(i).getFailTotal());
				successValues.add( map.get(i).getSuccessTotal() );
				openValues.add( map.get(i).getOpenTotal() );
				clickValues.add(map.get(i).getClickTotal());
				rejectValues.add(map.get(i).getRejectcallTotal());
			} else {
				successValues.add(0);
				failValues.add(0);
				openValues.add(0);
				clickValues.add(0);
				rejectValues.add(0);
			}
			
		}

		successBar.setValues(successValues);
		openBar.setValues(openValues);
		failBar.setValues(failValues);
		clickBar.setValues(clickValues);
		rejectBar.setValues(rejectValues);
		//line.setValues(lineValues);

		xAisLabel.setLabels(xAxisLabels);
		xAxis.setList_labels(xAisLabel);
		
		
		// x좌표의 최고값 찾기
		BigDecimal curValue = successBar.getMaxValue();
		BigDecimal maxValue = new BigDecimal(0);
		if(curValue.compareTo(maxValue) > 0 ) maxValue = curValue;
		curValue = failBar.getMaxValue();
		if(curValue.compareTo(maxValue) > 0 ) maxValue = curValue;
		curValue = openBar.getMaxValue();
		if(curValue.compareTo(maxValue) > 0 ) maxValue = curValue;

		yAxis.setAutoMax(maxValue.doubleValue());
		
		chart.setTitle(title);
		chart.setBg_colour("#ffffff");
		chart.setY_Legend(yLegend);
		
		//chart.setLine(line);
		chart.setX_Axis(xAxis);
		chart.setY_Axis(yAxis);
		chart.setY_Axis_Rigth(yAxisRight);
		
		chart.setBar_Glass(successBar);
		chart.setBar_Glass(failBar);
		chart.setBar_Glass(openBar);
		chart.setBar_Glass(clickBar);
		chart.setBar_Glass(rejectBar);
		
		chart.setToolTip(toolTip);
		
		return chart.createChart();
	}
	
	
	/**
	 * <p>시간별 차트파이
	 * @param statisticArrayList
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String createMailChartTimePie(ArrayList<Map<String,Object>> statisticArrayList, String key) throws Exception{
	
		Chart chart = new Chart(); 		
		Pie pie = commonPieTypeA();
		
		Map<Integer, ArrayList<Map<String,Object>>>map = new HashMap<Integer, ArrayList<Map<String,Object>>>(); 
		for(Map<String,Object> statistic : statisticArrayList)
		{
			map.put(Integer.parseInt(String.valueOf(statistic.get("standard"))),statisticArrayList);
		}

		
		ArrayList pieValues = new ArrayList();
		String label;
		double value;
		
		for(int i = 0; i < 24; i ++) {
				
			label = StringUtil.toFormatStr("00",(double)i)+"~"+StringUtil.toFormatStr("00",(double)i+1)+"시 ";
			
			if(map.containsKey(i)) {				
				
				if(key.equals("success")){
					value = Integer.parseInt(String.valueOf(map.get(i).get(i).get("successTotal")));
					label += "성공"+StringUtil.toFormatStr("###,###,###",value)+"(성공율"+String.valueOf(map.get(i).get(i).get("successRatio"))+"%)";
				}else if(key.equals("fail")){
					value =  Integer.parseInt(String.valueOf(map.get(i).get(i).get("failTotal")));
					label += "실패"+StringUtil.toFormatStr("###,###,###",value)+"(실패율"+( 100 - Integer.parseInt(String.valueOf(map.get(i).get(i).get("successRatio"))))+"%)";
				}else if(key.equals("open")){
					value =   Integer.parseInt(String.valueOf(map.get(i).get(i).get("openTotal")));
					label += "오픈"+StringUtil.toFormatStr("###,###,###",value)+"(오픈율"+String.valueOf(map.get(i).get(i).get("successRatio"))+"%)";
				}else if(key.equals("click")){
					value =   Integer.parseInt(String.valueOf(map.get(i).get(i).get("clickTotal")));
					label += "클릭"+StringUtil.toFormatStr("###,###,###",value)+"(클릭율"+String.valueOf(map.get(i).get(i).get("successRatio"))+"%)";
				}else if(key.equals("reject")){
					value = Integer.parseInt(String.valueOf(map.get(i).get(i).get("rejectTotal")));
					label += "수신거부"+StringUtil.toFormatStr("###,###,###",value)+"(수신거부율"+String.valueOf(map.get(i).get(i).get("successRatio"))+"%)";
				}else{
					value = Integer.parseInt(String.valueOf(map.get(i).get(i).get("sendTotal")));
					label += "전송"+StringUtil.toFormatStr("###,###,###",value)+"(성공"+String.valueOf(map.get(i).get(i).get("successRatio"))+"%)";
				}

			} else {

				label += "건수 없음";
				value = 1;
				
			}
			
			pieValues.add(new Values().setValue(value).setLabel(label));
		}
		
		pie.setValues(pieValues);
		pie.setTip("#label#");
		
		chart.setBg_colour("#ffffff");
		chart.setPie(pie);
		
		return chart.createChart();
		
	}
	
	
	
	/**
	 * <p>링크바차트 
	 * @param statisticArrayList
	 * @return
	 * @throws Exception
	 */
	public static String createMailChartLinkBar(ArrayList<Map<String,Object>> statisticArrayList) throws Exception{
		Chart chart = new Chart();
		
		Title title = new Title();
		title.setText("");
		
		Y_Legend yLegend = new Y_Legend();
		yLegend.setText("건수");
		yLegend.setStyle("{color: #909090; font-size: 12px;}");
		
		
		Bar_Glass clickBar = setBar(0.5,"#FF6600","클릭","drop",0.9,0,"#val# 건 클릭");		

		
		X_Axis xAxis = new X_Axis();
		xAxis.setStroke(1);
		xAxis.setTick__height(10);
		xAxis.setColour("#909090");
		xAxis.setGrid__colour("#C8D0D2");
		
		Y_Axis yAxis = new Y_Axis();
		yAxis.setStroke(2);
		yAxis.setTick_length(10);
		yAxis.setColour("#909090");
		yAxis.setGrid__colour("#C8D0D2");
		yAxis.setOffset(false);

		ToolTip toolTip = new ToolTip();
		toolTip.setShadow(true);
		toolTip.setStroke(2);
		toolTip.setColour("#C5ADAB");
		toolTip.setBackground("#FFFFEE");
		toolTip.setTitle("{font-size: 12px; font-weight: bold; color: #0000FF;}");
		toolTip.setBody("{font-size: 11px; color: #000000;}");

		ArrayList clickValues = new ArrayList();
		
		Labels xAisLabel = new Labels().setRotate(270);
		ArrayList xAxisLabels = new ArrayList();
		for(Map<String,Object> statistic : statisticArrayList ){			
			xAxisLabels.add("Link ID "+statistic.get("linkID") );
			clickValues.add(statistic.get("linkCount"));						
		}

		clickBar.setValues(clickValues);
		xAisLabel.setLabels(xAxisLabels);
		xAxis.setList_labels(xAisLabel);
		
		
		// x좌표의 최고값 찾기
		BigDecimal maxValue = clickBar.getMaxValue();
		yAxis.setAutoMax(maxValue.doubleValue());
		
		chart.setTitle(title);
		chart.setBg_colour("#ffffff");
		chart.setY_Legend(yLegend);
		chart.setX_Axis(xAxis);
		chart.setY_Axis(yAxis);
		chart.setBar_Glass(clickBar);
		chart.setToolTip(toolTip);
		
		return chart.createChart();
	}
	
	
	/**
	 * <p>고정되어 있지 않는 차트파이
	 * @param statisticArrayList
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String createMailChartAllPie(ArrayList<Map<String,Object>> statisticArrayList, String key) throws Exception{
		
		Chart chart = new Chart();		
		Pie pie = commonPieTypeA();
		
		ArrayList pieValues = new ArrayList();
		String label;
		double value;

		for( Map<String,Object> statistic :statisticArrayList ){
				label = String.valueOf(statistic.get("standard"));//massMailStatistic.getStandard();

				if(key.equals("success")){
					value =  Integer.parseInt(String.valueOf(statistic.get("successTotal")))+1;
					label += " 성공"+StringUtil.toFormatStr("###,###,###",value-1)+"(성공율"+String.valueOf(statistic.get("successRatio"))+"%)";
				}else if(key.equals("fail")){
					value = Integer.parseInt(String.valueOf(statistic.get("failTotal")))+1; 
					label += " 실패"+StringUtil.toFormatStr("###,###,###",value-1)+"(실패율"+String.valueOf(statistic.get("successRatio"))+"%)";
				}else if(key.equals("open")){
					value = Integer.parseInt(String.valueOf(statistic.get("openTotal")))+1; 
					label += " 오픈"+StringUtil.toFormatStr("###,###,###",value-1)+"(오픈율"+String.valueOf(statistic.get("successRatio"))+"%)";
				}else if(key.equals("click")){
					value = Integer.parseInt(String.valueOf(statistic.get("clickTotal")))+1; 
					label += " 클릭"+StringUtil.toFormatStr("###,###,###",value-1)+"(클릭율"+String.valueOf(statistic.get("successRatio"))+"%)";
				}else if(key.equals("reject")){
					value = Integer.parseInt(String.valueOf(statistic.get("rejectTotal")))+1; 
					label += " 수신거부"+StringUtil.toFormatStr("###,###,###",value+1)+"(수신거부율"+String.valueOf(statistic.get("successRatio"))+"%)";
				}else{
					value =Integer.parseInt(String.valueOf(statistic.get("sendTotal")))+1; 
					label += " 전송"+StringUtil.toFormatStr("###,###,###",value-1)+"(성공"+String.valueOf(statistic.get("successRatio"))+"%)";
				}
			pieValues.add(new Values().setValue(value).setLabel(label));
		}
		if(statisticArrayList.size() == 0)
		{
			label = "데이터 없음";
			value = 1;
			pieValues.add(new Values().setValue(value).setLabel(label));
		}
		
		pie.setValues(pieValues);
		pie.setTip("#label#");
		
		chart.setBg_colour("#ffffff");
		chart.setPie(pie);
		
		return chart.createChart();
	}
	
	
	/**
	 * <p>링크파이차트 
	 * @param statisticArrayList
	 * @return
	 * @throws Exception
	 */
	public static String createMailChartLinkPie(ArrayList<Map<String,Object>> statisticArrayList) throws Exception{
	
		Chart chart = new Chart();
		
		Pie pie = commonPieTypeA();
		
		ArrayList pieValues = new ArrayList();
		String label;
		double value;
		for( Map<String,Object> statistic :statisticArrayList ){
				label = "LinkID : "+String.valueOf(statistic.get("linkID"));
				value = Integer.parseInt(String.valueOf(statistic.get("linkCount")))+1;
				label += " (클릭수 : "+ String.valueOf(statistic.get("linkCount"))+")";
				
			pieValues.add(new Values().setValue(value).setLabel(label));
		}
		if(statisticArrayList.size() == 0)
		{
			label = "데이터 없음";
			value = 1;
			pieValues.add(new Values().setValue(value).setLabel(label));
		}
		
		pie.setValues(pieValues);
		pie.setTip("#label#");
		
		chart.setBg_colour("#ffffff");
		chart.setPie(pie);
		
		return chart.createChart();
	}
	
	
	
	/**
	 * <p>실매도메인 차트BAR
	 * @param statisticArrayList
	 * @return
	 * @throws Exception
	 */
	public static String createMailFailDomainBar(ArrayList<Map<String,Object>> statisticArrayList) throws Exception{
	
		Chart chart = new Chart();
		
		Title title = new Title();
		title.setText("");
		
		Y_Legend yLegend = new Y_Legend();
		yLegend.setText("건수");
		yLegend.setStyle("{color: #909090; font-size: 12px;}");
		
		
		Bar_Glass type1Bar = setBar(0.5,"#3334AD","일시적인 오류","pop",1,1.5,"#val# 건 일시적인 오류");	
		Bar_Glass type2Bar = setBar(0.5,"#FF6600","영구적인 오류","drop",0.9,1,"#val# 건 영구적인 오류");	
		Bar_Glass type3Bar = setBar(0.5,"#66CC00","기타SMTP 오류","grow-up",0.8,0,"#val# 건 기타SMTP 오류");	
		Bar_Glass type4Bar = setBar(0.5,"#FFBF55","시스템 내부 오류","drop",0.7,0,"#val# 건 시스템 내부 오류");	
		Bar_Glass type5Bar = setBar(0.5,"#FF0000","불확실한 오류","grow-up",0.6,0,"#val# 건 불확실한 오류");	
	
		
		Line line = new Line();
		line.setText("총실패건수");
		line.setColour("#AAAAAA");
		line.setWidth(1);
		
		line.setOn__show(new On__Show().setType("shrink-in").setCascade(1).setDelay(2.5));
		line.setDot__style(new DotStyle().setType("solid-dot").setColour("#a44a80").setDot__size(3).setTip("총 실패 #val#건 <br>#x_label#"));
		line.setAxis("left");
		
		X_Axis xAxis = new X_Axis();
		xAxis.setStroke(1);
		xAxis.setTick__height(10);
		xAxis.setColour("#909090");
		xAxis.setGrid__colour("#C8D0D2");
		
		Y_Axis yAxis = new Y_Axis();
		yAxis.setStroke(2);
		yAxis.setTick_length(10);
		yAxis.setColour("#909090");
		yAxis.setGrid__colour("#C8D0D2");
		yAxis.setOffset(false);
					
		ToolTip toolTip = new ToolTip();
		toolTip.setShadow(true);
		toolTip.setStroke(2);
		toolTip.setColour("#C5ADAB");
		toolTip.setBackground("#FFFFEE");
		toolTip.setTitle("{font-size: 12px; font-weight: bold; color: #0000FF;}");
		toolTip.setBody("{font-size: 11px; color: #000000;}");
		
		ArrayList lineValues = new ArrayList();
		ArrayList type1Values = new ArrayList();
		ArrayList type2Values = new ArrayList();
		ArrayList type3Values = new ArrayList();
		ArrayList type4Values = new ArrayList();
		ArrayList type5Values = new ArrayList();
		
		Labels xAisLabel = new Labels().setRotate(270);
		ArrayList xAxisLabels = new ArrayList();
		
		for( Map<String,Object> statistic :statisticArrayList ){
			xAxisLabels.add(statistic.get("standard"));
			type1Values.add(statistic.get("temparyErrorCount")); 	 	//1: 일시적인 오류 카운트 
			type2Values.add(statistic.get("persistantErrorCount")) ;		//2: 영구적인 오류 카운트
			type3Values.add(statistic.get("etcSMTPErrorCount"));		//3: 기타 SMTP오류 카운트
			type4Values.add(statistic.get("systemErrorCount"));			//4: 시스템내부 오류 카운트
			type5Values.add(statistic.get("undefindErrorCount"));		//5: 불확실한 오류 카운트
			lineValues.add(statistic.get("failCount"));
		}
		

		type1Bar.setValues(type1Values);
		type2Bar.setValues(type2Values);
		type3Bar.setValues(type3Values);
		type4Bar.setValues(type4Values);
		type5Bar.setValues(type5Values);
		line.setValues(lineValues);

		xAisLabel.setLabels(xAxisLabels);
		xAxis.setList_labels(xAisLabel);
		
		yAxis.setAutoMax(line.getMaxValue().doubleValue());
		
		chart.setTitle(title);
		chart.setBg_colour("#ffffff");
		chart.setY_Legend(yLegend);
		
		chart.setLine(line);
		chart.setX_Axis(xAxis);
		chart.setY_Axis(yAxis);
		
		chart.setBar_Glass(type1Bar);
		chart.setBar_Glass(type2Bar);
		chart.setBar_Glass(type3Bar);
		chart.setBar_Glass(type4Bar);
		chart.setBar_Glass(type5Bar);
		
		chart.setToolTip(toolTip);
		
		return chart.createChart();
	
	}
	
	/**
	 * <p>실매도메인 차트PIE
	 * @param statisticArrayList
	 * @return
	 * @throws Exception
	 */
	public static String createMailFailDomainPie(ArrayList<Map<String,Object>> statisticArrayList, String key) throws Exception{

		Chart chart = new Chart(); 		
		Pie pie = commonPieTypeA();
	
		ArrayList pieValues = new ArrayList();
		String label;
		double value;
		for( Map<String,Object> statistic :statisticArrayList ){
			
			label = String.valueOf(statistic.get("standard"));
			if(key.equals("type1")){
				value = Integer.parseInt(String.valueOf(statistic.get("temparyErrorCount")))+1;
				label += " 일시적인오류"+StringUtil.toFormatStr("###,###,###",value-1);
			}else if(key.equals("type2")){
				value = Integer.parseInt(String.valueOf(statistic.get("persistantErrorCount")))+1;
				label += " 영구적인오류"+StringUtil.toFormatStr("###,###,###",value-1);
			}else if(key.equals("type3")){
				value = Integer.parseInt(String.valueOf(statistic.get("etcSMTPErrorCount")))+1;
				label += " 기타SMTP오류"+StringUtil.toFormatStr("###,###,###",value-1);
			}else if(key.equals("type4")){
				value = Integer.parseInt(String.valueOf(statistic.get("systemErrorCount")))+1;				
				label += " 시스템내부오류"+StringUtil.toFormatStr("###,###,###",value-1);
			}else if((key.equals("type5"))){
				value = Integer.parseInt(String.valueOf(statistic.get("undefindErrorCount")))+1;
				label += " 불확실한오류"+StringUtil.toFormatStr("###,###,###",value-1);
			}else{
				value = Double.parseDouble(String.valueOf(statistic.get("failCount")));
				label += " 전체"+StringUtil.toFormatStr("###,###,###",value);
			}
						
			pieValues.add(new Values().setValue(value).setLabel(label));
		}
		if(statisticArrayList.size() == 0)
		{
			label = "데이터 없음";
			value = 1;
			pieValues.add(new Values().setValue(value).setLabel(label));
		}
	
		
		pie.setValues(pieValues);
		pie.setTip("#label#");
		
		chart.setBg_colour("#ffffff");
		chart.setPie(pie);
		
		return chart.createChart();
	}
	
	
	/**
	 * <p>실패원인별 차트바
	 * @param statisticArrayList
	 * @return
	 * @throws Exception
	 */
	public static String createMailFailCauseBar(ArrayList<Map<String,Object>> statisticArrayList) throws Exception{
	
		Chart chart = new Chart();
		
		Title title = new Title();
		title.setText("");
		
		Y_Legend yLegend = new Y_Legend();
		yLegend.setText("");
		yLegend.setStyle("{color: #909090; font-size: 12px;}");
		
		Bar_Glass failBar = setBar(0.5,"#FF6600","실패","drop",0.9,0,"#val# 건 실패");	
		
		X_Axis xAxis = new X_Axis();
		xAxis.setStroke(1);
		xAxis.setTick__height(10);
		xAxis.setColour("#909090");
		xAxis.setGrid__colour("#C8D0D2");
		
		
		Y_Axis yAxis = new Y_Axis();
		yAxis.setStroke(2);
		yAxis.setTick_length(10);
		yAxis.setColour("#909090");
		yAxis.setGrid__colour("#C8D0D2");
		yAxis.setOffset(false);

		ToolTip toolTip = new ToolTip();
		toolTip.setShadow(true);
		toolTip.setStroke(2);
		toolTip.setColour("#C5ADAB");
		toolTip.setBackground("#FFFFEE");
		toolTip.setTitle("{font-size: 12px; font-weight: bold; color: #0000FF;}");
		toolTip.setBody("{font-size: 11px; color: #000000;}");

		ArrayList failValues = new ArrayList();
		
		Labels xAisLabel = new Labels().setRotate(270);
		ArrayList xAxisLabels = new ArrayList();
		for( Map<String,Object> statistic :statisticArrayList ){
			xAxisLabels.add(statistic.get("standard"));
			failValues.add(statistic.get("failCount"));			
		}

		
		failBar.setValues(failValues);
		xAisLabel.setLabels(xAxisLabels);
		xAxis.setList_labels(xAisLabel);
		
		
		// x좌표의 최고값 찾기
		BigDecimal maxValue = failBar.getMaxValue();
		yAxis.setAutoMax(maxValue.doubleValue());
		
		chart.setTitle(title);
		chart.setBg_colour("#ffffff");
		chart.setY_Legend(yLegend);
		chart.setX_Axis(xAxis);
		chart.setY_Axis(yAxis);
		chart.setBar_Glass(failBar);
		chart.setToolTip(toolTip);
		return chart.createChart();
	}
	
	
	/**
	 * <p>실패원인별 차트바
	 * @param statisticArrayList
	 * @return
	 * @throws Exception
	 */
	public static String createMailFailCausePie(ArrayList<Map<String,Object>> statisticArrayList) throws Exception{
	
		Chart chart = new Chart();
		Pie pie = commonPieTypeA();
		
		ArrayList pieValues = new ArrayList();
		String label;
		double value;
		for( Map<String,Object> statistic :statisticArrayList ){
			label = String.valueOf(statistic.get("standard"))+" : 실패 "+String.valueOf(statistic.get("failCount"))+"건";
			value = Integer.parseInt(String.valueOf(statistic.get("failCount"))) +1;
			pieValues.add(new Values().setValue(value).setLabel(label));
			
		}

		pie.setValues(pieValues);
		pie.setTip("#label#");
		
		chart.setBg_colour("#ffffff");
		chart.setPie(pie);
		
		return chart.createChart();
	}
}
