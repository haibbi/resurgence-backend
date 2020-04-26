package tr.com.milia.resurgence.bank;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
class InterestJob extends QuartzJobBean {

	private final ApplicationEventPublisher eventPublisher;

	public InterestJob(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobDataMap data = context.getMergedJobDataMap();
		String playerName = data.getString("playerName");
		long amount = data.getLong("amount");
		eventPublisher.publishEvent(new InterestCompletedEvent(playerName, amount));
	}

}
