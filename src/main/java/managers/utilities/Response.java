package managers.utilities;

import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Response {
	public int StatusCode;
	public HashMap<String, String> Header;
	public String Result;

	public JSONArray toJSON() {
		return new JSONArray(Result);
	}

	public Document toXML() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(Result)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}