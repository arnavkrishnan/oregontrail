import java.util.List;
import java.util.Random;

public class Event {

    public static String findWildFruit() {
        return "You find some wild fruit.";
    }

    public static String personHasBroken(String name) {
        String limb = new Random().nextBoolean() ? "arm" : "leg";
        return name + " has a broken " + limb + ".";
    }

    public static String personHasDisease(String name) {
        String[] diseases = {"cholera", "measles", "dysentery", "typhoid fever", "influenza"};
        String disease = diseases[new Random().nextInt(diseases.length)];
        return name + " has " + disease + ".";
    }

    public static String personHasDied(String name) {
        return name + " has died.";
    }

    public static String wagonBroken() {
        String[] parts = {"wheel", "tongue", "axel"};
        String part = parts[new Random().nextInt(parts.length)];
        return "The wagon " + part + " is broken.";
    }

    public static String oxenInjuredOrDead() {
        String[] states = {"injured", "dead"};
        String state = states[new Random().nextInt(states.length)];
        return "The oxen are " + state + ".";
    }

    public static String wagonFire() {
        return "The wagon lights on fire.";
    }

    public static String wagonRobbed() {
        return "The wagon gets robbed.";
    }

    public static String findAbandonedWagon() {
        return "You find an abandoned wagon.";
    }

    public static String getRandomEvent(List<Character> characters, String weather, int foodRations, int pace, String topography, int grassAmount, int waterAmount, String waterQuality) {
        Random rand = new Random();

        if (grassAmount > 0 && waterAmount > 0 && rand.nextInt(100) < 20) {
            return findWildFruit();
        }

        if (rand.nextInt(100) < 15) {
            return wagonBroken();
        }

        if ((weather.equals("arid") || weather.equals("hot")) && rand.nextInt(100) < 10) {
            return wagonFire();
        }

        if (rand.nextInt(100) < 10) {
            return wagonRobbed();
        }

        if (rand.nextInt(100) < Math.min(20 + pace * 2, 80)) {
            return oxenInjuredOrDead();
        }

        if (waterAmount < 2 && grassAmount < 2 && rand.nextInt(100) < 5) {
            return oxenInjuredOrDead();
        }

        for (Character c : characters) {
            if ((foodRations < 3 || waterQuality.equals("bad")) && rand.nextInt(100) < 20) {
                return personHasDisease(c.getName());
            }
        }

        for (Character c : characters) {
            if (rand.nextInt(100) < Math.min(20 + pace * 2, 60)) {
                return personHasBroken(c.getName());
            }
        }

        for (Character c : characters) {
            if (rand.nextInt(100) < 5 && c.getHealth() < 20) {
                return personHasDied(c.getName());
            }
        }

        if (rand.nextInt(100) < 10) {
            return findAbandonedWagon();
        }

        return "Nothing significant happens.";
    }
}
