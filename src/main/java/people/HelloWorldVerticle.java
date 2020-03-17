package people;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class HelloWorldVerticle extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startPromise) {

		vertx.createHttpServer().requestHandler(r -> {
			r.response().end("Hello world");
		}).listen(8080, result -> {

			if (result.succeeded()) {
				startPromise.complete();
			} else {
				startPromise.fail(result.cause());
			}
		});

	}

}
