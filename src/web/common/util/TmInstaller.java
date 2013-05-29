package web.common.util;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.HashMap;
import com.sun.management.OperatingSystemMXBean;

/**
 * <p>엔진서비스 등록 및 수정 
 * @author ykkim
 *
 */
public class TmInstaller {
	
		
	private static Map<String,String> enginesMap = null;
	
	
	private static void init(){
		enginesMap =  new HashMap<String,String>();
			
		
		
		
		//대량메일 
		enginesMap.put("TM_MassMailPrepareMain", "tm.engine.massmail.prepare.main.MassMailPrepareMain");
		enginesMap.put("TM_MassMailSendMain", "tm.engine.massmail.send.main.MassMailSendMain");
		enginesMap.put("TM_MassMailStatisticMain", "tm.engine.massmail.statistic.main.MassMailStatisticMain");
		enginesMap.put("TM_MassMailBackupMain", "tm.engine.massmail.backup.main.MassMailBackupMain");
		enginesMap.put("TM_ObserverMain", "tm.engine.observer.main.ObserverMain");
		//자동메일
		enginesMap.put("TM_AutoMailSendMain", "tm.engine.automail.send.main.AutoMailSendMain");
		enginesMap.put("TM_AutoMailStatisticMain", "tm.engine.automail.statistic.main.AutoMailStatisticMain");
	
		//자동SMS
		enginesMap.put("TM_AutoSMSSendMain", "tm.engine.autosms.send.main.AutoSMSSendMain");
		enginesMap.put("TM_AutoSMSStatisticMain", "tm.engine.autosms.statistic.main.AutoSMSStatisticMain");
		
		//대량SMS
		enginesMap.put("TM_MassSMSPrepareMain", "-Xms64m -Xmx1024m tm.engine.masssms.prepare.main.MassSMSPrepareMain");
		enginesMap.put("TM_MassSMSSendMain", "-Xms64m -Xmx1024m tm.engine.masssms.send.main.MassSMSSendMain");
		enginesMap.put("TM_MassSMSStatisticMain", "tm.engine.masssms.statistic.main.MassSMSStatisticMain");
		enginesMap.put("TM_MassSMSBackupMain", "tm.engine.masssms.backup.main.MassSMSBackupMain");
		
				
	}

	
	
	
	/**
	 * <p>현재 OS타입을 가져온다.
	 * @return
	 */
	public static String getOSType(){
		String osName = getOSName();
		String osType="";	
		if(osName.toLowerCase().indexOf("windows")!=-1){
			osType="win";			
		}else{
			osType="other";			
		}
		return osType;
	}
	
	
	/**
	 * <p>현재 시스템의 OS명을 가져온다. 
	 * @return
	 */
	public static String getOSName(){		
		OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		return osbean.getName();
	}
	
	
	
	/**
	 * <p>서비스명에 해당하는 엔진 실행 경로 
	 * @param serviceName
	 * @return
	 */
	private static String getEnginePath(String serviceName){
		init();
		String enginePath = "";		
		for(int i=0;i<enginesMap.size();i++){
			enginePath = 	enginesMap.get(serviceName);
			if(enginePath!=null && !enginePath.equals("")){
				break;
			}			
		}
		return enginePath;
	}
	
		
	
	/**
	 * <p>서비스 시작 
	 * @param serviceName
	 */
	public static void startService(String serviceName){
		String str = "";
		if(getOSType().equals("win")){
			String[] cmd = {"net","start",serviceName};
			str = SystemExec.getCMD(cmd); 
			System.out.println(str);	   	
		}else{
			String[] cmd = {"sh","-c","java -Dfile.encoding=UTF-8 "+getEnginePath(serviceName)};
			if(serviceName.equals("TM_MassMailPrepareMain") || serviceName.equals("TM_MassMailSendMain"))
				cmd[2] = "java -Dfile.encoding=UTF-8 -Xms64m -Xmx1024m "+getEnginePath(serviceName); //추가옵션 	-Xms64m -Xmx1024m
			str = SystemExec.getCMD(cmd); 
			System.out.println(str);	   	
		}
	}

	
	/**
	 * <p>서비스 종료 
	 * @param serviceName
	 */
	public static void stopService(String serviceName){
		String str = "";
		if(getOSType().equals("win")){
			String[] cmd = {"net","stop",serviceName};
			str = SystemExec.getCMD(cmd); 
			System.out.println(str);	   	
		}else{
			String[] cmd = {"sh","-c","/usr/bin/pkill -f "+getEnginePath(serviceName)};
			str = SystemExec.getCMD(cmd); 
			System.out.println(str);	   	
		}
	}
	
	/**
	 * <p>서비스 시작 
	 * @param serviceName
	 */
	public static String startServiceReturn(String serviceName){
		String str = "";
		if(getOSType().equals("win")){
			String[] cmd = {"net","start",serviceName};
			str = SystemExec.getCMD(cmd); 
			System.out.println(str);	   	
		}else{
			String[] cmd = {"sh","-c","java -Dfile.encoding=UTF-8 "+getEnginePath(serviceName)};
			if(serviceName.equals("TM_MassMailPrepareMain") || serviceName.equals("TM_MassMailSendMain"))
				cmd[2] = "java -Dfile.encoding=UTF-8 -Xms64m -Xmx1024m "+getEnginePath(serviceName); //추가옵션 	-Xms64m -Xmx1024m
				
			str = SystemExec.getCMD(cmd); 
			System.out.println(str);	   	
		}
		return str;
	}

	
	/**
	 * <p>서비스 종료 
	 * @param serviceName
	 */
	public static String stopServiceReturn(String serviceName){
		String str = "";
		if(getOSType().equals("win")){
			String[] cmd = {"net","stop",serviceName};
			str = SystemExec.getCMD(cmd); 
			System.out.println(str);	   	
		}else{
			String[] cmd = {"sh","-c","/usr/bin/pkill -f "+getEnginePath(serviceName)};
			str = SystemExec.getCMD(cmd); 
			System.out.println(str);	   	
		}
		return str;
	}
	

}
