import org.openape.api.usercontext.UserContext;
import org.openape.client.OpenAPEClient;

public class OpenApeClientTester {
static String userContextId = "592d48b938f553545b8a8dd0";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
System.out.println("starte test");
		try{
		OpenAPEClient client = new OpenAPEClient("daniel","ich");
UserContext userContext = client.getUserContext(userContextId);
	System.out.println(userContext);
	System.out.println("erledigt");

} catch (Exception e) {
	e.printStackTrace();
}
}
}