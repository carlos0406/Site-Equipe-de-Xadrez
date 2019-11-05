package uteis;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class ControladorPadraoRespostaAPI implements ResponseHandler<String> {

	@Override
	public String handleResponse(HttpResponse r) throws ClientProtocolException, IOException {
		int status = r.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			HttpEntity entity = r.getEntity();
			return entity != null ? EntityUtils.toString(entity) : null;
			
			
			
			
			
		} else {
			int a=0;
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
	}

}
