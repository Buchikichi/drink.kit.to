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

	/**
	 * 言語を取得.
	 * @param form フォーム
	 * @return 言語一覧
	 */
	public Object language(ListRequest form) {
		String lang = form.getLang();
		DataAccessor dataAccessor = DataAccessorFactory.getInstance();
		List<Language> list = new ArrayList<>();

		try {
			for (Map<String, String> map : dataAccessor.list("iso639")) {
				Language rec = new Language();
				String text = map.get(lang);

				rec.setId(map.get("id"));
				if (text == null || text.isEmpty()) {
					text = map.get(DEFAULT_LANG);
				}
				rec.setText(text);
				list.add(rec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 国を取得.
	 * @param form フォーム
	 * @return 国一覧
	 */
	public Object country(ListRequest form) {
		DataAccessor dataAccessor = DataAccessorFactory.getInstance();
		List<Country> list = new ArrayList<>();
		String lang = form.getLang();

		try {
			for (Map<String, String> map : dataAccessor.list("iso3166")) {
				Country rec = new Country();
				String text = map.get(lang);

				rec.setId(map.get("id"));
				rec.setFlag(map.get("flag"));
				if (text == null || text.isEmpty()) {
					text = map.get(DEFAULT_LANG);
				}
				rec.setText(text);
				list.add(rec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Object execute(ListRequest form) {
		return null;
	}
}
