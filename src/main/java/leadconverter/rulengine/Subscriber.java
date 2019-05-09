package leadconverter.rulengine;

import java.io.Serializable;

public class Subscriber implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Input fields
	private String subsriberId;
	private boolean mailBounce;
	private boolean mailDelivered;
	private boolean mailOpen;
	private int mailBounceCount;
	private int mailHotLink1ClickCount;
	private int mailHotLink2ClickCount;
	private int mailHotLink3ClickCount;
	private int mailHotLink4ClickCount;
	private int mailHotLink5ClickCount;
	private int mailNormalLink1ClickCount;
	private int mailNormalLink2ClickCount;
	private int mailNormalLink3ClickCount;
	private int mailNormalLink4ClickCount;
	private int mailNormalLink5ClickCount;
	private boolean mailUnsubscribe;
	// internally used variables for process
	private boolean clickedOnHotLinks;
	private boolean clickedOnNormalLinks;
	private boolean clickedTwiceonAnyNormalLink;
	// result variable
	private String filteredBucket;
	
	
	public String getFilteredBucket() {
		return filteredBucket;
	}

	public void setFilteredBucket(String filteredBucket) {
		this.filteredBucket = filteredBucket;
	}

	public boolean isClickedTwiceonAnyNormalLink() {
		return clickedTwiceonAnyNormalLink;
	}

	public void setClickedTwiceonAnyNormalLink(boolean clickedTwiceonAnyNormalLink) {
		this.clickedTwiceonAnyNormalLink = clickedTwiceonAnyNormalLink;
	}

	public boolean isMailOpen() {
		return mailOpen;
	}

	public void setMailOpen(boolean mailOpen) {
		this.mailOpen = mailOpen;
	}

	public boolean isClickedOnHotLinks() {
		return clickedOnHotLinks;
	}

	public void setClickedOnHotLinks(boolean clickedOnHotLinks) {
		this.clickedOnHotLinks = clickedOnHotLinks;
	}

	public boolean isClickedOnNormalLinks() {
		return clickedOnNormalLinks;
	}

	public void setClickedOnNormalLinks(boolean clickedOnNormalLinks) {
		this.clickedOnNormalLinks = clickedOnNormalLinks;
	}

	public String getSubsriberId() {
		return subsriberId;
	}

	public void setSubsriberId(String subsriberId) {
		this.subsriberId = subsriberId;
	}

	public boolean isMailDelivered() {
		return mailDelivered;
	}

	public void setMailDelivered(boolean mailDelivered) {
		this.mailDelivered = mailDelivered;
	}

	public int getMailBounceCount() {
		return mailBounceCount;
	}

	public void setMailBounceCount(int mailBounceCount) {
		this.mailBounceCount = mailBounceCount;
	}

	public boolean isMailUnsubscribe() {
		return mailUnsubscribe;
	}

	public void setMailUnsubscribe(boolean mailUnsubscribe) {
		this.mailUnsubscribe = mailUnsubscribe;
	}

	public boolean isMailBounce() {
		return mailBounce;
	}

	public void setMailBounce(boolean mailBounce) {
		this.mailBounce = mailBounce;
	}

	public int getMailHotLink1ClickCount() {
		return mailHotLink1ClickCount;
	}

	public void setMailHotLink1ClickCount(int mailHotLink1ClickCount) {
		this.mailHotLink1ClickCount = mailHotLink1ClickCount;
	}

	public int getMailHotLink2ClickCount() {
		return mailHotLink2ClickCount;
	}

	public void setMailHotLink2ClickCount(int mailHotLink2ClickCount) {
		this.mailHotLink2ClickCount = mailHotLink2ClickCount;
	}

	public int getMailHotLink3ClickCount() {
		return mailHotLink3ClickCount;
	}

	public void setMailHotLink3ClickCount(int mailHotLink3ClickCount) {
		this.mailHotLink3ClickCount = mailHotLink3ClickCount;
	}

	public int getMailHotLink4ClickCount() {
		return mailHotLink4ClickCount;
	}

	public void setMailHotLink4ClickCount(int mailHotLink4ClickCount) {
		this.mailHotLink4ClickCount = mailHotLink4ClickCount;
	}

	public int getMailHotLink5ClickCount() {
		return mailHotLink5ClickCount;
	}

	public void setMailHotLink5ClickCount(int mailHotLink5ClickCount) {
		this.mailHotLink5ClickCount = mailHotLink5ClickCount;
	}

	public int getMailNormalLink1ClickCount() {
		return mailNormalLink1ClickCount;
	}

	public void setMailNormalLink1ClickCount(int mailNormalLink1ClickCount) {
		this.mailNormalLink1ClickCount = mailNormalLink1ClickCount;
	}

	public int getMailNormalLink2ClickCount() {
		return mailNormalLink2ClickCount;
	}

	public void setMailNormalLink2ClickCount(int mailNormalLink2ClickCount) {
		this.mailNormalLink2ClickCount = mailNormalLink2ClickCount;
	}

	public int getMailNormalLink3ClickCount() {
		return mailNormalLink3ClickCount;
	}

	public void setMailNormalLink3ClickCount(int mailNormalLink3ClickCount) {
		this.mailNormalLink3ClickCount = mailNormalLink3ClickCount;
	}

	public int getMailNormalLink4ClickCount() {
		return mailNormalLink4ClickCount;
	}

	public void setMailNormalLink4ClickCount(int mailNormalLink4ClickCount) {
		this.mailNormalLink4ClickCount = mailNormalLink4ClickCount;
	}

	public int getMailNormalLink5ClickCount() {
		return mailNormalLink5ClickCount;
	}

	public void setMailNormalLink5ClickCount(int mailNormalLink5ClickCount) {
		this.mailNormalLink5ClickCount = mailNormalLink5ClickCount;
	}

	public Subscriber(String subsriberId, boolean mailBounce, boolean mailDelivered, boolean mailOpen,
			int mailBounceCount, int mailHotLink1ClickCount, int mailHotLink2ClickCount, int mailHotLink3ClickCount,
			int mailHotLink4ClickCount, int mailHotLink5ClickCount, int mailNormalLink1ClickCount,
			int mailNormalLink2ClickCount, int mailNormalLink3ClickCount, int mailNormalLink4ClickCount,
			int mailNormalLink5ClickCount, boolean mailUnsubscribe, boolean clickedOnHotLinks,
			boolean clickedOnNormalLinks, boolean clickedTwiceonAnyNormalLink) {
		this.subsriberId = subsriberId;
		this.mailBounce = mailBounce;
		this.mailDelivered = mailDelivered;
		this.mailOpen = mailOpen;
		this.mailBounceCount = mailBounceCount;
		this.mailHotLink1ClickCount = mailHotLink1ClickCount;
		this.mailHotLink2ClickCount = mailHotLink2ClickCount;
		this.mailHotLink3ClickCount = mailHotLink3ClickCount;
		this.mailHotLink4ClickCount = mailHotLink4ClickCount;
		this.mailHotLink5ClickCount = mailHotLink5ClickCount;
		this.mailNormalLink1ClickCount = mailNormalLink1ClickCount;
		this.mailNormalLink2ClickCount = mailNormalLink2ClickCount;
		this.mailNormalLink3ClickCount = mailNormalLink3ClickCount;
		this.mailNormalLink4ClickCount = mailNormalLink4ClickCount;
		this.mailNormalLink5ClickCount = mailNormalLink5ClickCount;
		this.mailUnsubscribe = mailUnsubscribe;
		this.clickedOnHotLinks = clickedOnHotLinks;
		this.clickedOnNormalLinks = clickedOnNormalLinks;
		this.clickedTwiceonAnyNormalLink = clickedTwiceonAnyNormalLink;
	}

	@Override
	public String toString() {
		return "Subscriber [ Result filtering=" + filteredBucket + "]";
	}



	
}

