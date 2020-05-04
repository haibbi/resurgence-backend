package tr.com.milia.resurgence.murder;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class AgencyJob extends QuartzJobBean {

	private final ApplicationEventPublisher publisher;

	public AgencyJob(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobDataMap data = context.getMergedJobDataMap();
		String seeker = data.getString("seeker");
		String wanted = data.getString("wanted");
		int agent = data.getInt("agent");

		publisher.publishEvent(new AgencyJobCompletedEvent(seeker, wanted, agent));
	}

}
