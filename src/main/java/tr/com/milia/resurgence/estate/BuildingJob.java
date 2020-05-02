package tr.com.milia.resurgence.estate;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class BuildingJob extends QuartzJobBean {

	private final ApplicationEventPublisher publisher;

	public BuildingJob(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobDataMap data = context.getMergedJobDataMap();
		String playerName = data.getString("playerName");
		String buildingName = data.getString("buildingName");
		Building building = Building.valueOf(buildingName);

		publisher.publishEvent(new BuildingPeriodCompletedEvent(playerName, building));
	}

}
