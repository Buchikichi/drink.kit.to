package to.kit.drink.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.api.services.drive.model.File;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;

import to.kit.drink.data.DataAccessor;
import to.kit.drink.data.DataAccessorFactory;
import to.kit.drink.data.TableRecord;
import to.kit.drink.dto.Item;
import to.kit.drink.dto.ItemRequest;
import to.kit.drink.rest.GaDrive;
import to.kit.sas.control.Controller;

/**
 * アイテムコントローラー.
 * @author Hidetaka Sasai
 */
public class ItemController implements Controller<ItemRequest> {
	/** デフォルト言語. */
	public static final String[] DEFAULT_LANG = { "en", "ja" };

	private DataAccessor dao = DataAccessorFactory.getInstance();
	private GaDrive drive = new GaDrive();

	private Map<String, Object> readItem(String id) throws Exception {
		TableRecord table = new TableRecord("item");

		table.setKey(id);
		return this.dao.read(table);
	}

	private Item toItem(Map<String, ?> map) {
		Item rec = new Item();

		for (String lang : DEFAULT_LANG) {
			if (map.containsKey(lang)) {
				String text = (String) map.get(lang);

				if (StringUtils.isNotBlank(text)) {
					rec.setText(text);
					break;
				}
			}
		}
		for (Field field : FieldUtils.getAllFields(Item.class)) {
			String name = field.getName();

			if (!map.containsKey(name)) {
				continue;
			}
			try {
				FieldUtils.writeField(field, rec, map.get(name), true);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return rec;
	}

	/**
	 * アイテムを保存.
	 * @param form フォーム
	 * @return 保存したアイテム
	 * @throws Exception 例外
	 */
	public Object save(ItemRequest form) throws Exception {
		TableRecord rec = new TableRecord("item");
		String lang = form.getLang();
		String id = form.getId();
		String text = form.getText();
		byte[] bytes = form.getPicture();

		if (StringUtils.isBlank(id)) {
			id = DigestUtils.md5Hex(text);
		} else {
			rec.putAll(readItem(id));
		}
		if (bytes != null) {
			String fileId = (String) rec.get("fileId");

			if (StringUtils.isNotBlank(fileId)) {
				this.drive.delete(fileId);
			}
			File file = this.drive.create(id, text, form.getType(), form.getPicture());

			rec.put("fileId", file.getId());
			rec.put("imgsrc", file.getWebContentLink());
			rec.put("thumbnail", file.getThumbnailLink());
		}
		rec.setKey(id);
		rec.put("kindId", form.getKindId());
		rec.put("countryCd", form.getCountryCd());
		rec.put(lang, text);
		rec.put("abv", form.getAbv());
		this.dao.save(rec);
		rec.put("id", id);
		return toItem(rec);
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
	 * @return アイテム一覧
	 * @throws Exception 例外
	 */
	public Object read(ItemRequest form) throws Exception {
		String lang = form.getLang();
		String id = form.getId();
		Map<String, Object> map = readItem(id);
		Item rec = toItem(map);
		String text = (String) map.get(lang);

		if (StringUtils.isNotBlank(text)) {
			rec.setText(text);
		}
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
			Item rec = toItem(map);
			String text = (String) map.get(lang);

			if (StringUtils.isNotBlank(text)) {
				rec.setText(text);
			}
			list.add(rec);
		}
		return list;
	}

	@Override
	public Object execute(ItemRequest form) {
		return null;
	}
}
