import java.io.*;

public class Terminal {

    // most of the subroutines in here are just catching errors, etc, just makes my code in the game class a bit cleaner which is nice if you see how long the Game class is

    public static void getln(){
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
