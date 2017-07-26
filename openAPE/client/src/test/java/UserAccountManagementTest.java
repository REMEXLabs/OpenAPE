import org.openape.client.OpenAPEClient;

public class UserAccountManagementTest {
public static void main(String[] args) {
OpenAPEClient client = new OpenAPEClient("daniel","ich","http://192.168.188.95:4567");
client.changeUserPassword("ich", "du");
}
}
