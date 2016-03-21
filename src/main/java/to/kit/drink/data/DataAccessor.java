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

	Map<String, String> read(TableRecord rec) throws Exception;

	List<Map<String, String>> list(String kind) throws Exception;
}
