package rewards.internal.restaurant;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Loads restaurants from a data source using JPA.
 * @author Hamza Ouni
 */
public class JpaRestaurantRepository implements RestaurantRepository {

    public static final String INFO = "JPA";

    private static final Logger logger = Logger.getLogger("config");

    private EntityManager entityManager;

    public JpaRestaurantRepository() {
        logger.info("Created JpaRestaurantRepository");
    }

    /**
     * Creates a new JPA account manager.
     *
     * @param entityManager
     *            the JPA entity manager
     */
    public JpaRestaurantRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        logger.info("Created JpaRestaurantRepository");
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public String getInfo() {
        return INFO;
    }

    public Restaurant findByMerchantNumber(String merchantNumber) {
        return (Restaurant) entityManager.createQuery("from Restaurant r where r.number = :merchantNumber")
                .setParameter("merchantNumber", merchantNumber).getSingleResult();
    }

}
