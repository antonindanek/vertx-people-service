package people;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;
import people.model.Person;

@RunWith(VertxUnitRunner.class)
public class PeopleVerticleTest extends AbstractVerticleTest {

	private Person p1 = new Person(1, "Antonin");
	private Person p2 = new Person(2, "Michal");

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
	public void testCreateDuplicate(TestContext context) {

		WebClient client = WebClient.create(vertx);

		Async async = context.async();

		client.post(port, "localhost", "/persons").sendJson(p1, context.asyncAssertSuccess(createResponse1 -> {

			context.assertTrue(createResponse1.statusCode() == 200);

			Person createdPerson = createResponse1.bodyAsJson(Person.class);
			context.assertNotNull(createdPerson);

			client.post(port, "localhost", "/persons").sendJson(p1, context.asyncAssertSuccess(createResponse2 -> {
				context.assertTrue(createResponse2.statusCode() == 400);

			}));

			async.complete();
		}));

	}

	@Test
	public void testGet(TestContext context) {

		WebClient client = WebClient.create(vertx);

		Async async = context.async();

		client.post(port, "localhost", "/persons").sendJson(p1, context.asyncAssertSuccess(createResponse -> {

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

	@Test
	public void testGetAll(TestContext context) {

		WebClient client = WebClient.create(vertx);

		Async async = context.async();

		client.post(port, "localhost", "/persons").sendJson(p1, context.asyncAssertSuccess(createResponse1 -> {
			client.post(port, "localhost", "/persons").sendJson(p2, context.asyncAssertSuccess(createResponse2 -> {
				client.get(port, "localhost", "/persons").send(getResponse -> {

					List<Person> allPersons = getResponse.result().bodyAsJsonArray().stream()
							.map(jsonObject -> ((JsonObject) jsonObject).mapTo(Person.class))
							.collect(Collectors.toList());

					context.assertNotNull(allPersons);
					context.assertFalse(allPersons.isEmpty());
					context.assertTrue(allPersons.contains(p1));
					context.assertTrue(allPersons.contains(p2));

					async.complete();
				});
			}));
		}));

	}

}
