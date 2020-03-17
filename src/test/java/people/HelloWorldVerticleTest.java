package people;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;

@RunWith(VertxUnitRunner.class)
public class HelloWorldVerticleTest extends AbstractVerticleTest {

	@Override
	protected String getTestVerticleName() {
		return HelloWorldVerticle.class.getName();
	}

	@Test
	public void testMyApplication(TestContext context) {

		WebClient client = WebClient.create(vertx);

		Async async = context.async();

		client.get(port, "localhost", "/").send(ar -> {
			context.assertTrue(ar.succeeded());

			context.assertTrue(ar.result().bodyAsString().contains("Hello"));

			async.complete();
		});
	}

}
