
import database.UserDatabase;
import jakarta.ws.rs.core.Response;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import resources.UserResource;

    public class TestDatabase {
        @Test
        public void testUserDao() {
            // Test User Insertion and Selection
            User user = new User("testuser", "testpassword");
            UserDatabase.insertUser(user);
            User selectedUser = UserDatabase.selectUser("testuser");
            Assertions.assertNotNull(selectedUser);
            Assertions.assertEquals("testuser", selectedUser.getName());
            Assertions.assertEquals("testpassword", selectedUser.getPassword());

        }

        @Test
        public void testUserResource() {
            // Test User Signup and Login
            UserResource userResource = new UserResource();
            Response response = userResource.signup("testuser2", "testpassword2");
            Assertions.assertEquals(200, response.getStatus());

            response = userResource.login("testuser2", "testpassword2");
            Assertions.assertEquals(200, response.getStatus());

            response = userResource.login("testuser2", "wrongpassword");
            Assertions.assertEquals(401, response.getStatus());
        }
    }
