import java.util.List;
import java.util.Random;

public class Event {
    public static EventType getRandomEvent(Player player, List<Companion> companions, List<Oxen> oxen, WeatherType weather, String date,
                                            int foodRations, int pace, int grassAmount, int waterAmount, int waterQuality) {
        Random rand = new Random();
        
        if (grassAmount > 0 && waterAmount > 0 && rand.nextInt(100) < 20) {
            return EventType.WILD_FRUIT;
        }

        if (rand.nextInt(100) < 15) {
            return EventType.WAGON_BROKEN;
        }

        if (weather == WeatherType.HOT && rand.nextInt(100) < 10) {
            return EventType.WAGON_FIRE;
        }

        if (rand.nextInt(100) < 10) {
            return EventType.WAGON_ROBBED;
        }

        if (rand.nextInt(100) < Math.min(20 + pace * 2, 80)) {
            return EventType.OXEN_INJURED_OR_DEAD;
        }

        if (waterAmount < 2 && grassAmount < 2 && rand.nextInt(100) < 5) {
            return EventType.OXEN_INJURED_OR_DEAD;
        }

        if ((foodRations > 2 || waterQuality < 40) && rand.nextInt(100) < 20) {
            if (rand.nextBoolean()) {
                for (Companion c : companions) {
                    if (rand.nextInt(100) < Math.min(20 + c.getMorale() / 5, 60)) {
                        return EventType.PERSON_HAS_DISEASE;
                    }
                }
            }
            if (rand.nextInt(100) < Math.min(20 + player.getMorale() / 5, 60)) {
                return EventType.PERSON_HAS_DISEASE;
            }
        }

        if (rand.nextInt(100) < Math.min(20 + pace * 2, 60)) {
            if (rand.nextBoolean()) {
                for (Companion c : companions) {
                    if (rand.nextInt(100) < Math.min(20 + c.getHygiene() / 5, 60)) {
                        return EventType.PERSON_HAS_BROKEN;
                    }
                }
            }
            if (rand.nextInt(100) < Math.min(20 + player.getHygiene() / 5, 60)) {
                return EventType.PERSON_HAS_BROKEN;
            }
        }

        if (rand.nextInt(100) < 5) {
            if (rand.nextBoolean()) {
                for (Companion c : companions) {
                    if (c.getHealth() < 1 && rand.nextInt(100) < 30) {
                        return EventType.PERSON_HAS_DIED;
                    }
                }
            }
            if (player.getHealth() < 1 && rand.nextInt(100) < 30) {
                return EventType.PERSON_HAS_DIED;
            }
        }

        if (rand.nextInt(100) < 10) {
            return EventType.FIND_ABANDONED_WAGON;
        }

        return EventType.NOTHING_HAPPENS; 
    }
}
