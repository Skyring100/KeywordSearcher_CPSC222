import websites.WebsiteEnum;
import websites.WikiHow;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String keyword = getKeyword();
        WebScraper wikiHowScraper = new WebScraper(new WikiHow(), keyword, 3);
        wikiHowScraper.start();
    }
    private static String getKeyword(){
        Scanner inputReader = new Scanner(System.in);
        System.out.print("Enter a word to get data on: ");
        return inputReader.nextLine();
    }
}
