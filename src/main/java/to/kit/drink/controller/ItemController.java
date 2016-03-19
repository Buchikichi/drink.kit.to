package to.kit.drink.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.api.services.drive.model.File;

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
	public static final String DEFAULT_LANG = "en";

	private GaDrive drive = new GaDrive();

	/**
	 * イメージを取得.
	 * @param path パス
	 * @return バックグラウンドイメージ
	 * @throws Exception 例外
	 */
	public byte[] image(String path) throws Exception {
		byte[] image = null;
		DataAccessor dataAccessor = DataAccessorFactory.getInstance();
		TableRecord table = new TableRecord("item");

		table.setKey(path);
		Map<String, String> map = dataAccessor.read(table);
		String imgsrc = map.get("imgsrc");

this.drive.list("");
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
		DataAccessor dataAccessor = DataAccessorFactory.getInstance();
		String lang = form.getLang();
		TableRecord table = new TableRecord("item");

		table.setKey(form.getId());
		Map<String, String> map = dataAccessor.read(table);

		Item rec = new Item();
		String text = map.get(lang);
		String imgsrc = map.get("imgsrc");

		rec.setId(map.get("id"));
		rec.setKindId(map.get("kindId"));
		rec.setCountryCd(map.get("countryCd"));
		if (imgsrc != null && !imgsrc.isEmpty()) {
			File file = this.drive.read(imgsrc);

			rec.setImgsrc(file.getWebContentLink());
		}
		if (text == null || text.isEmpty()) {
			text = map.get(DEFAULT_LANG);
		}
		rec.setText(text);
		return rec;
	}

	/**
	 * アイテム一覧を取得.
	 * @param form フォーム
	 * @return アイテム一覧
	 * @throws Exception 例外
	 */
	public Object list(ItemRequest form) throws Exception {
		DataAccessor dataAccessor = DataAccessorFactory.getInstance();
		List<Item> list = new ArrayList<>();
		String lang = form.getLang();

		try {
			for (Map<String, String> map : dataAccessor.list("item")) {
				Item rec = new Item();
				String text = map.get(lang);
				String imgsrc = map.get("imgsrc");

				rec.setId(map.get("id"));
				rec.setKindId(map.get("kindId"));
				rec.setCountryCd(map.get("countryCd"));
				if (imgsrc != null && !imgsrc.isEmpty()) {
					File file = this.drive.read(imgsrc);

					rec.setImgsrc(file.getThumbnailLink());
				}
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
	public Object execute(ItemRequest form) {
		return null;
	}
}
