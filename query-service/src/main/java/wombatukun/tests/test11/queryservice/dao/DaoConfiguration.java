package wombatukun.tests.test11.queryservice.dao;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "wombatukun.tests.test11.queryservice.dao.repositories")
@EntityScan(basePackages = "wombatukun.tests.test11.queryservice.dao.entities")
@EnableTransactionManagement
public class DaoConfiguration {

}
