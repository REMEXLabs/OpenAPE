import org.openape.api.usercontext.UserContext;
import org.openape.client.OpenAPEClient;

public class OpenApeClientTester {
	static String userContextId = "592d48b938f553545b8a8dd0";

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		System.out.println("starte test");
		try {
			final OpenAPEClient client = new OpenAPEClient("daniel", "ich", "http://localhost:4567");
			final UserContext userContext = client.getUserContext(OpenApeClientTester.userContextId);
			System.out.println(userContext);
			System.out.println("erledigt");

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
