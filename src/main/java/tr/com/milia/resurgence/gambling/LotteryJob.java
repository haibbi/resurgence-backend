package tr.com.milia.resurgence.gambling;

import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class LotteryJob extends QuartzJobBean {

	private final ApplicationEventPublisher eventPublisher;

	public LotteryJob(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) {
		eventPublisher.publishEvent(new LotteryPeriodCompletedEvent());
	}

}
