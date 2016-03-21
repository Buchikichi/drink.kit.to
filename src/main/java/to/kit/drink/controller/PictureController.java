package to.kit.drink.controller;

import java.io.IOException;
import java.util.UUID;

import com.google.api.client.util.Base64;
import com.google.api.services.drive.model.File;

import to.kit.drink.data.DataAccessor;
import to.kit.drink.data.DataAccessorFactory;
import to.kit.drink.data.TableRecord;
import to.kit.drink.dto.Picture;
import to.kit.drink.dto.UploadRequest;
import to.kit.drink.rest.GaDrive;
import to.kit.sas.control.Controller;

/**
 * 画像コントローラー.
 * @author Hidetaka Sasai
 */
public class PictureController implements Controller<Object> {
	private TableRecord setupRecord(String uuid, UploadRequest form) {
		TableRecord rec = new TableRecord("picture");
		String src = Base64.encodeBase64String(form.getPicture());

		rec.put("uuid", uuid);
		rec.put("name", form.getName());
		rec.put("src", src);
		return rec;
	}

	public Object create(UploadRequest form) throws IOException {
		Picture result = new Picture();
		String name = form.getName();
		String type = form.getType();
		byte[] bytes = form.getPicture();
		GaDrive drive = new GaDrive();
		File file = drive.create(name, name, type, bytes);

		result.setId(file.getId());
		return result;
	}

	public Object upload(UploadRequest form) {
		String uuid = UUID.randomUUID().toString();
		TableRecord rec = setupRecord(uuid, form);
		DataAccessor da = DataAccessorFactory.getInstance();

		try {
			da.save(rec);
		} catch (Exception e) {
			e.printStackTrace();
			uuid = null;
		}
		return new String[] { "uuid:" + uuid, "name:" + form.getName() };
	}

	public Object getImage(UploadRequest form) {
		Picture result = new Picture();
		GaDrive drive = new GaDrive();

//		try {
//			drive.read(form.get);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return result;
	}

	@Override
	public Object execute(Object form) {
		return null;
	}
}
