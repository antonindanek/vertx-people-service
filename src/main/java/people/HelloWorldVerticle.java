package people;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class HelloWorldVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> future) throws Exception {

		vertx.createHttpServer().requestHandler(r -> {
			r.response().end("Hello world");
		}).listen(8080, result -> {
			if (result.succeeded()) {
				future.complete();
			} else {
				future.fail(result.cause());
			}
		});
	}
}
