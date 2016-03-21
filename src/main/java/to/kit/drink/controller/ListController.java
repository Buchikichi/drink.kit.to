package to.kit.drink.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import to.kit.drink.data.DataAccessor;
import to.kit.drink.data.DataAccessorFactory;
import to.kit.drink.dto.Country;
import to.kit.drink.dto.Language;
import to.kit.drink.dto.ListRequest;
import to.kit.sas.control.Controller;

/**
 * 一覧系コントローラー.
 * @author Hidetaka Sasai
 */
public class ListController implements Controller<ListRequest> {
	public static final String DEFAULT_LANG = "en";

	private DataAccessor dao = DataAccessorFactory.getInstance();

	/**
	 * 言語を取得.
	 * @param form フォーム
	 * @return 言語一覧
	 * @throws Exception 例外
	 */
	public Object language(ListRequest form) throws Exception {
		String lang = form.getLang();
		List<Language> list = new ArrayList<>();

		for (Map<String, Object> map : this.dao.list("iso639")) {
			Language rec = new Language();
			String text = (String) map.get(lang);

			rec.setId((String) map.get("id"));
			if (text == null || text.isEmpty()) {
				text = (String) map.get(DEFAULT_LANG);
			}
			rec.setText(text);
			list.add(rec);
		}
		return list;
	}

	/**
	 * 国を取得.
	 * @param form フォーム
	 * @return 国一覧
	 * @throws Exception 例外
	 */
	public Object country(ListRequest form) throws Exception {
		List<Country> list = new ArrayList<>();
		String lang = form.getLang();

		for (Map<String, Object> map : this.dao.list("iso3166")) {
			Country rec = new Country();
			String text = (String) map.get(lang);

			rec.setId((String) map.get("id"));
			rec.setFlag((String) map.get("flag"));
			if (text == null || text.isEmpty()) {
				text = (String) map.get(DEFAULT_LANG);
			}
			rec.setText(text);
			list.add(rec);
		}
		return list;
	}

	@Override
	public Object execute(ListRequest form) {
		return null;
	}
}
