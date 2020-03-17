package people;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import people.config.ConfigReader;

public abstract class AbstractVerticleTest {

	protected Vertx vertx;
	protected Integer port = ConfigReader.getPort();

	protected abstract String getTestVerticleName();
	
	@Before
	public void setUp(TestContext context) throws IOException {

		vertx = Vertx.vertx();
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));
		vertx.deployVerticle(getTestVerticleName(), options, context.asyncAssertSuccess());
	}


	@After
	public void tearDown(TestContext context) {

		vertx.close(context.asyncAssertSuccess());
	}	
	
}
