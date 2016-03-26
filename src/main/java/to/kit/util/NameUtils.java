package to.kit.util;

import java.lang.Character.UnicodeBlock;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 名前に関するユーティリティークラス.
 * @author Hidetaka Sasai
 */
public final class NameUtils {
	private static final String HALF_KANA = "ｧ ｱ ｨ ｲ ｩ ｳ ｪ ｴ ｫ ｵ ｶ ｶﾞｷ ｷﾞｸ ｸﾞｹ ｹﾞｺ ｺﾞｻ ｻﾞｼ ｼﾞｽ ｽﾞｾ ｾﾞｿ ｿﾞﾀ ﾀﾞﾁ ﾁﾞｯ ﾂ ﾂﾞﾃ ﾃﾞﾄ ﾄﾞﾅ ﾆ ﾇ ﾈ ﾉ ﾊ ﾊﾞﾊﾟﾋ ﾋﾞﾋﾟﾌ ﾌﾞﾌﾟﾍ ﾍﾞﾍﾟﾎ ﾎﾞﾎﾟﾏ ﾐ ﾑ ﾒ ﾓ ｬ ﾔ ｭ ﾕ ｮ ﾖ ﾗ ﾘ ﾙ ﾚ ﾛ * ﾜ ｲ ｴ ｦ ﾝ ｳﾞ";
	private static final String SUUJI = "〇一二三四五六七八九";

	private static char toFull(char c) {
		if ('!' <= c && c <= '~') {
			return (char) ('！' + (c - '!'));
		}
		return c;
	}

	private static String getHalfKana(final char c) {
		int kanaIndex = c - 'ぁ';
		int ix = kanaIndex < 96 ? kanaIndex : kanaIndex - 96;

		ix *= 2;
		if (0 <= ix && ix < HALF_KANA.length()) {
			return HALF_KANA.substring(ix, ix + 2).trim();
		}
		if (kanaIndex == 187 || kanaIndex == 53021) {
			return "ｰ";
		}
		return "*" + kanaIndex + "*";
	}

	private NameUtils() {
		// nop
	}

	/**
	 * 全角に変換.
	 * @param str 文字列
	 * @return 変換後の文字列
	 */
	public static String toFull(final String str) {
		StringBuilder buff = new StringBuilder();

		for (final char c : str.toCharArray()) {
			buff.append(toFull(c));
		}
		return buff.toString();
	}


	/**
	 * 漢数字に変換.<br/>
	 * 1桁のみ対応.
	 * @param str 文字列
	 * @return 変換後の文字列
	 */
	public static String toKansuuji(final String str) {
		StringBuilder buff = new StringBuilder();

		for (final char c : str.toCharArray()) {
			if ('0' <= c && c <= '9') {
				int ix = c - '0';
				buff.append(SUUJI.charAt(ix));
				continue;
			}
			buff.append(c);
		}
		return buff.toString();
	}

	/**
	 * ひらがなに変換.<br/>
	 * 半角カナおよび全角カナをひらがなに変換
	 * @param str 文字列
	 * @return 変換後の文字列
	 */
	public static String toHiragana(final String str) {
		StringBuilder buff = new StringBuilder();
		char[] chars = str.toCharArray();
		String punctuation = StringUtils.SPACE;

		ArrayUtils.reverse(chars);
		for (final char c : chars) {
			UnicodeBlock block = UnicodeBlock.of(c);

			if (UnicodeBlock.KATAKANA.equals(block)) {
				int diff = c - 'ァ';
				char hira = (char) ('ぁ' + diff);

				buff.append(hira);
			} else if (c == 'ｰ') {
				buff.append('ー');
			} else if (c == 'ﾞ' || c == 'ﾟ') {
				punctuation = String.valueOf(c);
			} else if ('ｦ' <= c && c <= 'ﾝ') {
				int ix = HALF_KANA.indexOf(c + punctuation) / 2;
				char hira = (char) ('ぁ' + ix);

				buff.append(hira);
				punctuation = StringUtils.SPACE;
			} else {
				buff.append(c);
			}
		}
		return buff.reverse().toString();
	}

	/**
	 * カタカナに変換.<br/>
	 * 全角ひらがなを全角カタカナに変換
	 * @param str 文字列
	 * @return 変換後の文字列
	 */
	public static String toKatakana(final String str) {
		StringBuilder buff = new StringBuilder();
		char[] chars = str.toCharArray();
		for (final char c : chars) {
			UnicodeBlock block = UnicodeBlock.of(c);

			if (UnicodeBlock.HIRAGANA.equals(block)) {
				int diff = c - 'ぁ';
				char hira = (char) ('ァ' + diff);

				buff.append(hira);
			} else {
				buff.append(c);
			}
		}
		return buff.toString();
	}

	public static String toHalfKana(final String str) {
		StringBuilder buff = new StringBuilder();

		for (final char c : str.toCharArray()) {
			UnicodeBlock block = UnicodeBlock.of(c);

			if (UnicodeBlock.HIRAGANA.equals(block)
					|| UnicodeBlock.KATAKANA.equals(block)
					|| c == '～') {
				buff.append(getHalfKana(c));
			} else {
				buff.append(c);
			}
		}
		return buff.toString();
	}

	/**
	 * 文字列をシャッフル.
	 * @param str 文字列
	 * @return シャッフル後の文字列
	 */
	public static String shuffle(final String str) {
		Character[] chars = ArrayUtils.toObject(str.toCharArray());
		List<Character> list = Arrays.asList(chars);

		Collections.shuffle(list);
		return StringUtils.join(list, "");
	}
}
