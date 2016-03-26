package to.kit.drink.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.api.services.drive.model.File;

import to.kit.drink.data.DataAccessor;
import to.kit.drink.data.DataAccessorFactory;
import to.kit.drink.data.TableRecord;
import to.kit.drink.dto.Item;
import to.kit.drink.dto.ItemRequest;
import to.kit.drink.rest.GaDrive;

/**
 * アイテムコントローラー.
 * @author Hidetaka Sasai
 */
public class ItemController extends BaseController<ItemRequest> {
	private DataAccessor dao = DataAccessorFactory.getInstance();
	private GaDrive drive = new GaDrive();

	private Item toItem(String lang, Map<String, ?> map) throws Exception {
		Item item = toBean(map, lang, Item.class);
		String tags = StringUtils.defaultString(item.getTags());

		if (StringUtils.isNotBlank(tags)) {
			item.setTagList(Arrays.asList(tags.split(",", -1)));
		}
		return item;
	}

	/**
	 * アイテムを保存.
	 * @param form フォーム
	 * @return 保存したアイテム
	 * @throws Exception 例外
	 */
	public Object save(ItemRequest form) throws Exception {
		String lang = form.getLang();
		String id = form.getId();
		String text = form.getText();
		byte[] bytes = form.getPicture();
		TableRecord rec = new TableRecord("item").setKey(id);

		read(rec);
		if (bytes != null && 0 < bytes.length) {
			String fileId = (String) rec.get("fileId");

			if (StringUtils.isNotBlank(fileId)) {
				this.drive.delete(fileId);
			}
			File file = this.drive.create(id, text, form.getType(), form.getPicture());

			rec.put("fileId", file.getId());
			rec.put("imgsrc", file.getWebContentLink());
			rec.put("thumbnail", file.getThumbnailLink());
		}
		rec.put("kindId", form.getKindId());
		rec.put("countryCd", form.getCountryCd());
		rec.put(lang, text);
		rec.put("abv", form.getAbv());
		rec.put("tags", StringUtils.join(form.getTagList(), ','));
		this.dao.save(rec);
		rec.put("id", id);
		return toItem(lang, rec);
	}

	/**
	 * イメージを取得.
	 * @param path パス
	 * @return バックグラウンドイメージ
	 * @throws Exception 例外
	 */
	public byte[] image(String path) throws Exception {
		byte[] image = null;
		TableRecord table = new TableRecord("item");

		table.setKey(path);
		Map<String, Object> map = this.dao.read(table);
		String imgsrc = (String) map.get("imgsrc");

		if (imgsrc != null && !imgsrc.isEmpty()) {
			byte[] bytes = this.drive.getImage(imgsrc);

			image = bytes;
		}
//		try (InputStream in = ActorController.class.getResourceAsStream(name)) {
//			image = IOUtils.toByteArray(in);
//		} catch (@SuppressWarnings("unused") IOException e) {
//			// nop
//		}
		return image;
	}

	/**
	 * アイテムを取得.
	 * @param form フォーム
	 * @return アイテム
	 * @throws Exception 例外
	 */
	public Object read(ItemRequest form) throws Exception {
		String lang = form.getLang();
		String id = form.getId();
		TableRecord table = new TableRecord("item").setKey(id);

		read(table);
		Item rec = toItem(lang, table);
		rec.setId(id);
		return rec;
	}

	/**
	 * アイテム一覧を取得.
	 * @param form フォーム
	 * @return アイテム一覧
	 * @throws Exception 例外
	 */
	public Object list(ItemRequest form) throws Exception {
		List<Item> list = new ArrayList<>();
		String lang = form.getLang();

		for (Map<String, Object> map : this.dao.list("item")) {
			Item rec = toItem(lang, map);

			list.add(rec);
		}
		return list;
	}

	@Override
	public Object execute(ItemRequest form) {
		return null;
	}
}
