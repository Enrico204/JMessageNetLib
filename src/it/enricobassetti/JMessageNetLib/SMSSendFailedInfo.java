
package it.enricobassetti.JMessageNetLib;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 *
 * @author enrico
 */
public class SMSSendFailedInfo implements JSONAware {
	public String number;
	public String description;
	public Integer code;

	public SMSSendFailedInfo(String number, String description, Integer code) {
		this.number = number;
		this.description = description;
		this.code = code;
	}

	@Override
	public String toJSONString() {
		JSONObject obj = new JSONObject();
		obj.put("number", this.number);
		obj.put("description", this.description);
		obj.put("code", this.code);
		return obj.toJSONString();
	}
}
