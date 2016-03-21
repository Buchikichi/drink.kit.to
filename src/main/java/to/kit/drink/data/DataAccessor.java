package to.kit.drink.data;

import java.util.List;
import java.util.Map;

/**
 * データアクセス.
 * @author Hidetaka Sasai
 *
 */
public interface DataAccessor {
	/**
	 * レコード作成.
	 * @param rec レコード
	 * @throws Exception 例外
	 */
	void save(TableRecord rec) throws Exception;

	/**
	 * レコード取得.
	 * @param rec テーブル情報
	 * @return レコード
	 * @throws Exception 例外
	 */
	Map<String, Object> read(TableRecord rec) throws Exception;

	/**
	 * 一覧取得.
	 * @param kind 種類
	 * @return 一覧
	 * @throws Exception 例外
	 */
	List<Map<String, Object>> list(String kind) throws Exception;
}
