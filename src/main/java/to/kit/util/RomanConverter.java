package to.kit.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class RomanConverter {
	private static final String[][] CONSONANT_LIST = {
		{"a", "あ", "い", "う", "え", "お"},
		{"b", "ば", "び", "ぶ", "べ", "ぼ"},
		{"c", "か", "し", "く", "せ", "こ"},
		{"ch", "ちゃ", "ち", "ちゅ", "ちぇ", "ちょ"},
		{"cy", "ちゃ", "ち", "ちゅ", "ちぇ", "ちょ"},
		{"d", "だ", "ぢ", "づ", "で", "ど"},
		{"f", "ふぁ", "ふぃ", "ふ", "ふぇ", "ふぉ"},
		{"g", "が", "ぎ", "ぐ", "げ", "ご"},
		{"h", "は", "ひ", "ふ", "へ", "ほ"},
		{"j", "じゃ", "じ", "じゅ", "じぇ", "じょ"},
		{"k", "か", "き", "く", "け", "こ"},
		{"l", "ぁ", "ぃ", "ぅ", "ぇ", "ぉ"},
		{"ly", "ゃ", "ぃ", "ゅ", "ぇ", "ょ"},
		{"lw", "ゎ", "", "", "", ""},
		{"m", "ま", "み", "む", "め", "も"},
		{"n", "な", "に", "ぬ", "ね", "の"},
		{"p", "ぱ", "ぴ", "ぷ", "ぺ", "ぽ"},
		{"q", "くぁ", "くぃ", "く", "くぇ", "くぉ"},
		{"r", "ら", "り", "る", "れ", "ろ"},
		{"s", "さ", "し", "す", "せ", "そ"},
		{"sh", "しゃ", "し", "しゅ", "しぇ", "しょ"},
		{"t", "た", "ち", "つ", "て", "と"},
		{"v", "ゔぁ", "ゔぃ", "ゔ", "ゔぇ", "ゔぉ"},
		{"w", "わ", "ゐ", "う", "ゑ", "を"},
		{"x", "ぁ", "ぃ", "ぅ", "ぇ", "ぉ"},
		{"y", "や", "ゐ", "ゆ", "ゑ", "よ"},
		{"z", "ざ", "じ", "ず", "ぜ", "ぞ"},
	};
	/** The only instance. */
	private static final RomanConverter SELF = new RomanConverter();
	/** Mapping. */
	private Map<String, String[]> consonantMap = new HashMap<>();

	private RomanConverter() {
		// consonant:子音, vowel:母音
		for (String[] row : CONSONANT_LIST) {
			String key = row[0];
			String[] value = Arrays.copyOfRange(row, 1, row.length);

			this.consonantMap.put(key, value);
		}
	}

	private String choice(String consonant, char vowel) {
		StringBuilder buff = new StringBuilder();
		String[] row = this.consonantMap.get(consonant);

		if (row == null && 1 < consonant.length()) {
			String key = consonant.substring(1);
			char first = consonant.charAt(0);
			char second = key.charAt(0);

			if (first == 'm' || first == 'n') {
				buff.append('ん');
			} else if (first == second) {
				buff.append('っ');
			}
			row = this.consonantMap.get(key);
		}
		if (row != null) {
			int index = vowel == 'a' ? 0 : vowel == 'i' ? 1 : vowel == 'u' ? 2
					: vowel == 'e' ? 3 : 4;
			buff.append(row[index]);
		}
		return buff.toString();
	}

	private String choice(String candidate) {
		StringBuilder buff = new StringBuilder();
		int splitPos = candidate.length() - 1;
		String consonant = candidate.substring(0, splitPos);
		int consonantLen = consonant.length();
		char vowel = candidate.charAt(splitPos);

		if (consonantLen == 0) {
			// only vowel
			buff.append(choice("a", vowel));
		} else if (1 < consonantLen && consonant.endsWith("y")) {
			// 拗音
			String first = String.valueOf(consonant.charAt(0));
			buff.append(choice(first, 'i'));
			buff.append(choice("ly", vowel));
		} else if ("dh".equals(consonant) || "th".equals(consonant)) {
			// でぃてぃ
			String first = String.valueOf(consonant.charAt(0));
			buff.append(choice(first, 'e'));
			buff.append(choice("ly", vowel));
		} else {
			buff.append(choice(consonant, vowel));
		}
		return buff.toString();
	}

	private String[] chop(String roman) {
		StringBuilder buff = new StringBuilder();
		boolean isLastTarget = true;

		for (char ch : roman.toLowerCase().toCharArray()) {
			boolean isTarget = 'a' <= ch && ch <= 'z' || ch == '-';
			if (isTarget && !isLastTarget) {
				buff.append(" ");
			}
			buff.append(ch);
			if (ch == 'a' || ch == 'i' || ch == 'u' || ch == 'e' || ch == 'o'
					|| ch == '-') {
				buff.append(" ");
			}
			isLastTarget = isTarget;
		}
		return buff.toString().split("[\\s']");
	}

	public String convert(String roman) {
		StringBuilder buff = new StringBuilder();

		for (String candidate : chop(roman)) {
			if (!candidate.matches("[a-z-]+")) {
				buff.append(candidate);
				continue;
			}
			if ("n".equals(candidate)) {
				buff.append('ん');
				continue;
			}
			if ("-".equals(candidate)) {
				buff.append('ー');
				continue;
			}
			if (candidate.length() == 1 && !candidate.matches("[aiueo]")) {
				// The middle of the input.
				continue;
			}
			buff.append(choice(candidate));
		}
		return buff.toString();
	}

	public static RomanConverter getInstance() {
		return SELF;
	}
}
