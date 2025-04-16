package fii.css.database.persistence;

import java.time.Instant;

public class RandomId {
    // FIXME: Collisions are VERY unlikely, but a better algorithm is most welcome!
    // Or change all IDs to String to use GUIDs
    public static int newId() {
        int seed = (int) (Math.random() * 1000);

        return Math.abs((int) Instant.now().toEpochMilli() * seed);
    }
}
