package to.kit.drink.data;

import java.util.HashMap;

/**
 * テーブルレコードを管理.
 * @author Hidetaka Sasai
 */
public final class TableRecord extends HashMap<String, Object> {
	private final String table;
	private String key;

	/**
	 * インスタンス生成.
	 * @param tableName テーブル名
	 */
	public TableRecord(String tableName) {
		this.table = tableName;
	}

	/**
	 * テーブル名を取得.
	 * @return テーブル名
	 */
	public String getTable() {
		return this.table;
	}
	/**
	 * キーを取得.
	 * @return キー
	 */
	public String getKey() {
		return this.key;
	}
	/**
	 * キーを設定.
	 * @param val キー
	 * @return self
	 */
	public TableRecord setKey(String val) {
		this.key = val;
		return this;
	}
}
