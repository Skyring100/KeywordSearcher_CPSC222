import websites.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String keyword = getKeyword();
        ScrapeOrganizer organizer = new ScrapeOrganizer(keyword, new Website[]{new YouTube()}, 15);
        //NEXT SITES: youtube, https://www.pexels.com
        organizer.start();
    }
    private static String getKeyword(){
        Scanner inputReader = new Scanner(System.in);
        System.out.print("Enter a word to get data on: ");
        return inputReader.nextLine();
    }
}
