
package it.enricobassetti.JMessageNetLib;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author enrico
 */
public class SMSSendInfo {
	
	public enum CURRENCY {
		EUR
	}
	
	public Date timestamp;
	public Float credit;
	public CURRENCY creditCurrency;
	public String statusMessage;
	public Integer statusCode;
	public String warningMessage;
	public Integer warningCode;
	public Map<Integer, String> sent;
	public List<SMSSendFailedInfo> failed;
	
}
