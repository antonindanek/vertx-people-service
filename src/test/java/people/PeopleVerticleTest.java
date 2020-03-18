package people;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;
import people.model.Person;

@RunWith(VertxUnitRunner.class)
public class PeopleVerticleTest extends AbstractVerticleTest {

	private Person p1 = new Person(1, "Antonin");
	
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
			context.assertEquals("application/json; charset=utf-8", ar.result().getHeader("content-type"));

			context.assertTrue(ar.result().bodyAsJsonArray().isEmpty());

			async.complete();
		});

	}
	
	@Test
	public void testCreate(TestContext context) {
		
		WebClient client = WebClient.create(vertx);

		Async async = context.async();
		
		client.post(port, "localhost", "/persons").sendJson(p1, ar -> {
			context.assertTrue(ar.succeeded());
			context.assertEquals("application/json; charset=utf-8", ar.result().getHeader("content-type"));

			Person result = ar.result().bodyAsJson(Person.class);
			
			context.assertNotNull(result);
			context.assertEquals(p1.getId(), result.getId());
			context.assertEquals(p1.getName(), result.getName());

			async.complete();
		});
		
	}
	
	@Test
	public void testGet(TestContext context) {

		WebClient client = WebClient.create(vertx);

		Async async = context.async();

		client.post(port, "localhost", "/persons").sendJson(p1, context.asyncAssertSuccess(createResponse -> {
			context.assertEquals("application/json; charset=utf-8", createResponse.getHeader("content-type"));

			Person createdPerson = createResponse.bodyAsJson(Person.class);

			client.get(port, "localhost", "/person/" + createdPerson.getId()).send(getResponse -> {

				Person fetchedPerson = getResponse.result().bodyAsJson(Person.class);

				context.assertNotNull(fetchedPerson);
				context.assertEquals(p1.getId(), fetchedPerson.getId());
				context.assertEquals(p1.getName(), fetchedPerson.getName());

				async.complete();
			});
		}));

	}
	
}
