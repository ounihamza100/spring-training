package rewards.internal;

import common.money.MonetaryAmount;
import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.internal.account.Account;
import rewards.internal.account.AccountRepository;
import rewards.internal.restaurant.Restaurant;
import rewards.internal.restaurant.RestaurantRepository;
import rewards.internal.reward.RewardRepository;

/**
 * Rewards an Account for Dining at a Restaurant.
 *
 * The sole Reward Network implementation. This object is an application-layer service responsible for coordinating with
 * the domain-layer to carry out the process of rewarding benefits to accounts for dining.
 *
 * Said in other words, this class implements the "reward account for dining" use case.
 * @author Hamza Ouni
 */
public class RewardNetworkImpl implements RewardNetwork {
    private AccountRepository accountRepository;

    private RestaurantRepository restaurantRepository;

    private RewardRepository rewardRepository;

    /**
     * Creates a new reward network.
     * @param accountRepository the repository for loading accounts to reward
     * @param restaurantRepository the repository for loading restaurants that determine how much to reward
     * @param rewardRepository the repository for recording a record of successful reward transactions
     */
    public RewardNetworkImpl(AccountRepository accountRepository, RestaurantRepository restaurantRepository,
                             RewardRepository rewardRepository) {
        this.accountRepository = accountRepository;
        this.restaurantRepository = restaurantRepository;
        this.rewardRepository = rewardRepository;
    }

    public RewardConfirmation rewardAccountFor(Dining dining) {
        // TODO-01: Reward an account per the sequence diagram
        Account account = this.accountRepository.findByCreditCard(dining.getCreditCardNumber()) ;
        Restaurant restaurant = this.restaurantRepository.findByMerchantNumber(dining.getMerchantNumber());
        //Get the benefit
        MonetaryAmount monetaryAmount = restaurant.calculateBenefitFor(account,dining);
        //Make contribution
        AccountContribution accountContribution = account.makeContribution(monetaryAmount);
        accountRepository.updateBeneficiaries(account);
        RewardConfirmation rewardConfirmation = rewardRepository.confirmReward(accountContribution,dining);

        // TODO-02: Return the corresponding reward confirmation
        return rewardConfirmation;
    }
}
