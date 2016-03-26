package to.kit.drink.data;

/**
 * データアクセスファクトリー.
 * @author Hidetaka Sasai
 */
public final class DataAccessorFactory {
	private static DataAccessor instance;

	/**
	 * データアクセスのインスタンスを取得.
	 * @return データアクセス
	 */
	public static DataAccessor getInstance() {
		if (instance != null) {
			return instance;
		}
		if (System.getenv("DATASTORE_SERVICE_ACCOUNT") == null) {
			instance = new AppEngineDatastore();
		} else {
			instance = new ApiServicesDatastore();
		}
		return instance;
	}
}
