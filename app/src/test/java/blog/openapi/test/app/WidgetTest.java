package blog.openapi.test.app;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class WidgetTest {

    @Test
    public void testJaxrs() {
        RestAssured.when().get("/ping").then()
                .contentType("text/plain")
                .body(equalTo("Pongz..."));
    }

}
