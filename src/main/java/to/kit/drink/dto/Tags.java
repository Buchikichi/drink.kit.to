package to.kit.drink.dto;

/**
 * タグ.
 * @author Hidetaka Sasai
 */
public class Tags implements Multilingual {
	private String id;
	private String text;
	private String filtertext;

	/**
	 * タグIDを取得.
	 * @return タグID
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * タグIDを設定.
	 * @param id タグID
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * テキストを取得.
	 * @return テキスト
	 */
	public String getText() {
		return this.text;
	}
	/**
	 * テキストを設定.
	 * @param text テキスト
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * フィルターテキストを取得.
	 * @return フィルターテキスト
	 */
	public String getFiltertext() {
		return this.filtertext;
	}
	/**
	 * フィルターテキストを設定.
	 * @param filtertext フィルターテキスト
	 */
	public void setFiltertext(String filtertext) {
		this.filtertext = filtertext;
	}
}
