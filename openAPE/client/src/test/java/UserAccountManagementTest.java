import org.openape.client.OpenAPEClient;

public class UserAccountManagementTest {
public static void main(String[] args) {
try {
//	OpenAPEClient client = new OpenAPEClient("daniel","ich","http://192.168.188.95:4567");
	OpenAPEClient client = new OpenAPEClient("daniel","ich");
client.changeUserPassword("ich", "du");
} catch (Exception e) {
	e.printStackTrace();
}
}
}
