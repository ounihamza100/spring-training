package config;

import accounts.AccountManager;
import accounts.internal.JpaAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewards.internal.account.AccountRepository;
import rewards.internal.account.JpaAccountRepository;
import rewards.internal.restaurant.JpaRestaurantRepository;
import rewards.internal.restaurant.RestaurantRepository;
import rewards.internal.reward.JdbcRewardRepository;
import rewards.internal.reward.RewardRepository;

import javax.sql.DataSource;


/**
 * Application configuration - services and repositories.
 * <p>
 * Because this is used by many similar lab projects with slightly different
 * classes and packages, everything is explicitly created using @Bean methods.
 * Component-scanning risks picking up unwanted beans in the same package in
 * other projects.
 *
 *  @author Hamza Ouni
 */
@Configuration
public class AppConfig {
    DataSource dataSource;

    @Autowired
    public AppConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public AccountManager accountManager() {
        return new JpaAccountManager();
    }

    @Bean
    public AccountRepository accountRepository() {
        return new JpaAccountRepository();
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return new JpaRestaurantRepository();
    }

    @Bean
    public RewardRepository rewardRepository() {
        return new JdbcRewardRepository(dataSource);
    }
}
