package tr.com.milia.resurgence.cron;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class CronAutoConfiguration {

	@Bean
	@QuartzDataSource
	public DataSource cronDatasource(DataSourceProperties properties) {
		HikariDataSource dataSource = properties
			.initializeDataSourceBuilder()
			.type(HikariDataSource.class)
			.build();
		dataSource.setPoolName("QuartzPool");
		dataSource.setSchema("quartz");
		return dataSource;
	}

}
