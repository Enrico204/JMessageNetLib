
package it.enricobassetti.JMessageNetLib;

/**
 *
 * @author enrico
 */
public class SMSSendFailedInfo {
	public String number;
	public String description;
	public Integer code;

	public SMSSendFailedInfo(String number, String description, Integer code) {
		this.number = number;
		this.description = description;
		this.code = code;
	}
	
	
}
