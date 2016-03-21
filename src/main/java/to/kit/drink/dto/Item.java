package to.kit.drink.dto;

public class Item {
	private String id;
	private String kindId;
	private String countryCd;
	private String text;
	private String abv;
	private String fileId;
	private String imgsrc;
	private String thumbnail;

	/**
	 * アイテムIDを取得.
	 * @return アイテムID
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * アイテムIDを設定.
	 * @param id アイテムID
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
	 * @return the abv
	 */
	public String getAbv() {
		return this.abv;
	}
	/**
	 * @param abv the abv to set
	 */
	public void setAbv(String abv) {
		this.abv = abv;
	}
	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return this.fileId;
	}
	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
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
	/**
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return this.thumbnail;
	}
	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}
