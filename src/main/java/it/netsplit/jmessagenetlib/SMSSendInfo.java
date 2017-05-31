
package it.netsplit.jmessagenetlib;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 *
 * @author enrico
 */
public class SMSSendInfo implements JSONAware {
	
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
	
	@Override
	public String toJSONString() {
		JSONObject ret = new JSONObject();
		ret.put("timestamp", this.timestamp.getTime());
		ret.put("credit", this.credit);
		ret.put("creditCurrency", this.creditCurrency.name());
		ret.put("statusMessage", this.statusMessage);
		ret.put("statusCode", this.statusCode);
		ret.put("warningMessage", this.warningMessage);
		ret.put("warningCode", this.warningCode);
		ret.put("statusCode", this.statusCode);
		ret.put("sent", this.sent);
		ret.put("failed", this.failed);
		return ret.toString();
	}
	
}
