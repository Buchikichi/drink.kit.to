package to.kit.drink.controller;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;

import to.kit.drink.data.DataAccessor;
import to.kit.drink.data.DataAccessorFactory;
import to.kit.drink.data.TableRecord;
import to.kit.drink.dto.Multilingual;
import to.kit.sas.control.Controller;

/**
 * 基底コントローラー.
 * @author Hidetaka Sasai
 * @param <R> リクエストパラメーターなど
 */
public abstract class BaseController<R> implements Controller<R> {
	/** デフォルト言語. */
	protected static final String[] DEFAULT_LANG = { "en", "ja" };

	/** データアクセス. */
	private final DataAccessor dao = DataAccessorFactory.getInstance();

	protected void read(TableRecord table) throws Exception {
		String key = table.getKey();

		if (key == null || key.trim().isEmpty()) {
			String uuid = UUID.randomUUID().toString();
			String id = DigestUtils.md5Hex(uuid);

			table.setKey(id);
		} else {
			Map<String, Object> map = this.dao.read(table);

			table.putAll(map);
		}
	}

	protected <T extends Multilingual> T toBean(Map<String, ?> map, String targetLang, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		T rec = clazz.newInstance();
		String text = (String) map.get(targetLang);

		if (StringUtils.isNotBlank(text)) {
			rec.setText(text);
		} else {
			for (String lang : DEFAULT_LANG) {
				text = (String) map.get(lang);

				if (StringUtils.isNotBlank(text)) {
					rec.setText(text);
					break;
				}
			}
		}
		for (Field field : FieldUtils.getAllFields(clazz)) {
			String name = field.getName();

			if (!map.containsKey(name)) {
				continue;
			}
			try {
				FieldUtils.writeField(field, rec, map.get(name), true);
			} catch (@SuppressWarnings("unused") Exception e) {
				//e.printStackTrace();
			}
		}
		return rec;
	}

	@Override
	public Object execute(R form) {
		return null;
	}
}
