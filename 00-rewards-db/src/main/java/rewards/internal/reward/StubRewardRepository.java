package rewards.internal.reward;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;

import java.util.Random;

/**
 * A dummy reward repository implementation.
 *
 * IMPORTANT!!!
 * Per best practices, this class shouldn't be in 'src/main/java' but rather in 'src/test/java'.
 * However, it is used by numerous Test classes inside multiple projects. Maven does not
 * provide an easy way to access a class that is inside another project's 'src/test/java' folder.
 *
 * Rather than using some complex Maven configuration, we decided it is acceptable to place this test
 * class inside 'src/main/java'.
 * @author Hamza Ouni
 */
public class StubRewardRepository implements RewardRepository {

    public static final String TYPE = "Stub";

    public RewardConfirmation confirmReward(AccountContribution contribution, Dining dining) {
        return new RewardConfirmation(confirmationNumber(), contribution);
    }


    public String getInfo() {
        return TYPE;
    }

    private String confirmationNumber() {
        return new Random().toString();
    }
}