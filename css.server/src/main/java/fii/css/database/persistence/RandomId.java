package fii.css.database.persistence;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Base64;
import java.util.Random;

public class RandomId {
    private static final Random RANDOM = new Random();

    public static String newId() {
        var epoch = Instant.now().toEpochMilli();
        var seed = RANDOM.nextLong(0, Integer.MAX_VALUE);

        return BigInteger.valueOf(epoch).toString(32)
                + "-"
                + BigInteger.valueOf(seed).toString(32);
    }
}
