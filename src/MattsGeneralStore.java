import java.util.*;

public class MattsGeneralStore {
    public static int buyOxen(Player player){
        int yoke = -1;
        Terminal.println("There are 2 oxen in a yoke; I reccommend at least 3 yoke. I charge $40 a yoke. ");
        Terminal.println("How many yoke do you want?");
        do {
            yoke = TextIO.getlnInt();
            if (yoke < 0){
                Terminal.println("I can't sell you negative oxen.");
                yoke = -1;
            } else if (yoke > 10){
                Terminal.println("Each wagon can't have more than 20 oxen.");
                yoke = -1;
            } else if (40*yoke > player.getMoney()){
                Terminal.println("You can't afford that.");
                yoke = -1;
            }
        } while(yoke == -1);

        return yoke;
    }

    public static int buyFood(Player player){
        int food = -1;
        Terminal.println("I recommend you take at least 200 pounds of food for each person in your family. I see that you have 5 people in all. You'll need flour, sugar, bacon, and coffee. My price is 20 cents a pound.");
        Terminal.println("How many pounds of food do you want?");
        do {
            food = TextIO.getlnInt();
            if (food < 0){
                Terminal.println("I can't sell you negative food.");
                food = -1;
            } else if (food > 2000){
                Terminal.println("Your wagon may only carry 2000 pounds of food.");
                food = -1;
            } else if (0.20*food > player.getMoney()){
                Terminal.println("You can't afford that.");
                food = -1;
            }
        } while(food == -1);

        return food;
    }

    public static int buyClothes(Player player){
        int sets = -1;
        Terminal.println("You'll need warm clothing in the mountains. I recommend taking at least 2 sets of clothes per person. Each set is $10.00.");
        Terminal.println("How many sets of clothes do you want?");
        do {
            sets = TextIO.getlnInt();
            if (sets < 0){
                Terminal.println("I can't sell you negative sets of clothes.");
                sets = -1;
            } else if (sets > 99){
                Terminal.println("Your wagon may only carry 99 sets of clothes.");
                sets = -1;
            } else if (10*sets > player.getMoney()){
                Terminal.println("You can't afford that.");
                sets = -1;
            }
        } while(sets == -1);

        return sets;
    }

    public static int buyAmmo(Player player){
        int boxes = -1;
        Terminal.println("I sell ammunition in boxes of 20 bullets. Each box costs $2.00.");
        Terminal.println("How many boxes do you want?");
        do {
            boxes = TextIO.getlnInt();
            if (boxes < 0){
                Terminal.println("I can't sell you negative boxes.");
                boxes = -1;
            } else if (boxes > 99){
                Terminal.println("Your wagon may only carry 99 boxes.");
                boxes = -1;
            } else if (2*boxes > player.getMoney()){
                Terminal.println("You can't afford that.");
                boxes = -1;
            }
        } while(boxes == -1);

        return boxes;
    }

    public static int[] buyParts(Player player){
        int[] part = new int[]{-1, -1, -1};
        String[] parts = new String[]{"wheels", "axles", "tongues"};
        Terminal.println("It's a good idea to have a few spare parts for your wagon. Each part is $10.");
        for(int i  = 0; i<3; i++){
            Terminal.println("How many wagon " + parts[i] + " do you want?");
            do {
                part[i] = TextIO.getlnInt();
                if (part[i] < 0){
                    Terminal.println("I can't sell you negative " + parts[i] + ".");
                    part[i] = -1;
                } else if (part[i] > 3){
                    Terminal.println("Your wagon may only carry 3 " + parts[i] + ".");
                    part[i] = -1;
                } else if (10*part[i] > player.getMoney()){
                    Terminal.println("You can't afford that.");
                    part[i] = -1;
                }
            } while(part[i] == -1);
        }
        return part;
    }
}
