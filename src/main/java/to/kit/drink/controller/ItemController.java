package to.kit.drink.controller;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.google.api.services.drive.model.File;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import to.kit.drink.data.DataAccessor;
import to.kit.drink.data.DataAccessorFactory;
import to.kit.drink.data.TableRecord;
import to.kit.drink.dto.Description;
import to.kit.drink.dto.Item;
import to.kit.drink.dto.ItemRequest;
import to.kit.drink.rest.GaDrive;
import to.kit.util.NameUtils;
import to.kit.util.RomanConverter;
import to.kit.util.SysEnv;

/**
 * アイテムコントローラー.
 * @author Hidetaka Sasai
 */
public class ItemController extends BaseController<ItemRequest> {
	private static final List<String> EXCLUDE_FIELDS =
			Arrays.asList(new String[] {"abv", "id", "fileId", "filtertext", "imgsrc", "kindId", "tags", "thumbnail", "updated", "user"});
	private static final int MAX_NAME_LENGTH = 100;
	private static final int MAX_DESCRIPTION_LENGTH = 200;
	private DataAccessor dao = DataAccessorFactory.getInstance();
	private GaDrive drive = new GaDrive();
	private RomanConverter romanConverter = RomanConverter.getInstance();

	/**
	 * ユーザー情報取得.
	 * @return ユーザー情報
	 */
	private User getUser() {
		if (!SysEnv.isLocal()) {
			UserService service = UserServiceFactory.getUserService();

			return service.getCurrentUser();
		}
		return new User("ぶちきち", "");
	}

	private Item toItem(String lang, Map<String, ?> map) throws Exception {
		Item item = toBean(map, lang, Item.class);
		String tags = StringUtils.defaultString(item.getTags());

		if (StringUtils.isNotBlank(tags)) {
			item.setTagList(Arrays.asList(tags.split(",", -1)));
		}
		TableRecord descriptionTable = read("description", item.getId());
		Description description = toBean(descriptionTable, lang, Description.class);
		String text = StringUtils.defaultString(description.getText());

		if ("null".equals(text)) {
			text = "";
		}
		item.setDescription(text);
		return item;
	}

	private String makeFilter(TableRecord... records) {
		Set<String> wordSet = new HashSet<>();

		for (TableRecord rec : records) {
			for (Map.Entry<String, Object> entry : rec.entrySet()) {
				if (EXCLUDE_FIELDS.contains(entry.getKey())) {
					continue;
				}
				String word = String.valueOf(entry.getValue());

				wordSet.add(NameUtils.toKatakana(word));
				wordSet.add(word.toLowerCase());
			}
		}
		return StringUtils.join(wordSet, "\t");
	}

	private void saveImage(TableRecord rec, ItemRequest form) throws Exception {
		String id = form.getId();
		String text = form.getText();
		String fileId = (String) rec.get("fileId");

		if (StringUtils.isNotBlank(fileId)) {
			this.drive.delete(fileId);
		}
		File file = this.drive.create(id, text, form.getType(), form.getPicture());

		rec.put("fileId", file.getId());
		rec.put("imgsrc", file.getWebContentLink());
		rec.put("thumbnail", file.getThumbnailLink());
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
		String text = StringUtils.left(form.getText(), MAX_NAME_LENGTH);
		byte[] bytes = form.getPicture();
		String descriptionText = StringUtils.left(form.getDescription(), MAX_DESCRIPTION_LENGTH);
		boolean isNew = StringUtils.isBlank(id);
		TableRecord item = read("item", id);
		TableRecord description = read("description", id);

		if (isNew) {
			for (String key : DEFAULT_LANG) {
				item.put(key, text);
			}
		} else {
			item.put(lang, text);
		}
		if (bytes != null && 0 < bytes.length) {
			saveImage(item, form);
		}
		item.put("kindId", form.getKindId());
		item.put("countryCd", form.getCountryCd());
		item.put("abv", form.getAbv());
		item.put("tags", StringUtils.join(form.getTagList(), ','));
		description.put(lang, descriptionText);
		item.put("filtertext", makeFilter(item, description));
		item.put("user", getUser().getEmail());
		item.put("updated", DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date()));
		this.dao.save(item);
		this.dao.save(description);
		item.put("id", item.getKey());
		return toItem(lang, item);
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
		TableRecord table = read("item", id);

		table.put("id", id);
		Item rec = toItem(lang, table);

		rec.setId(id);
		return rec;
	}

	private String[] makeKeywords(final String keyword) {
		Set<String> set = new HashSet<>();

		if (StringUtils.isNotBlank(keyword)) {
			for (String word : keyword.split("[\\s,]")) {
				String kana = this.romanConverter.convert(word);

				if (StringUtils.isNotBlank(kana)) {
					set.add(NameUtils.toKatakana(kana));
				}
				set.add(NameUtils.toKatakana(word));
				set.add(word);
			}
		}
		return set.toArray(new String[set.size()]);
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
		List<String> countries = form.getCountries();
		List<String> tagList = form.getTagList();
		String[] keywords = makeKeywords(form.getKeyword());

//System.out.println("Item.list: begin.");
		for (Map<String, Object> map : this.dao.list(new TableRecord("item").addSort("en"))) {
//System.out.print("*filter*");
			Item rec = toItem(lang, map);

			if (!countries.isEmpty() && !countries.contains(rec.getCountryCd())) {
				continue;
			}
			if (!tagList.isEmpty()) {
				boolean isMatch = false;

				for (String tagId : tagList) {
					if (rec.getTags().contains(tagId)) {
						isMatch = true;
						break;
					}
				}
				if (!isMatch) {
					continue;
				}
			}
			String filtertext = StringUtils.defaultString(rec.getFiltertext());
			boolean isMatch = keywords.length == 0;

			for (String word : keywords) {
				if (filtertext.contains(word)) {
					isMatch = true;
					break;
				}
			}
			if (!isMatch) {
				continue;
			}
			list.add(rec);
		}
//System.out.println("Item.list: end.");
		return list;
	}

	/**
	 * 新規作成画面へ.
	 * @param form
	 * @return 転送先画面
	 * @throws Exception 例外
	 */
	public Object create(ItemRequest form) throws Exception {
		User user = getUser();

		if (user == null) {
			UserService service = UserServiceFactory.getUserService();

			return new URL(service.createLoginURL("/item/create"));
		}
		return new URI("/detail.html?lang=" + form.getLang());
	}

	@Override
	public Object execute(ItemRequest form) {
		return null;
	}
}
