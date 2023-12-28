package blog.openapi.test.app;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class WidgetTest {

    @Test
    public void testJaxrs() {
        RestAssured.when().get("/ping").then()
                .contentType("text/plain")
                .body(equalTo("Pong..."));
    }

    @Test
    public void testAccountCosts() {
        RestAssured.when().get("/cost/accounts").then()
            .contentType("application/json")
            .body("timePeriod.start.month", equalTo("APRIL"));
    }

    @Test
    public void testAccountCosts2() {
        RestAssured.when().get("/cost/accounts")
            .then()
            .contentType("application/json")
            .body("timePeriod.start.month", equalTo("APRIL"));
    }
}
