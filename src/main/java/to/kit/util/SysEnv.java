package to.kit.util;

/**
 * System Environment.
 * @author Hidetaka Sasai
 */
public class SysEnv {
	/**
	 * ローカル開発環境かどうか調べる.
	 * @return ローカル開発環境の場合は true、そうでない場合は false
	 */
	public static boolean isLocal() {
		return System.getProperty("java.vendor", "").toLowerCase().contains("oracle");
	}
}
