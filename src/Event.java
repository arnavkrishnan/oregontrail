import java.util.List;
import java.util.Random;

public class Event {

    private static EventType findWildFruit() {
        return EventType.WILD_FRUIT;
    }

    private static EventType wagonBroken() {
        return EventType.WAGON_BROKEN;
    }

    private static EventType wagonFire() {
        return EventType.WAGON_FIRE;
    }

    private static EventType wagonRobbed() {
        return EventType.WAGON_ROBBED;
    }

    private static EventType oxenInjuredOrDead() {
        return EventType.OXEN_INJURED_OR_DEAD;
    }

    private static EventType personHasDisease(String name) {
        return EventType.PERSON_HAS_DISEASE;
    }

    private static EventType personHasBroken(String name) {
        return EventType.PERSON_HAS_BROKEN;
    }

    private static EventType personHasDied(String name) {
        return EventType.PERSON_HAS_DIED;
    }

    private static EventType findAbandonedWagon() {
        return EventType.FIND_ABANDONED_WAGON;
    }

    public static EventType getRandomEvent(Player player, List<Companion> companions, List<Oxen> oxen, WeatherType weather, String date,
                                            int foodRations, int pace, int grassAmount, int waterAmount, int waterQuality) {
        Random rand = new Random();
        
        if (grassAmount > 0 && waterAmount > 0 && rand.nextInt(100) < 20) {
            return findWildFruit();
        }

        if (rand.nextInt(100) < 15) {
            return wagonBroken();
        }

        if (weather == WeatherType.HOT && rand.nextInt(100) < 10) {
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

        if ((foodRations > 2 || waterQuality < 40) && rand.nextInt(100) < 20) {
            if (rand.nextBoolean()) {
                for (Companion c : companions) {
                    if (rand.nextInt(100) < Math.min(20 + c.getMorale() / 5, 60)) {
                        return personHasDisease(c.getName());
                    }
                }
            }
            if (rand.nextInt(100) < Math.min(20 + player.getMorale() / 5, 60)) {
                return personHasDisease(player.getName());
            }
        }

        if (rand.nextInt(100) < Math.min(20 + pace * 2, 60)) {
            if (rand.nextBoolean()) {
                for (Companion c : companions) {
                    if (rand.nextInt(100) < Math.min(20 + c.getHygiene() / 5, 60)) {
                        return personHasBroken(c.getName());
                    }
                }
            }
            if (rand.nextInt(100) < Math.min(20 + player.getHygiene() / 5, 60)) {
                return personHasBroken(player.getName());
            }
        }

        if (rand.nextInt(100) < 5) {
            if (rand.nextBoolean()) {
                for (Companion c : companions) {
                    if (c.getHealth() < 1 && rand.nextInt(100) < 30) {
                        return personHasDied(c.getName());
                    }
                }
            }
            if (player.getHealth() < 1 && rand.nextInt(100) < 30) {
                return personHasDied(player.getName());
            }
        }

        if (rand.nextInt(100) < 10) {
            return findAbandonedWagon();
        }

        return EventType.NOTHING_HAPPENS; 
    }
}
