package to.kit.drink.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

public class GaDrive extends GaBase {
	protected static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);
	private static final String APP_NAME = "drunker";

	private Permission createPermission(String fileId) throws IOException {
		GoogleCredential credential = authorize().createScoped(SCOPES);
		Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APP_NAME)
				.build();
		Permission permission = new Permission()
				.setRole("reader")
				.setType("anyone");
		Permission result = service.permissions()
				.create(fileId, permission)
				.execute();
		return result;
	}

	public String create(String name, String type, byte[] bytes) throws IOException {
		GoogleCredential credential = authorize().createScoped(SCOPES);
		Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APP_NAME)
				.build();
		File file = new File().setName(name);
		ByteArrayContent mediaContent = new ByteArrayContent(type, bytes);
		File result = service.files()
				.create(file, mediaContent)
				.setIgnoreDefaultVisibility(Boolean.FALSE)
				.execute();
		String id = result.getId();

		createPermission(id);
		return id;
	}

	public File read(String fileId) throws IOException {
		GoogleCredential credential = authorize().createScoped(SCOPES);
		Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APP_NAME)
				.build();
		String fields = "id,name,mimeType,thumbnailLink,webContentLink,webViewLink";
		File file = service.files()
				.get(fileId)
				.setFields(fields)
				.execute();

//		System.out.println(file.toPrettyString());
		return file;
	}

	public void delete(String fileId) throws IOException {
		GoogleCredential credential = authorize().createScoped(SCOPES);
		Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APP_NAME)
				.build();
		service.files().delete(fileId).execute();
	}

	public String list(String spaces) throws IOException {
		GoogleCredential credential = authorize().createScoped(SCOPES);
		Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APP_NAME)
				.build();
		String fields = "files(id,name,mimeType,thumbnailLink,webContentLink,webViewLink,permissions)";
		FileList files = service.files()
				.list()
				.setSpaces(spaces)
				.setFields(fields)
				.execute();

		for (File file : files.getFiles()) {
			String msg = file.getId() + ":" + file.getName();
			System.out.println(msg);
			System.out.println(file.toPrettyString());
		}
		return "";
	}

	/**
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	public byte[] getImage(String fileId) throws IOException {
		File file = read(fileId);
		String link = file.getWebContentLink();
		GenericUrl url = new GenericUrl(link);
		HttpRequestFactory request = httpTransport.createRequestFactory();
		HttpResponse res = request.buildGetRequest(url).execute();

		return IOUtils.toByteArray(res.getContent());
	}
}
