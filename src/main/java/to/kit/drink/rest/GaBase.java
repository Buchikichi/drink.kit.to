package to.kit.drink.rest;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public abstract class GaBase {
	protected static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	protected static HttpTransport httpTransport;

	{
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Credential
	 * @throws IOException
	 */
	public GoogleCredential authorize() throws IOException {
		GoogleCredential credential;

		try (InputStream in = GaBase.class.getResourceAsStream("/drunker-2fed4777714b.json")) {
			credential = GoogleCredential.fromStream(in, httpTransport, JSON_FACTORY);
		}
//		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

/*		GoogleCredential credentialB = new GoogleCredential.Builder()
				.setTransport(HTTP_TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountUser("drink-kit-to@sonic-diorama-91601.iam.gserviceaccount.com")
				.setClientSecrets(clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret())
				.setServiceAccountId(serviceAccountId)
				.setServiceAccountScopes(SCOPES)
//				.setServiceAccountPrivateKey(serviceAccountPrivateKey)
				.build();
//*/
		return credential;
	}
}
