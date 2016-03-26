package to.kit.drink.dto;

/**
 * アイテムリクエスト.
 * @author Hidetaka Sasai
 *
 */
public class ItemRequest extends Item {
	private String lang;
	private String type;
	private byte[] picture;

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
	 * タイプを取得.
	 * @return タイプ
	 */
	public String getType() {
		return this.type;
	}
	/**
	 * タイプを設定.
	 * @param type タイプ
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 画像を取得.
	 * @return 画像
	 */
	public byte[] getPicture() {
		return this.picture;
	}
	/**
	 * 画像を設定.
	 * @param value 画像
	 */
	public void setPicture(byte[] value) {
		this.picture = value;
	}
}
