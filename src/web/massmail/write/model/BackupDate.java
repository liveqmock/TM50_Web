package web.massmail.write.model;

import java.io.Serializable;


/**
 * <p>백업정보 (tm_massmail_backupdate)
 * @author yhlim
 *
 */
@SuppressWarnings("serial")
public class BackupDate implements Serializable {
	
	private String backupYearMonth;
	private String backupYear;
	private String backupMonth;
	public String getBackupYearMonth() {
		return backupYearMonth;
	}
	public void setBackupYearMonth(String backupYearMonth) {
		this.backupYearMonth = backupYearMonth;
	}
	public String getBackupYear() {
		return backupYear;
	}
	public void setBackupYear(String backupYear) {
		this.backupYear = backupYear;
	}
	public String getBackupMonth() {
		return backupMonth;
	}
	public void setBackupMonth(String backupMonth) {
		this.backupMonth = backupMonth;
	}
	
}
