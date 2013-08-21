
package it.enricobassetti.JMessageNetLib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author enrico
 */
public class SMSAPI {
	private String url;
	private String username;
	private String password;
	private Float credit = null;
	private String lastError = null;
	
	private DateFormat df = new SimpleDateFormat("y-M-d'T'H:m:s");

	public SMSAPI(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public SMSAPI(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Float getCredit() {
		return credit;
	}

	public String getLastError() {
		return lastError;
	}
	
	public SMSSendInfo sendSMS(SMS smsobj) throws TextLimitExcedeed,
			UnsupportedEncodingException, IOException, ParseException,
			NoCreditException, UnauthorizedException {
		return this.sendSMS(
				smsobj.getRecipients(),
				smsobj.getText()
				);
	}
	
	public SMSSendInfo sendSMS(String[] number, String text) throws TextLimitExcedeed,
			UnsupportedEncodingException, IOException, ParseException,
			NoCreditException, UnauthorizedException {
		List<String> numbers = new ArrayList<String>();
		numbers.addAll(Arrays.asList(number));
		return this.sendSMS(numbers, text);
	}
	
	public SMSSendInfo sendSMS(List<String> number, String text) throws TextLimitExcedeed,
			UnsupportedEncodingException, IOException, ParseException,
			NoCreditException, UnauthorizedException {
		
		if(text.length() > 1530) {
			throw new TextLimitExcedeed("SMS Text limit exceeded (" + text.length() + " > 1530)");
		}
		
		HttpClient cl = new DefaultHttpClient();
		HttpPost req = new HttpPost(url);
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair("userid", this.username));
		reqParams.add(new BasicNameValuePair("password", this.password));
		reqParams.add(new BasicNameValuePair("method", "send_sms"));
		for(String n : number) {
			reqParams.add(new BasicNameValuePair("number", n));
		}
		reqParams.add(new BasicNameValuePair("text", text));
		
		req.setEntity(new UrlEncodedFormEntity(reqParams));
		
		HttpResponse resp = cl.execute(req);
		StatusLine status = resp.getStatusLine();
		switch(status.getStatusCode()) {
			case 200:
				JSONParser par = new JSONParser();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
                resp.getEntity().writeTo(out);
                out.close();
				
				SMSSendInfo inf = new SMSSendInfo();
				
				JSONObject obj = (JSONObject)par.parse(out.toString());
				try {
					inf.timestamp = df.parse((String)((JSONObject)obj.get("timestamp")).get("value"));
				} catch(java.text.ParseException e) {
					inf.timestamp = new Date();
				}
				
				JSONObject subObj = (JSONObject)obj.get("credit");
				inf.credit = Float.parseFloat(subObj.get("value").toString());
				inf.creditCurrency = SMSSendInfo.CURRENCY.valueOf(subObj.get("currency").toString());
				this.credit = inf.credit;
				
				subObj = (JSONObject)obj.get("status");
				inf.statusMessage = subObj.get("description").toString();
				inf.statusCode = Integer.parseInt(subObj.get("code").toString());
				
				subObj = (JSONObject)obj.get("warning");
				if(subObj != null) {
					inf.statusMessage = subObj.get("description").toString();
					inf.statusCode = Integer.parseInt(subObj.get("code").toString());
				}
				
				JSONArray arrObj = (JSONArray)obj.get("sent");
				inf.sent = new HashMap<Integer, String>();
				for(Object o : arrObj) {
					inf.sent.put(
							Integer.parseInt(((JSONObject)o).get("message_id").toString()),
							((JSONObject)o).get("value").toString()
							);
				}
				
				arrObj = (JSONArray)obj.get("failed");
				if(arrObj != null) {
					inf.failed = new ArrayList<SMSSendFailedInfo>();
					for(Object o : arrObj) {
						inf.failed.add(new SMSSendFailedInfo(
								((JSONObject)o).get("number").toString(),
								((JSONObject)o).get("description").toString(),
								Integer.parseInt(((JSONObject)o).get("code").toString())
								));
					}
				}
				
				return inf;
			case 401:
				this.lastError = status.getReasonPhrase();
				throw new UnauthorizedException(this.lastError);
			case 402:
				this.lastError = status.getReasonPhrase();
				throw new NoCreditException(this.lastError);
			case 500:
			default:
				this.lastError = status.getReasonPhrase();
				throw new IOException(this.lastError);
		}
	}
	
	
}
