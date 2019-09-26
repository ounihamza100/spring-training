package accounts.internal;

import accounts.AccountManager;
import common.money.Percentage;
import rewards.internal.account.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

/**
 * An account manager that uses JPA to find accounts.
 * @author Hamza Ouni
 */
public class JpaAccountManager implements AccountManager {
    public static final String INFO = "JPA";

    private static final Logger logger = Logger.getLogger("config");

    private EntityManager entityManager;

    public JpaAccountManager() {
        logger.info("Created JpaAccountManager");
    }

    /**
     * Creates a new JPA account manager.
     *
     * @param entityManager
     *            the JPA entity manager
     */
    public JpaAccountManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public String getInfo() {
        return INFO;
    }


    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Account> getAllAccounts() {
        List<Account> l = entityManager.createQuery("select a from Account a LEFT JOIN FETCH a.beneficiaries")
                .getResultList();

        // Use of "JOIN FETCH" produces duplicate accounts, and DISTINCT does
        // not address this. So we have to filter it manually.
        List<Account> result = new ArrayList<Account>();

        for (Account a : l) {
            if (!result.contains(a))
                result.add(a);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long id) {
        Account account = (Account) entityManager.find(Account.class, id);

        if (account == null)
            throw new ObjectRetrievalFailureException(Account.class, id);

        // Force beneficiaries to load too - avoid Hibernate lazy loading error
        account.getBeneficiaries().size();

        return account;
    }

    @Transactional
    public Account save(Account account) {
        entityManager.persist(account);
        return account;
    }


    @Transactional
    public void update(Account account) {
        entityManager.merge(account);
    }


    @Transactional
    public void updateBeneficiaryAllocationPercentages(Long accountId, Map<String, Percentage> allocationPercentages) {
        Account account = getAccount(accountId);
        for (Map.Entry<String, Percentage> entry : allocationPercentages.entrySet()) {
            account.getBeneficiary(entry.getKey()).setAllocationPercentage(entry.getValue());
        }
    }


    @Transactional
    public void addBeneficiary(Long accountId, String beneficiaryName) {
        getAccount(accountId).addBeneficiary(beneficiaryName, Percentage.zero());
    }

    @Transactional
    public void removeBeneficiary(Long accountId, String beneficiaryName,
                                  Map<String, Percentage> allocationPercentages) {
        getAccount(accountId).removeBeneficiary(beneficiaryName);

        if (allocationPercentages != null)
            updateBeneficiaryAllocationPercentages(accountId, allocationPercentages);
    }

}
