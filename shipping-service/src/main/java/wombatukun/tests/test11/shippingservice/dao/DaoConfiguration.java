package wombatukun.tests.test11.shippingservice.dao;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "wombatukun.tests.test11.shippingservice.dao.repositories")
@EntityScan(basePackages = "wombatukun.tests.test11.shippingservice.dao.entities")
@EnableTransactionManagement
public class DaoConfiguration {

}
