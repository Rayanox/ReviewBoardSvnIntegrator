package upgrade.karavel.services.reviewBoardSvnIntegrator;

import lombok.Builder;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.SvnProperties;
import upgrade.karavel.services.reviewBoardSvnIntegrator.core.IntegrationProcessor;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.DatabaseService;

@SpringBootApplication
@EnableConfigurationProperties({SvnProperties.class})
@Builder
@Log
public class ReviewBoardSvnIntegratorApplication implements  CommandLineRunner{

	private static final int MAX_RETRY = 3;
	private static final int SLEEP_DURATION_SECONDS = 60;

	public static boolean IS_FIRST_RUN = false;

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
				e.printStackTrace();
				retryCount++;
			}
			IS_FIRST_RUN = false;
			log.fine(String.format("Sleep for %s seconds", SLEEP_DURATION_SECONDS));
			Thread.sleep(SLEEP_DURATION_SECONDS * 1000L);
		}
	}
}
