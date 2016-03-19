package to.kit.drink.dto;

public final class Picture {
	private String id;
	private String url;

	/**
	 * IDを取得.
	 * @return ID
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * IDを設定.
	 * @param id ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * URLを取得.
	 * @return URL
	 */
	public String getUrl() {
		return this.url;
	}
	/**
	 * URLを設定.
	 * @param url URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
