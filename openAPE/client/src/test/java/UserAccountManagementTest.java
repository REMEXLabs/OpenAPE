import org.openape.client.OpenAPEClient;

public class UserAccountManagementTest {

    public static void main(final String[] args) {
        try {
            final OpenAPEClient client = new OpenAPEClient("daniel", "du",
                    "http://Lukas_Arbeit:4567");
            client.changeUserPassword("du", "ich");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
