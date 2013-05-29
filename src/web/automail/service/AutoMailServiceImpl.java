package web.automail.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import web.automail.dao.AutoMailDAO;
import web.automail.model.AutoMailEvent;
import web.automail.model.FailCauseStatistic;
import web.automail.model.MailStatistic;
import web.common.util.StringUtil;
import web.common.util.DateUtils;

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
import org.openflashchart.elements.On__Show;
import org.openflashchart.elements.Line;
import org.openflashchart.elements.Pie;
import org.springframework.dao.DataAccessException;

public class AutoMailServiceImpl implements AutoMailService{
	
	private Logger logger = Logger.getLogger("TM");	
	private AutoMailDAO autoMailDAO = null;
	
	public void setAutoMailDAO(AutoMailDAO autoMailDAO){	
		this.autoMailDAO = autoMailDAO;
	}
	

	/**
	 * <p>자동메일리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<AutoMailEvent> listAutoMailEvents(int currentPage, int countPerPage,Map<String, String> searchMap){
		List<AutoMailEvent> result = null;
		try{
			result = autoMailDAO.listAutoMailEvents(currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>자동메일카운트 증가 
	 * @param searchMap
	 * @return
	 */
	public int getTotalCountAutoMailEvent(Map<String, String> searchMap){
		int result = 0;
		try{
			result = autoMailDAO.getTotalCountAutoMailEvent(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
		
	
	/**
	 * <p>자동메일이벤트등록 
	 * @param autoMailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAutoMailEvent(AutoMailEvent autoMailEvent){
		int result = 0;
		try{
			result = autoMailDAO.insertAutoMailEvent(autoMailEvent);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>자동메일이벤트수정
	 * @param autoMailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAutoMailEvent(AutoMailEvent autoMailEvent){
		int result = 0;
		try{
			result = autoMailDAO.updateAutoMailEvent(autoMailEvent);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>자동메일이벤트보기 
	 * @param automailID
	 * @return
	 */
	public AutoMailEvent viewAutoMailEvent(int automailID){
		AutoMailEvent autoMailEvent = null;
		try{
			autoMailEvent = autoMailDAO.viewAutoMailEvent(automailID);
		}catch(Exception e){
			logger.error(e);
		}
		return autoMailEvent;
	}
	
	/**
	 * <p>자동메일 레코드 삭제
	 * @param automailID
	 * @return
	 */
	public void deleteAutoMailEventAll(int automailID){
		autoMailDAO.deleteAutoMailEvent(automailID);
		autoMailDAO.deleteAutoMailSendQueue(automailID);
		autoMailDAO.deleteAutoMailDomainStatistic(automailID);
		autoMailDAO.deleteAutoMailFailStatistic(automailID);	
	}
	/**
	 * <p>자동메일 시간별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticHourlyList(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			List<MailStatistic> statisticHourlyList = autoMailDAO.statisticHourly(searchMap);
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>(); 
			for(MailStatistic mailStatistic : statisticHourlyList  ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
			statisticHourlyList.clear();
			for(int i = 0; i < 24; i ++) {
				MailStatistic tempMailStatistic = new MailStatistic();
				tempMailStatistic.setStandard(i+"");
				tempMailStatistic. setViewStandard(StringUtil.toFormatStr("00",(double)i)+"시 ~ " +StringUtil.toFormatStr("00",(double)i+1)+"시");
				if(map.containsKey(i)) {	
					tempMailStatistic.setSendTotal(map.get(i).getSendTotal());
					tempMailStatistic.setSuccessTotal(map.get(i).getSuccessTotal());
					tempMailStatistic.setFailTotal(map.get(i).getFailTotal());
					tempMailStatistic.setOpenTotal(map.get(i).getOpenTotal());
				}else{
					tempMailStatistic.setSendTotal(0);
					tempMailStatistic.setSuccessTotal(0);
					tempMailStatistic.setFailTotal(0);
					tempMailStatistic.setOpenTotal(0);
				}
				statisticHourlyList.add(tempMailStatistic);
			}
			result = statisticHourlyList;
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
		
	}
	
	/**
	 * <p>자동메일 시간별 챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public String statisticHourly(Map<String, Object> searchMap) {
		
		String retv = "";
		
		try{
			List<MailStatistic> mailStatisticList = autoMailDAO.statisticHourly(searchMap);
		
			Chart chart = new Chart();
			
			Title title = new Title();
			title.setText("");
			
			Y_Legend yLegend = new Y_Legend();
			yLegend.setText("TEST");
			yLegend.setStyle("{color: #909090; font-size: 12px;}");
			
			Bar_Glass successBar = new Bar_Glass();
			successBar.setAlpha(0.5);
			successBar.setColour("#3334AD");
			successBar.setText("성공");
			successBar.setOn__show(new On__Show().setType("pop").setCascade(1).setDelay(1.5));
			successBar.setTip("#val# 건 성공");
			
			//successBar.setValues(new Object[] {1000,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
			
			Bar_Glass failBar = new Bar_Glass();
			failBar.setAlpha(0.5);
			failBar.setText("실패");
			failBar.setColour("#FF6600");
			failBar.setOn__show(new On__Show().setType("drop").setCascade(0.9).setDelay(0));
			failBar.setTip("#val# 건 실패");
			
			//failBar.setValues(new Object[] { new Values().setTop(15.0) ,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
			
			
			Bar_Glass openBar = new Bar_Glass();
			openBar.setAlpha(0.5);
			openBar.setText("오픈");
			openBar.setColour("#66CC00");
			openBar.setOn__show(new On__Show().setType("grow-up").setCascade(0.8).setDelay(0));
			openBar.setTip("#val# 건 오픈");
			
			//openBar.setValues(new Object[] {9,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
	
			
			Line line = new Line();
			line.setText("성공율(%)");
			line.setColour("#AAAAAA");
			line.setWidth(1);
			
			line.setOn__show(new On__Show().setType("shrink-in").setCascade(1).setDelay(2.5));
			line.setDot__style(new DotStyle().setType("solid-dot").setColour("#a44a80").setDot__size(3).setTip("#val#% 성공 <br>#x_label#"));
			line.setAxis("right");
			
			
			X_Axis xAxis = new X_Axis();
			//xAxis.set___3d(12);
			xAxis.setStroke(1);
			//xAxis.setSteps("1");
			xAxis.setTick__height(10);
			xAxis.setColour("#909090");
			xAxis.setGrid__colour("#C8D0D2");
			//xAxis.setList_labels( new Labels().setRotate(270).setLabels(new Object[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"})  );
			//xAxis.setList_labels( new Object[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"});
			
			
			
			Y_Axis yAxis = new Y_Axis();
			//yAxis.setRotate("vertical");
			yAxis.setStroke(2);
			//yAxis.setSteps(100);
			yAxis.setTick_length(10);
			yAxis.setColour("#909090");
			yAxis.setGrid__colour("#C8D0D2");
			yAxis.setOffset(false);
			//yAxis.setMax("1000");
			
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
	
			
			
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>(); 
			for(MailStatistic mailStatistic : mailStatisticList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
	
			ArrayList lineValues = new ArrayList();
			ArrayList successValues = new ArrayList();
			ArrayList openValues = new ArrayList();
			ArrayList failValues = new ArrayList();
			
			Labels xAisLabel = new Labels().setRotate(270);
			ArrayList xAxisLabels = new ArrayList();
					
			
			for(int i = 0; i < 24; i ++) {
				
	
				xAxisLabels.add(StringUtil.toFormatStr("00",(double)i)+"~"+StringUtil.toFormatStr("00",(double)i+1)+"시"); 
				
				if(map.containsKey(i)) {
					
					// 실패 담기
					failValues.add(map.get(i).getFailTotal());
						
					lineValues.add( map.get(i).getSuccessRatio() );
					successValues.add( map.get(i).getSuccessTotal() );
					openValues.add( map.get(i).getOpenTotal() );
					
				} else {
					lineValues.add(0);
					successValues.add(0);
					failValues.add(0);
					openValues.add(0);
				}
				
			}
			
			
			successBar.setValues(successValues);
			openBar.setValues(openValues);
			failBar.setValues(failValues);
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
			//yAxis.setAutoMax(33600.0);
			
			
			
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
			
			chart.setToolTip(toolTip);
			
			retv = chart.createChart();
		
		}catch(Exception e){
			logger.error(e);
		}

		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 시간별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public String statisticHourlyPie(Map<String, Object> searchMap) {
		
		String retv = "";
		try
		{
			//double total = autoMailDAO.getSendTotal(automailID, startDate, endDate);
			
			List<MailStatistic> mailStatisticList = autoMailDAO.statisticHourly(searchMap);
			
			Chart chart = new Chart();
			
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
			
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>();
			for(MailStatistic mailStatistic : mailStatisticList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
	
			
			ArrayList pieValues = new ArrayList();
			String label;
			double value;
			for(int i = 0; i < 24; i ++) {
					
				label = StringUtil.toFormatStr("00",(double)i)+"~"+StringUtil.toFormatStr("00",(double)i+1)+"시 ";
	
				if(map.containsKey(i)) {

					value = map.get(i).getSendTotal();
					
					label += "전송"+StringUtil.toFormatStr("###,###,###",value)+"(성공"+StringUtil.toFormatStr("##.00", map.get(i).getSuccessRatio().doubleValue())+"%)";
					
					
				} else {
	
					label += "전송 건수 없음";
					value = 1;
					
				}
				
				pieValues.add(new Values().setValue(value).setLabel(label));
			}
			
			pie.setValues(pieValues);
			pie.setTip("#label#");
			
			chart.setBg_colour("#ffffff");
			chart.setPie(pie);
			
			retv = chart.createChart();
			return retv ;
			
		}catch(Exception e){
			logger.error(e);
		}

		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 일자별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDailyList(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			List<MailStatistic> statisticDailyList = autoMailDAO.statisticDaily(searchMap);
			
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>(); 
			for(MailStatistic mailStatistic : statisticDailyList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
			statisticDailyList.clear();
			String year = (String)searchMap.get("year");
			String month = (String)searchMap.get("month");
			String day = (String)searchMap.get("day");
			
			String startDate = year+month;
			String endDate = year+month;
			
			if(day.equals("all")){
				startDate+="01";
				endDate=DateUtils.lastDayOfMonth(startDate);
				//System.out.println(endDate);
			}else{
				startDate += day;
				endDate += day;
			}
			
			int count = DateUtils.daysBetween1(startDate, endDate);
			
			for(int i = 0; i <= count; i ++) {
				int key_value = new Integer(DateUtils.addDays(startDate, i));
				
				MailStatistic tempMailStatistic = new MailStatistic();
				tempMailStatistic.setStandard(key_value+"");
				tempMailStatistic. setViewStandard(DateUtils.reFormat(key_value+"","yyyymmdd","yyyy-mm-dd"));
				if(map.containsKey(key_value)) {
					tempMailStatistic.setStandard(key_value+"");
					tempMailStatistic.setSendTotal(map.get(key_value).getSendTotal());
					tempMailStatistic.setSuccessTotal(map.get(key_value).getSuccessTotal());
					tempMailStatistic.setFailTotal(map.get(key_value).getFailTotal());
					tempMailStatistic.setOpenTotal(map.get(key_value).getOpenTotal());
				}else{
					tempMailStatistic.setSendTotal(0);
					tempMailStatistic.setSuccessTotal(0);
					tempMailStatistic.setFailTotal(0);
					tempMailStatistic.setOpenTotal(0);
				}
				statisticDailyList.add(tempMailStatistic);
			}
			
			result = statisticDailyList;
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
		
	}
	
	/**
	 * <p>자동메일 일자별 챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public String statisticDaily(Map<String, Object> searchMap) {
		String retv = "";
		try{
			List<MailStatistic> mailStatisticList = autoMailDAO.statisticDaily(searchMap);
			String year = (String)searchMap.get("year");
			String month = (String)searchMap.get("month");
			String day = (String)searchMap.get("day");
			
			String startDate = year+month;
			String endDate = year+month;
			
			if(day.equals("all")){
				startDate+="01";
				endDate=DateUtils.lastDayOfMonth(startDate);
			}else{
				startDate += day;
				endDate += day;
			}
	
			int count = DateUtils.daysBetween1(startDate, endDate);
			//System.out.println("count : "+count);
			Chart chart = new Chart();
			
			Title title = new Title();
			title.setText("");
			
			Y_Legend yLegend = new Y_Legend();
			yLegend.setText("TEST");
			yLegend.setStyle("{color: #909090; font-size: 12px;}");
			
			Bar_Glass successBar = new Bar_Glass();
			successBar.setAlpha(0.5);
			successBar.setColour("#3334AD");
			successBar.setText("성공");
			successBar.setOn__show(new On__Show().setType("pop").setCascade(1).setDelay(1.5));
			successBar.setTip("#val# 건 성공");
			
			//successBar.setValues(new Object[] {1000,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
			
			Bar_Glass failBar = new Bar_Glass();
			failBar.setAlpha(0.5);
			failBar.setText("실패");
			failBar.setColour("#FF6600");
			failBar.setOn__show(new On__Show().setType("drop").setCascade(0.9).setDelay(0));
			failBar.setTip("#val# 건 실패");
			
			//failBar.setValues(new Object[] { new Values().setTop(15.0) ,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
			
			
			Bar_Glass openBar = new Bar_Glass();
			openBar.setAlpha(0.5);
			openBar.setText("오픈");
			openBar.setColour("#66CC00");
			openBar.setOn__show(new On__Show().setType("grow-up").setCascade(0.8).setDelay(0));
			openBar.setTip("#val# 건 오픈");
			
			//openBar.setValues(new Object[] {9,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
	
			
			Line line = new Line();
			line.setText("성공율(%)");
			line.setColour("#AAAAAA");
			line.setWidth(1);
			
			line.setOn__show(new On__Show().setType("shrink-in").setCascade(1).setDelay(2.5));
			line.setDot__style(new DotStyle().setType("solid-dot").setColour("#a44a80").setDot__size(3).setTip("#val#% 성공 <br>#x_label#"));
			line.setAxis("right");
			
			
			X_Axis xAxis = new X_Axis();
			//xAxis.set___3d(12);
			xAxis.setStroke(1);
			//xAxis.setSteps("1");
			xAxis.setTick__height(10);
			xAxis.setColour("#909090");
			xAxis.setGrid__colour("#C8D0D2");
			//xAxis.setList_labels( new Labels().setRotate(270).setLabels(new Object[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"})  );
			//xAxis.setList_labels( new Object[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"});
			
			
			
			Y_Axis yAxis = new Y_Axis();
			//yAxis.setRotate("vertical");
			yAxis.setStroke(2);
			//yAxis.setSteps(100);
			yAxis.setTick_length(10);
			yAxis.setColour("#909090");
			yAxis.setGrid__colour("#C8D0D2");
			yAxis.setOffset(false);
			//yAxis.setMax("1000");
			
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
	
			
			
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>(); 
			for(MailStatistic mailStatistic : mailStatisticList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
	
			ArrayList lineValues = new ArrayList();
			ArrayList successValues = new ArrayList();
			ArrayList openValues = new ArrayList();
			ArrayList failValues = new ArrayList();
			
			Labels xAisLabel = new Labels().setRotate(270);
			ArrayList xAxisLabels = new ArrayList();
					
			
			for(int i = 0; i <= count; i ++) {
				
				int key_value = new Integer(DateUtils.addDays(startDate, i));
				xAxisLabels.add(DateUtils.reFormat(key_value+"", "yyyymmdd", "yyyy-mm-dd")); 
				if(map.containsKey(key_value)) {
					
					// 실패 담기
					failValues.add(map.get(key_value).getFailTotal());
						
					lineValues.add( map.get(key_value).getSuccessRatio() );
					successValues.add( map.get(key_value).getSuccessTotal() );
					openValues.add( map.get(key_value).getOpenTotal() );
					
				} else {
					lineValues.add(0);
					successValues.add(0);
					failValues.add(0);
					openValues.add(0);
				}
				
			}
			
			
			successBar.setValues(successValues);
			openBar.setValues(openValues);
			failBar.setValues(failValues);
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
			//yAxis.setAutoMax(33600.0);
			
			
			
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
			
			chart.setToolTip(toolTip);
			
			retv = chart.createChart();
		
		}catch(Exception e){
			logger.error(e);
		}

		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 일자별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public String statisticDailyPie(Map<String, Object> searchMap) {
		
		String retv = "";
		try
		{
			List<MailStatistic> mailStatisticList = autoMailDAO.statisticDaily(searchMap);
			String year = (String)searchMap.get("year");
			String month = (String)searchMap.get("month");
			String day = (String)searchMap.get("day");
			
			String startDate = year+month;
			String endDate = year+month;
			
			if(day.equals("all")){
				startDate+="01";
				endDate=DateUtils.lastDayOfMonth(startDate);
			}else{
				startDate += day;
				endDate += day;
			}
			int count = DateUtils.daysBetween1(startDate, endDate);
			Chart chart = new Chart();
			
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
			
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>();
			for(MailStatistic mailStatistic : mailStatisticList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
	
			
			ArrayList pieValues = new ArrayList();
			String label;
			double value;
			for(int i = 0; i <= count; i ++) {
				int key_value = new Integer(DateUtils.addDays(startDate, i));	
				label = DateUtils.reFormat(key_value+"", "yyyymmdd", "yyyy년mm월dd일");
				
				if(map.containsKey(key_value)) {
					value = map.get(key_value).getSendTotal();
					
					label += "전송"+StringUtil.toFormatStr("###,###,###",value)+"(성공"+StringUtil.toFormatStr("##.00", map.get(key_value).getSuccessRatio().doubleValue())+"%)";
					
					
				} else {
	
					label += "전송 건수 없음";
					value = 1;
					
				}
				
				pieValues.add(new Values().setValue(value).setLabel(label));
			}
			
			pie.setValues(pieValues);
			pie.setTip("#label#");
			
			chart.setBg_colour("#ffffff");
			chart.setPie(pie);
			
			retv = chart.createChart();
			return retv ;
			
		}catch(Exception e){
			logger.error(e);
		}

		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 월별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticMonthlyList(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			List<MailStatistic> statisticMonthlyList = autoMailDAO.statisticMonthly(searchMap);
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>(); 
			for(MailStatistic mailStatistic : statisticMonthlyList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
			statisticMonthlyList.clear();
			String year = (String)searchMap.get("year");
			
			String startDate = year+"0101";
			String endDate = year+"1231";
			
			int count = DateUtils.monthBetween(startDate, endDate);
			
			for(int i = 0; i <= count; i ++) {
				int key_value = new Integer(DateUtils.reFormat(DateUtils.addMonths(startDate, i),"yyyymmdd","yyyymm"));	
				MailStatistic tempMailStatistic = new MailStatistic();
				tempMailStatistic.setStandard(key_value+"");
				tempMailStatistic.setViewStandard(DateUtils.reFormat(key_value+"","yyyymm","yyyy-mm"));
				if(map.containsKey(key_value)) {
					tempMailStatistic.setSendTotal(map.get(key_value).getSendTotal());
					tempMailStatistic.setSuccessTotal(map.get(key_value).getSuccessTotal());
					tempMailStatistic.setFailTotal(map.get(key_value).getFailTotal());
					tempMailStatistic.setOpenTotal(map.get(key_value).getOpenTotal());
				}else{
					tempMailStatistic.setSendTotal(0);
					tempMailStatistic.setSuccessTotal(0);
					tempMailStatistic.setFailTotal(0);
					tempMailStatistic.setOpenTotal(0);
				}
				statisticMonthlyList.add(tempMailStatistic);
			}
			result = statisticMonthlyList;
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
		
	}
	
	/**
	 * <p>자동메일 월별 챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public String statisticMonthly(Map<String, Object> searchMap) {
		
		String retv = "";
		
		try{
			List<MailStatistic> mailStatisticList = autoMailDAO.statisticMonthly(searchMap);
			String year = (String)searchMap.get("year");
			String month = (String)searchMap.get("month");
			
			String startDate = year+month+"01";
			String endDate = year+month+"01";
			int count = DateUtils.monthBetween(startDate, endDate);
			Chart chart = new Chart();
			
			Title title = new Title();
			title.setText("");
			
			Y_Legend yLegend = new Y_Legend();
			yLegend.setText("TEST");
			yLegend.setStyle("{color: #909090; font-size: 12px;}");
			
			Bar_Glass successBar = new Bar_Glass();
			successBar.setAlpha(0.5);
			successBar.setColour("#3334AD");
			successBar.setText("성공");
			successBar.setOn__show(new On__Show().setType("pop").setCascade(1).setDelay(1.5));
			successBar.setTip("#val# 건 성공");
			
			//successBar.setValues(new Object[] {1000,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
			
			Bar_Glass failBar = new Bar_Glass();
			failBar.setAlpha(0.5);
			failBar.setText("실패");
			failBar.setColour("#FF6600");
			failBar.setOn__show(new On__Show().setType("drop").setCascade(0.9).setDelay(0));
			failBar.setTip("#val# 건 실패");
			
			//failBar.setValues(new Object[] { new Values().setTop(15.0) ,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
			
			
			Bar_Glass openBar = new Bar_Glass();
			openBar.setAlpha(0.5);
			openBar.setText("오픈");
			openBar.setColour("#66CC00");
			openBar.setOn__show(new On__Show().setType("grow-up").setCascade(0.8).setDelay(0));
			openBar.setTip("#val# 건 오픈");
			
			//openBar.setValues(new Object[] {9,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
	
			
			Line line = new Line();
			line.setText("성공율(%)");
			line.setColour("#AAAAAA");
			line.setWidth(1);
			
			line.setOn__show(new On__Show().setType("shrink-in").setCascade(1).setDelay(2.5));
			line.setDot__style(new DotStyle().setType("solid-dot").setColour("#a44a80").setDot__size(3).setTip("#val#% 성공 <br>#x_label#"));
			line.setAxis("right");
			
			
			X_Axis xAxis = new X_Axis();
			//xAxis.set___3d(12);
			xAxis.setStroke(1);
			//xAxis.setSteps("1");
			xAxis.setTick__height(10);
			xAxis.setColour("#909090");
			xAxis.setGrid__colour("#C8D0D2");
			//xAxis.setList_labels( new Labels().setRotate(270).setLabels(new Object[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"})  );
			//xAxis.setList_labels( new Object[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"});
			
			
			
			Y_Axis yAxis = new Y_Axis();
			//yAxis.setRotate("vertical");
			yAxis.setStroke(2);
			//yAxis.setSteps(100);
			yAxis.setTick_length(10);
			yAxis.setColour("#909090");
			yAxis.setGrid__colour("#C8D0D2");
			yAxis.setOffset(false);
			//yAxis.setMax("1000");
			
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
	
			
			
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>(); 
			for(MailStatistic mailStatistic : mailStatisticList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
	
			ArrayList lineValues = new ArrayList();
			ArrayList successValues = new ArrayList();
			ArrayList openValues = new ArrayList();
			ArrayList failValues = new ArrayList();
			
			Labels xAisLabel = new Labels().setRotate(270);
			ArrayList xAxisLabels = new ArrayList();
					
			
			for(int i = 0; i <= count; i ++) {
				
				int key_value = new Integer(DateUtils.reFormat(DateUtils.addMonths(startDate, i),"yyyymmdd","yyyymm"));	
				xAxisLabels.add(DateUtils.reFormat(key_value+"", "yyyymm", "yyyy-mm")); 
				if(map.containsKey(key_value)) {
					
					// 실패 담기
					failValues.add(map.get(key_value).getFailTotal());
						
					lineValues.add( map.get(key_value).getSuccessRatio() );
					successValues.add( map.get(key_value).getSuccessTotal() );
					openValues.add( map.get(key_value).getOpenTotal() );
					
				} else {
					lineValues.add(0);
					successValues.add(0);
					failValues.add(0);
					openValues.add(0);
				}
				
			}
			
			
			successBar.setValues(successValues);
			openBar.setValues(openValues);
			failBar.setValues(failValues);
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
			//yAxis.setAutoMax(33600.0);
			
			
			
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
			
			chart.setToolTip(toolTip);
			
			retv = chart.createChart();
		
		}catch(Exception e){
			logger.error(e);
		}

		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 월별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public String statisticMonthlyPie(Map<String, Object> searchMap) {
		
		String retv = "";
		
		try
		{
			List<MailStatistic> mailStatisticList = autoMailDAO.statisticMonthly(searchMap);
			String year = (String)searchMap.get("year");
			String month = (String)searchMap.get("month");
			
			String startDate = year+month+"01";
			String endDate = year+month+"01";
			int count = DateUtils.monthBetween(startDate, endDate);
			Chart chart = new Chart();
			
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
			
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>();
			for(MailStatistic mailStatistic : mailStatisticList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
	
			
			ArrayList pieValues = new ArrayList();
			String label;
			double value;
			for(int i = 0; i <= count+1; i ++) {
				int key_value = new Integer(DateUtils.reFormat(DateUtils.addMonths(startDate, (i)),"yyyymmdd","yyyymm"));	
				label = DateUtils.reFormat(key_value+"", "yyyymm", "yyyy년mm월");
				
				if(map.containsKey(key_value)) {
					value = map.get(key_value).getSendTotal();
					
					label += "전송"+StringUtil.toFormatStr("###,###,###",value)+"(성공"+StringUtil.toFormatStr("##.00", map.get(key_value).getSuccessRatio().doubleValue())+"%)";
					
					
				} else {
	
					label += "전송 건수 없음";
					value = 1;
					
				}
				
				pieValues.add(new Values().setValue(value).setLabel(label));
			}
			
			pie.setValues(pieValues);
			pie.setTip("#label#");
			
			chart.setBg_colour("#ffffff");
			chart.setPie(pie);
			
			retv = chart.createChart();
			return retv ;
			
		}catch(Exception e){
			logger.error(e);
		}

		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 도메인별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDomainList(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			result = autoMailDAO.statisticDomain(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
		
	}
	
	/**
	 * <p>자동메일 도메인별 챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public String statisticDomain(Map<String, Object> searchMap) {
		
		String retv = "";
		
		try{
			List<MailStatistic> mailStatisticList = autoMailDAO.statisticDomain(searchMap);
			Chart chart = new Chart();
			
			Title title = new Title();
			title.setText("");
			
			Y_Legend yLegend = new Y_Legend();
			yLegend.setText("TEST");
			yLegend.setStyle("{color: #909090; font-size: 12px;}");
			
			Bar_Glass successBar = new Bar_Glass();
			successBar.setAlpha(0.5);
			successBar.setColour("#3334AD");
			successBar.setText("성공");
			successBar.setOn__show(new On__Show().setType("pop").setCascade(1).setDelay(1.5));
			successBar.setTip("#val# 건 성공");
			
			//successBar.setValues(new Object[] {1000,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
			
			Bar_Glass failBar = new Bar_Glass();
			failBar.setAlpha(0.5);
			failBar.setText("실패");
			failBar.setColour("#FF6600");
			failBar.setOn__show(new On__Show().setType("drop").setCascade(0.9).setDelay(0));
			failBar.setTip("#val# 건 실패");
			
			//failBar.setValues(new Object[] { new Values().setTop(15.0) ,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
			
			
			Bar_Glass openBar = new Bar_Glass();
			openBar.setAlpha(0.5);
			openBar.setText("오픈");
			openBar.setColour("#66CC00");
			openBar.setOn__show(new On__Show().setType("grow-up").setCascade(0.8).setDelay(0));
			openBar.setTip("#val# 건 오픈");
			
			//openBar.setValues(new Object[] {9,6,7,9,5,7,6,9,7,9,6,7,9,5,7,6,9,7,20,20,21,22,23});
	
			
			Line line = new Line();
			line.setText("성공율(%)");
			line.setColour("#AAAAAA");
			line.setWidth(1);
			
			line.setOn__show(new On__Show().setType("shrink-in").setCascade(1).setDelay(2.5));
			line.setDot__style(new DotStyle().setType("solid-dot").setColour("#a44a80").setDot__size(3).setTip("#val#% 성공 <br>#x_label#"));
			line.setAxis("right");
			
			
			X_Axis xAxis = new X_Axis();
			//xAxis.set___3d(12);
			xAxis.setStroke(1);
			//xAxis.setSteps("1");
			xAxis.setTick__height(10);
			xAxis.setColour("#909090");
			xAxis.setGrid__colour("#C8D0D2");
			//xAxis.setList_labels( new Labels().setRotate(270).setLabels(new Object[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"})  );
			//xAxis.setList_labels( new Object[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"});
			
			
			
			Y_Axis yAxis = new Y_Axis();
			//yAxis.setRotate("vertical");
			yAxis.setStroke(2);
			//yAxis.setSteps(100);
			yAxis.setTick_length(10);
			yAxis.setColour("#909090");
			yAxis.setGrid__colour("#C8D0D2");
			yAxis.setOffset(false);
			//yAxis.setMax("1000");
			
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
			
			Labels xAisLabel = new Labels().setRotate(270);
			ArrayList xAxisLabels = new ArrayList();
					
			for(MailStatistic mailStatistic : mailStatisticList ){
				xAxisLabels.add( mailStatistic.getStandard());
				failValues.add( mailStatistic.getFailTotal());
				lineValues.add( mailStatistic.getSuccessRatio() );
				successValues.add( mailStatistic.getSuccessTotal() );
				openValues.add( mailStatistic.getOpenTotal() );
			}
			
			successBar.setValues(successValues);
			openBar.setValues(openValues);
			failBar.setValues(failValues);
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
			//yAxis.setAutoMax(33600.0);
			
			
			
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
			
			chart.setToolTip(toolTip);
			
			retv = chart.createChart();
		
		}catch(Exception e){
			logger.error(e);
		}
		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 도메인별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public String statisticDomainPie(Map<String, Object> searchMap) {
		
		String retv = "";
		try
		{
			List<MailStatistic> mailStatisticList = autoMailDAO.statisticDomain(searchMap);
			Chart chart = new Chart();
			
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
			
			ArrayList pieValues = new ArrayList();
			String label;
			double value;
			for(MailStatistic mailStatistic : mailStatisticList ){
				label = mailStatistic.getStandard();
				value = mailStatistic.getSendTotal()+1 ;
				label += "전송"+StringUtil.toFormatStr("###,###,###",value-1)+"(성공"+StringUtil.toFormatStr("##.00", mailStatistic.getSuccessRatio().doubleValue())+"%)";
				
				pieValues.add(new Values().setValue(value).setLabel(label));
			}
			if(mailStatisticList.size() == 0)
			{
				label = "데이터 없음";
				value = 1;
				pieValues.add(new Values().setValue(value).setLabel(label));
			}

			pie.setValues(pieValues);
			pie.setTip("#label#");
			
			chart.setBg_colour("#ffffff");
			chart.setPie(pie);
			
			retv = chart.createChart();
			return retv ;
			
		}catch(Exception e){
			logger.error(e);
		}

		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 실패원인별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<FailCauseStatistic> statisticFailCauseList(Map<String, Object> searchMap){
		List<FailCauseStatistic> result = null;
		try{
			result = autoMailDAO.statisticFailCause(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
		
	}
	
	/**
	 * <p>자동메일 실패원인별 챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public String statisticFailCause(Map<String, Object> searchMap) {
		
		String retv = "";
		
		try{
			List<FailCauseStatistic> failCauselStatisticList = autoMailDAO.statisticFailCause(searchMap);
			Chart chart = new Chart();
	
			Title title = new Title();
			title.setText("");
			
			Y_Legend yLegend = new Y_Legend();
			yLegend.setText("TEST");
			yLegend.setStyle("{color: #909090; font-size: 12px;}");
			
			Bar_Glass failBar = new Bar_Glass();
			failBar.setAlpha(0.5);
			failBar.setText("실패");
			failBar.setColour("#FF6600");
			failBar.setOn__show(new On__Show().setType("drop").setCascade(0.9).setDelay(0));
			failBar.setTip("#val# 건 실패");
			
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
			for(FailCauseStatistic failCauselStatistic : failCauselStatisticList ){
				xAxisLabels.add( failCauselStatistic.getFailcauseCode());
				failValues.add( failCauselStatistic.getFailCount());
				
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
			retv = chart.createChart();
		
		}catch(Exception e){
			logger.error(e);
		}

		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 도메인별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public String statisticFailCausePie(Map<String, Object> searchMap) {
		
		String retv = "";
		try
		{
		
			List<FailCauseStatistic> failCauselStatisticList = autoMailDAO.statisticFailCause(searchMap);
			
			Chart chart = new Chart();
			
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
			
			ArrayList pieValues = new ArrayList();
			String label;
			double value;
			for(FailCauseStatistic failCauselStatistic : failCauselStatisticList ){
				label = failCauselStatistic.getFailCauseDes()+" : 실패 "+failCauselStatistic.getFailCount()+"건";
				value = failCauselStatistic.getFailCount().intValue() +1;
				pieValues.add(new Values().setValue(value).setLabel(label));
				
			}

			pie.setValues(pieValues);
			pie.setTip("#label#");
			
			chart.setBg_colour("#ffffff");
			chart.setPie(pie);
			
			retv = chart.createChart();
			return retv ;
			
		}catch(Exception e){
			logger.error(e);
		}

		return retv;
		
		
	}
	
	/**
	 * <p>자동메일 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> porsonPreview(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			result = autoMailDAO.porsonPreview(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>자동메일 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap){
		int result = 0;
		try{
			result = autoMailDAO.totalPorsonPreview(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}	
	
	/**
	 * <p>자동메일 월간 통계 ID 별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	
	public List<MailStatistic> autoMailReportMonth(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		
		try {
			result = autoMailDAO.autoMailReportMonth(searchMap);
		} catch (Exception e) {
			logger.error(e);
		}
		
		return result;
	}
	
	/**
	 * <p>자동메일 월간 통계 전체 발송 현황
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */	
	
	public MailStatistic autoMailReportMonthAll(String sendTime){
		MailStatistic result = null;
		
		try {
			result = autoMailDAO.autoMailReportMonthAll(sendTime);
		} catch (Exception e) {
			logger.error(e);
		}
		
		return result;
	}
	
	/**
	 * <p>자동메일 월간통계 카운트 
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoMailReportMonth(String sendTime){
		int result = 0;
		try{
			result = autoMailDAO.getTotalCountAutoMailReportMonth(sendTime);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
}
