package web.api.automail;

public class ipCheck_Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String remoteIP ="192.168.0.17";
		String checkIP = "192.168.*.*|192.168.0.17|192.168.*.*";
		boolean result = false;
		String[] remoteIPs = remoteIP.split("\\.");
		String[] checkIPs = checkIP.split("|");
		
		for (int i = 0; i < checkIPs.length; i++) {
			checkIPs[i] = checkIPs[i].replace(".*", "");
			System.out.println("ㄱㄱㄱ : " + checkIPs[i]);
			String[] chkIPs = checkIPs[i].split("\\.");
			System.out.println("ㄴㄴㄴ : " + chkIPs.length);
			for (int j = 0; j < chkIPs.length; j++) {
				if (chkIPs[j].equals(remoteIPs[j])) {
					result = true;
				} else {
				System.out.println("i="+i+" j="+j+" chkIPs[j]="+chkIPs[j]+" remoteIPs[j]="+remoteIPs[j]);
					result = false;
					break;
				}
			}
			if(result){
				break;
			}
		}
		System.out.println(result);

	}

}
