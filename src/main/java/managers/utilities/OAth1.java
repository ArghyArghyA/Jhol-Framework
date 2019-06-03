package managers.utilities;

public class OAth1 implements Authentication {
	String ConsumerKey;
	String ConsumerSecret;
	String AccessToken;
	String AccessTokenSecret;

	public OAth1(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		ConsumerKey = consumerKey;
		ConsumerSecret = consumerSecret;
		AccessToken = accessToken;
		AccessTokenSecret = accessTokenSecret;
	}
}
