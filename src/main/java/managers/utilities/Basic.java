package managers.utilities;

public class Basic implements Authentication {
	String UserName;
	String Password;

	public Basic(String UserName, String Password) {
		this.UserName = UserName;
		this.Password = Password;
	}
}
