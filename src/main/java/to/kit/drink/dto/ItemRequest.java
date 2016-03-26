package to.kit.drink.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * アイテムリクエスト.
 * @author Hidetaka Sasai
 *
 */
public class ItemRequest extends Item {
	private String lang;
	private List<String> countries = new ArrayList<>();
	private String type;
	private String keyword;
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
	 * 国を取得.
	 * @return 国
	 */
	public List<String> getCountries() {
		return this.countries;
	}
	/**
	 * 国を設定.
	 * @param list 国
	 */
	public void setCountries(List<String> list) {
		this.countries = list;
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
	 * キーワードを取得.
	 * @return キーワード
	 */
	public String getKeyword() {
		return this.keyword;
	}
	/**
	 * キーワードを設定.
	 * @param keyword キーワード
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
