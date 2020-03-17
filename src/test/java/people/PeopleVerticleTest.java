package people;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;

@RunWith(VertxUnitRunner.class)
public class PeopleVerticleTest extends AbstractVerticleTest {

	@Override
	protected String getTestVerticleName() {
		return PeopleVerticle.class.getName();
	}
	
	@Test
	public void testEmpty(TestContext context) {

		WebClient client = WebClient.create(vertx);

		Async async = context.async();

		client.get(port, "localhost", "/persons").send(ar -> {
			context.assertTrue(ar.succeeded());
			context.assertEquals(ar.result().getHeader("content-type"), "application/json; charset=utf-8");

			context.assertTrue(ar.result().bodyAsJsonArray().isEmpty());

			async.complete();
		});

	}
}
