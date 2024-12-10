import java.util.*;

public class Terminal {

    public static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void clean() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void load(int totalCycles, int delayMillis) {
        Random rand = new Random();

        String[] frames = {"◜", "◝", "◞", "◟", "◠", "◡", "◯"};
        int totalFrames = frames.length;

        for (int i = 0; i < totalCycles; i++) {
            clean();
            int progress = (int) ((i + 1) * 100.0 / totalCycles);
            String frame = frames[i % totalFrames];
            System.out.println("Loading..." + frame);
            System.out.print("Progress: [");
            
            int blocks = progress / 2;
            for (int j = 0; j < 50; j++) {
                if (j < blocks) {
                    System.out.print("█");
                } else {
                    System.out.print(" ");
                }
            }

            System.out.print("] " + progress + "%\r");

            try {
                Thread.sleep(rand.nextInt(delayMillis));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        clean();
        System.out.println("Loading complete! \u2713");
    }

    public static void print(String text){
        try{
            for(int i =0; i<text.length(); i++){
                System.out.print(text.charAt(i));
                Thread.sleep(10);
            }
        } catch(InterruptedException e){
        }
    }

    public static void println(String text){
        try{
            for(int i =0; i<text.length(); i++){
                System.out.print(text.charAt(i));
                Thread.sleep(10);
            }
            System.out.println();
        } catch(InterruptedException e){
        }
    }
}
