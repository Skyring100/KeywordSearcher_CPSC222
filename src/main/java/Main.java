import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String keyword = getKeyword();
        WebScraper scraper = new WebScraper(Website.GOOGLE_IMAGES.getSearchUrl(keyword), Website.GOOGLE_IMAGES.getSearchResultSection());
        scraper.start();
    }
    private static String getKeyword(){
        Scanner inputReader = new Scanner(System.in);
        System.out.print("Enter a word to get data on: ");
        return inputReader.nextLine();
    }
}
