package upgrade.karavel.services.reviewBoardSvnIntegrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.SvnProperties;
import upgrade.karavel.services.reviewBoardSvnIntegrator.core.IntegrationProcessor;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.ExceptionService;

@SpringBootApplication
@EnableConfigurationProperties({SvnProperties.class})
@PropertySource({"classpath:core.properties"})
@Log4j2
@RequiredArgsConstructor
public class ReviewBoardSvnIntegratorApplication implements  CommandLineRunner{

	private static final int MAX_RETRY = 3;

	@Value("${sleep-intervale-minutes}")
	private int sleepDurationIntervaleMinutes;

	public static boolean IS_FIRST_RUN = false;

	private final IntegrationProcessor processorLoop;
	private final ExceptionService exceptionService;

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
				exceptionService.insertException(e);
				e.printStackTrace();
				retryCount++;
			}
			IS_FIRST_RUN = false;
			log.debug("Sleep for {} minutes", sleepDurationIntervaleMinutes);
			Thread.sleep(sleepDurationIntervaleMinutes * 60L * 1000L);
		}
	}
}


