package to.kit.drink.dto;

/**
 * Country.
 * @author Hidetaka Sasai
 */
public class Country implements Multilingual {
	private String id;
	private String text;
	private String flag;

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the flag
	 */
	public String getFlag() {
		return this.flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
