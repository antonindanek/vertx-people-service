package people;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ParameterType;
import people.config.ConfigReader;
import people.storage.InMemoryPeopleStorage;
import people.storage.PeopleStorage;
import people.storage.StorageException;

public class PeopleVerticle extends AbstractVerticle {

	private static final String PATH_PARAM_PERSON_ID = "personId";

	private static final PeopleStorage storage = new InMemoryPeopleStorage();

	@Override
	public void start(Promise<Void> startPromise) {

		Router router = Router.router(vertx);
		router.get("/persons").handler(this::getPersons);
		router.post("/persons").handler(this::savePerson);
		router.get("/persons/:" + PATH_PARAM_PERSON_ID)
				.handler(HTTPRequestValidationHandler.create().addPathParam(PATH_PARAM_PERSON_ID, ParameterType.INT))
				.handler(this::getPerson);

		router.errorHandler(500, routingContext -> {
			routingContext.response().setStatusCode(routingContext.failure() instanceof StorageException ? 400 : 500)
					.end(routingContext.failure().getMessage());
		});

		HttpServer server = vertx.createHttpServer();

		server.requestHandler(router).listen(ConfigReader.getPort(), result -> {

			if (result.succeeded()) {
				startPromise.complete();
			} else {
				startPromise.fail(result.cause());
			}
		});

	}

	private void getPerson(RoutingContext routingContext) {

		RequestParameters params = routingContext.get("parsedParameters");
		Integer personId = params.pathParameter(PATH_PARAM_PERSON_ID).getInteger();

		routingContext.response().end(Json.encodePrettily(storage.getPerson(personId)));
	}

	private void savePerson(RoutingContext routingContext) {

		routingContext.getBodyAsJson();
	}

	private void getPersons(RoutingContext routingContext) {

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(storage.getAll()));
	}

}
