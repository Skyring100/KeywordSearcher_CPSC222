import websites.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner inputReader = new Scanner(System.in);
        String keyword = getKeyword(inputReader);
        int results = getResultCount(inputReader);
        ScrapeOrganizer organizer = new ScrapeOrganizer(keyword, new Website[]{new Dictionary(), new Tenor(), new UNBC(), new WikiHow()}, results);
        organizer.start();
    }
    private static String getKeyword(Scanner reader){
        System.out.print("Enter a word to get data on: ");
        return reader.nextLine().strip();
    }
    private static int getResultCount(Scanner reader){
        System.out.println("How many results would you like per website? ");
        return reader.nextInt();
    }
}
