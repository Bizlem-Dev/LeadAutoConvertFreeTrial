package leadconverter.rulengine;

import java.io.Serializable;

public class Campaign implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int campaignId;
	private String campaignName;
	private String campaignSubject;
	private String type;
	private String customerId;
	private Subscriber subscriber;

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignSubject() {
		return campaignSubject;
	}

	public void setCampaignSubject(String campaignSubject) {
		this.campaignSubject = campaignSubject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public Campaign(int campaignId, String campaignName, String campaignSubject, String type, String customerId,
			Subscriber subscriber) {
		super();
		this.campaignId = campaignId;
		this.campaignName = campaignName;
		this.campaignSubject = campaignSubject;
		this.type = type;
		this.customerId = customerId;
		this.subscriber = subscriber;
	}
	
}
