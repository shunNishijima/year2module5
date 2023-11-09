import database.UserDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBruteForce {
    @Test
    void testDeleteBrute() {
        UserDatabase.addBrute();
        UserDatabase.deleteBrute();
        int count = UserDatabase.getBrute();
        assertEquals(0, count, "Brute count should be 0 after deleting all entries");
    }

    @Test
    void testAddAndGetBrute() {
        int count = UserDatabase.getBrute();
        UserDatabase.addBrute();
        int count2 = UserDatabase.getBrute();
        assertEquals(1, (count2-count), "Brute count should be 1 after adding one entry");
    }

}
