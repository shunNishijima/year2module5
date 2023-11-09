import database.UserDatabase;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUpdate {
    String username = "testuser";
    String gps = "TestGPS";
    @Test
    public void testUpdateGPS() {
        UserDatabase.updateGPS(username, gps);

        String updatedGPS = UserDatabase.getGPS(username);
        assertEquals(gps, updatedGPS);
    }

    @Test
    public void testUpdatePassword() {
        String username = "testuser";
        String newPassword = "newPassword123";

        UserDatabase.updatePassword(username, newPassword);
    }
}
