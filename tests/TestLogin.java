import database.UserDao;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLogin{
    @Test
    public void testLogin() {
        String BASE_URL = "http://localhost:8080/SAFEty/api";

        WebTarget target = ClientBuilder.newClient().target(BASE_URL + "/login");

        Form form = new Form();
        form.param("username", "admin");
        form.param("password", "1111");

        Response response = target.request().post(Entity.form(form));
        assertEquals(200, response.getStatus(), "Valid user login test failed");


        Form invalidForm = new Form();
        invalidForm.param("username", "testUser");
        invalidForm.param("password", "wrongPassword");

        Response invalidResponse = target.request().post(Entity.form(invalidForm));
        assertEquals(401, invalidResponse.getStatus(), "Invalid user login test failed");
    }
}
