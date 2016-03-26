package to.kit.drink.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import to.kit.drink.data.DataAccessor;
import to.kit.drink.data.DataAccessorFactory;
import to.kit.drink.data.TableRecord;
import to.kit.drink.dto.Tags;
import to.kit.drink.dto.TagsRequest;

/**
 * タグコントローラー.
 * @author Hidetaka Sasai
 */
public class TagsController extends BaseController<TagsRequest> {
	private DataAccessor dao = DataAccessorFactory.getInstance();

	/**
	 * タグを保存.
	 * @param form フォーム
	 * @return 保存したタグ
	 * @throws Exception 例外
	 */
	public Object save(TagsRequest form) throws Exception {
		String lang = form.getLang();
		String id = form.getId();
		String text = form.getText();
		TableRecord rec = read("tags", id);

		rec.put(lang, text);
		rec.remove("filtertext");
		rec.put("filtertext", StringUtils.join(rec.values(), ","));
		this.dao.save(rec);
		rec.put("id", rec.getKey());
		return toBean(rec, lang, Tags.class);
	}

//	/**
//	 * タグを取得.
//	 * @param form フォーム
//	 * @return タグ
//	 * @throws Exception 例外
//	 */
//	public Object read(TagsRequest form) throws Exception {
//		String lang = form.getLang();
//		String id = form.getId();
//		TableRecord rec = new TableRecord("tags").setKey(id);
//
//		read(rec);
//		Tags tags = toBean(rec, Tags.class);
//		String text = (String) rec.get(lang);
//		if (StringUtils.isNotBlank(text)) {
//			tags.setText(text);
//		}
//		tags.setId(id);
//		return rec;
//	}

	/**
	 * タグ一覧を取得.
	 * @param form フォーム
	 * @return タグ一覧
	 * @throws Exception 例外
	 */
	public Object list(TagsRequest form) throws Exception {
		List<Tags> list = new ArrayList<>();
		String lang = form.getLang();

		for (Map<String, Object> map : this.dao.list(new TableRecord("tags"))) {
			list.add(toBean(map, lang, Tags.class));
		}
		return list;
	}
}
