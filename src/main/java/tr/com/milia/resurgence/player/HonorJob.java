package tr.com.milia.resurgence.player;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class HonorJob extends QuartzJobBean {

	private final ApplicationEventPublisher eventPublisher;

	public HonorJob(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobDataMap data = context.getMergedJobDataMap();
		String playerName = data.getString("playerName");
		eventPublisher.publishEvent(new HonorPeriodCompletedEvent(playerName));
	}

}
