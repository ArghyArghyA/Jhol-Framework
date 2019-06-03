package managers;

import java.util.HashMap;

import managers.utilities.Authentication;
import managers.utilities.Nothing;

public class APIUtil {
	private String URI = null;
	private int TimeOut = 6000; // in mili-second
	private String ClientCertificate = null;
	private String ClientCertificatePassword = null;
	private Method requestMethod = null;
	private Response AcceptResponseAs = null;
	private HashMap<String, String> Parameter = new HashMap<String, String>();
	private HashMap<String, String> Header = new HashMap<String, String>();
	private HashMap<String, String> URLSegment = new HashMap<String, String>();
	private Authentication authentication = new Nothing();


	public static enum Method {
		GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH, MERGE
	}

	public static enum Response {
		ANY, JSON, XML
	}

	public APIUtil setURI(String URI) {
		this.URI = URI;
		return this;
	}

	public APIUtil setTimeOut(int timeOut) {
		this.TimeOut = timeOut;
		return this;
	}

	public APIUtil setClientCertificate(String clientCertificate) {
		this.ClientCertificate = clientCertificate;
		return this;
	}

	public APIUtil setClientCertificatePassword(String clientCertificatePassword) {
		this.ClientCertificatePassword = clientCertificatePassword;
		return this;
	}

	public APIUtil setParameter(HashMap<String, String> parameter) {
		this.Parameter = parameter;
		return this;
	}

	public APIUtil setHeader(HashMap<String, String> header) {
		this.Header = header;
		return this;
	}

	public APIUtil setURLSegment(HashMap<String, String> uRLSegment) {
		this.URLSegment = uRLSegment;
		return this;
	}

	public APIUtil setRequestMethod(Method requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}

	public APIUtil setAcceptResponseAs(Response acceptResponseAs) {
		this.AcceptResponseAs = acceptResponseAs;
		return this;
	}
	
	public APIUtil setAuthentication(Authentication authentication) {
		this.authentication = authentication;
		return this;
	}
	
	

}
