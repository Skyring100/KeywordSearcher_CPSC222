import websites.*;

import java.util.Scanner;

/**
 * The driver program for the web-scraping to begin
 * Please note for this project to work, it requires a library called Jsoup, which I included in pom.xml as per Maven dependencies
 */
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
        String rawLine = reader.nextLine().strip();
        //remove all potentially problematic characters for file saving
        char[] badSymbols = {'/','\\','*','?',':','>','<','|','\"'};
        for(char sym : badSymbols){
            rawLine = rawLine.replace(sym,' ');
        }
        return rawLine;
    }
    private static int getResultCount(Scanner reader){
        System.out.print("How many results would you like per website? ");
        return reader.nextInt();
    }
}
