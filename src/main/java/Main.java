import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String keyword = getKeyword();
        WebScraper wikipediaScraper = new WebScraper(Website.WIKIPEDIA.getSearchUrl(keyword), Website.WIKIPEDIA.getSearchResultSection());
        wikipediaScraper.start();
    }
    private static String getKeyword(){
        Scanner inputReader = new Scanner(System.in);
        System.out.print("Enter a word to get data on: ");
        return inputReader.nextLine();
    }
}
