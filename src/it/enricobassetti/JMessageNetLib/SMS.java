
package it.enricobassetti.JMessageNetLib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ebassetti
 */
public class SMS {
	
	private List<String> recipients = new ArrayList<String>();
	private String text = "";

	public SMS() {
	}

	public SMS(List<String> recipient, String text) throws TextLimitExcedeed {
		if(text.length() > 1530) {
			throw new TextLimitExcedeed("SMS Text limit exceeded (" + text.length() + " > 1530)");
		}
		
		this.recipients = recipient;
		this.text = text;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public String getText() {
		return text;
	}
	
	public void addRecipient(String recipient) {
		this.recipients.add(recipient);
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	public void setRecipients(String[] recipient) {
		this.recipients.clear();
		this.recipients.addAll(Arrays.asList(recipient));
	}

	public void setText(String text) throws TextLimitExcedeed {
		if(text.length() > 1530) {
			throw new TextLimitExcedeed("SMS Text limit exceeded (" + text.length() + " > 1530)");
		}
		this.text = text;
	}
}
