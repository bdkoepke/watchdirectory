package pw.swordfish.main;

import com.google.common.collect.ImmutableSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import pw.swordfish.lang.ExceptionHandler;
import pw.swordfish.io.WatchDirectory;
import pw.swordfish.prefs.Configuration;
import pw.swordfish.prefs.Source;

public class App implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(App.class.getName());
	private static final int EXIT_STATUS = 1;
	private static ExceptionHandler<Exception> APP_EXCEPTION_HANDLER = new ExceptionHandler<Exception>() {
		private final Level _level = Level.SEVERE;
		@Override
		public void handle(Exception exception) {
			LOGGER.log(_level, exception.getMessage(), exception);
			System.exit(EXIT_STATUS);
		}
	};

	private void _run() throws JAXBException, IOException {
		XmlContext<Configuration> context = XmlContext.<Configuration>builder()
				.setMarshallerProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
				.build(Configuration.class, PrintlnWatchEventObserver.class);

		Configuration configuration = context.unmarshall(new FileInputStream("configuration.xml"));

		WatchEventObserver watchEventObserver = configuration.getWatchEventObserver();

		Set<Source> sources = configuration.getSources().get();

		ImmutableSet.Builder<Thread> threads = ImmutableSet.builder();
		for (Source source : sources) {
			final WatchDirectory watchDirectory = WatchDirectory.of(source.isRecursive());
			watchDirectory.register(source.getDirectory().toPath());
			watchDirectory.subscribe(watchEventObserver);
			threads.add(new Thread() {
				@Override
				public void run() {
					watchDirectory.run();
				}
			});
		}

		for (Thread thread : threads.build()) {
			thread.start();
		}
	}

	public void run(ExceptionHandler<Exception> handler) {
		try {
			_run();
		} catch (JAXBException | IOException ex) {
			handler.handle(ex);
		}
	}

	@Override
	public void run() {
		run(APP_EXCEPTION_HANDLER);
	}

	public static void main(String... args) throws IOException, JAXBException {
		App app = new App();
		app.run();
	}
}