package to.kit.drink.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * テーブルレコードを管理.
 * @author Hidetaka Sasai
 */
public final class TableRecord extends HashMap<String, Object> {
	private final String table;
	private String key;
	private final List<String> sort = new ArrayList<>();

	/**
	 * インスタンス生成.
	 * @param tableName テーブル名
	 */
	public TableRecord(String tableName) {
		this.table = tableName;
	}

	/**
	 * ソート順を追加.
	 * @param fieldName フィールド名
	 * @return self
	 */
	public TableRecord addSort(String fieldName) {
		this.sort.add(fieldName);
		return this;
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

	/**
	 * ソート順を取得.
	 * @return ソート順
	 */
	public List<String> getSort() {
		return this.sort;
	}
}
