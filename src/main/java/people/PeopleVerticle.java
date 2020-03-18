package people;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ParameterType;
import io.vertx.ext.web.handler.BodyHandler;
import people.config.ConfigReader;
import people.model.Person;
import people.storage.InMemoryPeopleStorage;
import people.storage.PeopleStorage;
import people.storage.StorageException;

public class PeopleVerticle extends AbstractVerticle {

	public static final String PATH_PARAM_PERSON_ID = "personId";
	public static final String LOCATION_PERSONS = "persons";

	private final PeopleStorage storage = new InMemoryPeopleStorage();

	private Logger logger = LoggerFactory.getLogger(PeopleVerticle.class);
	
	@Override
	public void start(Promise<Void> startPromise) {

		Router router = initRouter();

		logger.info("Creating HTTP server");
		
		HttpServer server = vertx.createHttpServer();

		server.requestHandler(router).listen(ConfigReader.getPort(), result -> {
			
			if (result.succeeded()) {
				startPromise.complete();
			} else {
				startPromise.fail(result.cause());
			}
		});

	}

	private Router initRouter() {
		
		Router router = Router.router(vertx);
		router.get("/" + LOCATION_PERSONS).handler(this::getPersons);
		router.post("/" + LOCATION_PERSONS).handler(BodyHandler.create()).handler(this::savePerson);
		router.get("/" + LOCATION_PERSONS + "/:" + PATH_PARAM_PERSON_ID)
				.handler(HTTPRequestValidationHandler.create().addPathParam(PATH_PARAM_PERSON_ID, ParameterType.INT))
				.handler(this::getPerson);

		router.errorHandler(500, routingContext -> {
			
			logger.error(routingContext.failure().getMessage());
			
			routingContext.response().setStatusCode(routingContext.failure() instanceof StorageException ? 400 : 500)
					.end(routingContext.failure().toString());
		});
		
		return router;
	}

	private void getPerson(RoutingContext routingContext) {
		
		RequestParameters params = routingContext.get("parsedParameters");
		
		Integer personId = params.pathParameter(PATH_PARAM_PERSON_ID).getInteger();
		
		logger.info("Processing request to get person: " + personId);

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(storage.getPerson(personId)));
	}

	private void savePerson(RoutingContext routingContext) {

		Person newPerson = routingContext.getBodyAsJson().mapTo(Person.class);
		
		logger.info("Processing request to save person: " + newPerson);
		
		storage.save(newPerson);
		
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(newPerson));
	}

	private void getPersons(RoutingContext routingContext) {

		logger.info("Processing request to get all persons");
		
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(storage.getAll()));
	}

}
