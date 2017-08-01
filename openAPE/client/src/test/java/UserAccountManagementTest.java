import org.openape.client.OpenAPEClient;

public class UserAccountManagementTest {
public static void main(String[] args) {
try{
	OpenAPEClient client = new OpenAPEClient("daniel","ich","http://localhost:8080");
client.changeUserPassword("ich", "du");
} catch (Exception e) {
	e.printStackTrace();
}
}
}