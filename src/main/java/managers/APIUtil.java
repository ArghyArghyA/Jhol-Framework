package managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import managers.utilities.Authentication;
import managers.utilities.Nothing;

public class APIUtil {
	private static String URI = null;
	private static int TimeOut = 6000; // in mili-second
	private static String ClientCertificate = null;
	private static String ClientCertificatePassword = null;
	private static Method requestMethod = null;
	private static Response AcceptResponseAs = null;
	private static HashMap<String, String> Parameter = new HashMap<String, String>();
	private static HashMap<String, String> Header = new HashMap<String, String>();
	private static HashMap<String, String> URLSegment = new HashMap<String, String>();
	private static Authentication authentication = new Nothing();
	private static URLConnection Connection;

	public enum Method {
		GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH, MERGE
	}

	public enum Response {
		ANY, JSON, XML
	}

	public APIUtil setURI(String URI) {
		APIUtil.URI = URI;
		return this;
	}

	public APIUtil setTimeOut(int timeOut) {
		APIUtil.TimeOut = timeOut;
		return this;
	}

	public APIUtil setClientCertificate(String clientCertificate) {
		APIUtil.ClientCertificate = clientCertificate;
		return this;
	}

	public APIUtil setClientCertificatePassword(String clientCertificatePassword) {
		APIUtil.ClientCertificatePassword = clientCertificatePassword;
		return this;
	}

	public APIUtil setParameter(HashMap<String, String> parameter) {
		APIUtil.Parameter = parameter;
		return this;
	}

	public APIUtil setHeader(HashMap<String, String> header) {
		APIUtil.Header = header;
		return this;
	}

	public APIUtil setURLSegment(HashMap<String, String> uRLSegment) {
		APIUtil.URLSegment = uRLSegment;
		return this;
	}

	public APIUtil setRequestMethod(Method requestMethod) {
		APIUtil.requestMethod = requestMethod;
		return this;
	}

	public APIUtil setAcceptResponseAs(Response acceptResponseAs) {
		APIUtil.AcceptResponseAs = acceptResponseAs;
		return this;
	}

	public APIUtil setAuthentication(Authentication authentication) {
		APIUtil.authentication = authentication;
		return this;
	}

	public void GET() throws IOException {
		Connection = new URL(APIUtil.URI + generateQuery()).openConnection();
		generateHeader();
		InputStream response = Connection.getInputStream();
		String line;
		while ((line = new BufferedReader(new InputStreamReader(response)).readLine())!= null)
			System.out.println(line);
	}

	private String generateQuery() throws UnsupportedEncodingException {
		String query = "";
		for (String key : APIUtil.Parameter.keySet()) {
			if (query.length() == 0)
				query = "?" + key + "=" + URLEncoder.encode(Parameter.get(key), StandardCharsets.UTF_8.name());
			else
				query = query + "&" + key + "=" + URLEncoder.encode(Parameter.get(key), StandardCharsets.UTF_8.name());
		}
		System.out.println(query);
		return query;
	}

	private void generateHeader() {
		for (String key : APIUtil.Header.keySet()) {
			Connection.setRequestProperty(key, APIUtil.Header.get(key));
		}
	}

}
