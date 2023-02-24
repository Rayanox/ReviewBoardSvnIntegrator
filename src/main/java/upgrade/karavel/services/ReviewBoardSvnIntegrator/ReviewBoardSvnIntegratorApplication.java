package upgrade.karavel.services.ReviewBoardSvnIntegrator;

import lombok.Builder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.core.IntegrationProcessor;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.DatabaseService;

@SpringBootApplication
@Builder
public class ReviewBoardSvnIntegratorApplication implements  CommandLineRunner{

	private static final int MAX_RETRY = 3;
	private static final int SLEEP_DURATION_SECONDS = 60;

	private IntegrationProcessor processorLoop;
	private DatabaseService databaseService;

	public static void main(String[] args) {
		SpringApplication.run(ReviewBoardSvnIntegratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		int retryCount = 0;
		while (retryCount < MAX_RETRY) {
			try {
				processorLoop.process();
				retryCount = 0;
			} catch (Exception e) {
				databaseService.insertException(e);
				retryCount++;
			}
			Thread.sleep(SLEEP_DURATION_SECONDS);
		}
	}
}
