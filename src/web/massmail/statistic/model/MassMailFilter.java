package web.massmail.statistic.model;

import java.io.Serializable;

/**
 * <p>tm_massmail_filter
 * @author 임영호
 *
 */
@SuppressWarnings("serial")
public class MassMailFilter  implements Serializable{
	
	int filterType0 ;
	int filterType1 ;
	int filterType2 ;
	int filterType3 ;
	int filterType4 ;
	int filterType5 ;
	int filterType6 ;
	int filterType7 ;
	int filterTotal ;
	
	public int getFilterType0() {
		return filterType0;
	}
	public void setFilterType0(int filterType0) {
		this.filterType0 = filterType0;
	}
	public int getFilterType1() {
		return filterType1;
	}
	public void setFilterType1(int filterType1) {
		this.filterType1 = filterType1;
	}
	public int getFilterType2() {
		return filterType2;
	}
	public void setFilterType2(int filterType2) {
		this.filterType2 = filterType2;
	}
	public int getFilterType3() {
		return filterType3;
	}
	public void setFilterType3(int filterType3) {
		this.filterType3 = filterType3;
	}
	public int getFilterType4() {
		return filterType4;
	}
	public void setFilterType4(int filterType4) {
		this.filterType4 = filterType4;
	}
	public int getFilterType5() {
		return filterType5;
	}
	public void setFilterType5(int filterType5) {
		this.filterType5 = filterType5;
	}
	public int getFilterType6() {
		return filterType6;
	}
	public void setFilterType6(int filterType6) {
		this.filterType6 = filterType6;
	}
	public int getFilterType7() {
		return filterType7;
	}
	public void setFilterType7(int filterType7) {
		this.filterType7 = filterType7;
	}
	public int getFilterTotal() {
		return filterTotal;
	}
	public void setFilterTotal(int filterTotal) {
		this.filterTotal = filterTotal;
	}
	
}
