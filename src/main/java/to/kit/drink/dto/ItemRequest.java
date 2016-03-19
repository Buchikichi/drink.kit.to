package to.kit.drink.dto;

public class ItemRequest {
	private String lang;
	private String id;

	/**
	 * 言語コードを取得.
	 * @return 言語コード
	 */
	public String getLang() {
		return this.lang;
	}
	/**
	 * 言語コードを設定.
	 * @param lang 言語コード
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
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
}
