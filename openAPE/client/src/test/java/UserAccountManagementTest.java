import org.openape.client.OpenAPEClient;

public class UserAccountManagementTest {
public static void main(String[] args) {
try{
	OpenAPEClient client = new OpenAPEClient("daniel","du","http://Lukas_Arbeit:4567");
client.changeUserPassword("du", "ich");
} catch (Exception e) {
	e.printStackTrace();
}
}
}