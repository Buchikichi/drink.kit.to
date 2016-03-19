package to.kit.drink.dto;

public class Item {
	private String id;
	private String kindId;
	private String countryCd;
	private String text;
	private String imgsrc;

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
	 * @return the kindId
	 */
	public String getKindId() {
		return this.kindId;
	}
	/**
	 * @param kindId the kindId to set
	 */
	public void setKindId(String kindId) {
		this.kindId = kindId;
	}
	/**
	 * @return the countryCd
	 */
	public String getCountryCd() {
		return this.countryCd;
	}
	/**
	 * @param countryCd the countryCd to set
	 */
	public void setCountryCd(String countryCd) {
		this.countryCd = countryCd;
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
	 * @return the imgsrc
	 */
	public String getImgsrc() {
		return this.imgsrc;
	}
	/**
	 * @param imgsrc the imgsrc to set
	 */
	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}
}
