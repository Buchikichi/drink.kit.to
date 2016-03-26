package to.kit.drink.dto;

/**
 * タグリクエスト.
 * @author Hidetaka Sasai
 *
 */
public class TagsRequest extends Tags {
	private String lang;

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
}
