package tr.com.milia.resurgence.monitoring;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jpa.HibernateMetrics;
import io.micrometer.core.instrument.binder.jpa.HibernateQueryMetrics;
import org.hibernate.SessionFactory;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.metrics.jdbc.DataSourcePoolMetrics;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;


@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({MetricsAutoConfiguration.class, CompositeMeterRegistryAutoConfiguration.class})
@ConditionalOnClass(MeterRegistry.class)
@ConditionalOnBean(MeterRegistry.class)
public class HibernateMetricsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public HibernateMetrics hibernateMetrics(EntityManagerFactory entityManagerFactory) {
		SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		return new HibernateMetrics(sessionFactory, "sessionFactory", null);
	}

	@Bean
	@ConditionalOnMissingBean
	public HibernateQueryMetrics hibernateQueryMetrics(EntityManagerFactory entityManagerFactory) {
		SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		return new HibernateQueryMetrics(sessionFactory, "sessionFactory", null);
	}

	@Bean
	@ConditionalOnMissingBean
	public DataSourcePoolMetrics dataSourcePoolMetrics(HikariDataSource dataSource,
													   DataSourcePoolMetadataProvider dataSourcePoolMetadataProvider) {
		return new DataSourcePoolMetrics(dataSource,
			dataSourcePoolMetadataProvider,
			dataSource.getPoolName(),
			null);
	}

}
