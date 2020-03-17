package people;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import people.config.ConfigReader;

public class HelloWorldVerticle extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startPromise) {

		vertx.createHttpServer().requestHandler(r -> {
			r.response().end("Hello world");
		}).listen(ConfigReader.getPort(), result -> {

			if (result.succeeded()) {
				startPromise.complete();
			} else {
				startPromise.fail(result.cause());
			}
		});

	}

}
