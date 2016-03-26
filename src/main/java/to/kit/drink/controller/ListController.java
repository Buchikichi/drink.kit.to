package to.kit.drink.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import to.kit.drink.data.DataAccessor;
import to.kit.drink.data.DataAccessorFactory;
import to.kit.drink.dto.Country;
import to.kit.drink.dto.Language;
import to.kit.drink.dto.ListRequest;

/**
 * 一覧系コントローラー.
 * @author Hidetaka Sasai
 */
public class ListController extends BaseController<ListRequest> {
	private DataAccessor dao = DataAccessorFactory.getInstance();

	/**
	 * 言語を取得.
	 * @param form フォーム
	 * @return 言語一覧
	 * @throws Exception 例外
	 */
	public Object language(ListRequest form) throws Exception {
		List<Language> list = new ArrayList<>();
		String lang = form.getLang();

		for (Map<String, Object> map : this.dao.list("iso639")) {
			list.add(toBean(map, lang, Language.class));
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
			list.add(toBean(map, lang, Country.class));
		}
		return list;
	}

	@Override
	public Object execute(ListRequest form) {
		return null;
	}
}
