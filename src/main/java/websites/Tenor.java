package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * An implementation to make Tenor Gifs to work with the webscrapers
 */
public class Tenor extends Website{
    public Tenor() {
        super("Tenor", "https://tenor.com/search/", "-gifs", ResultTypes.IMAGE);
    }

    @Override
    public Elements getSearchResultElements(Document resultPage) {
         Element gifSection = resultPage.select("div.UniversalGifList").first();
         return gifSection.select("a");
    }

    @Override
    public String getResultUrl(Element htmlElement) {
        return "https://tenor.com"+htmlElement.attr("href");
    }

    @Override
    public String findUsefulData(Document webpage) {
        Element gifSection = webpage.select("div.Gif").first();
        if(gifSection == null){
            System.out.println("No gif section was found on page "+webpage.location());
            return "No result";
        }
        return gifSection.select("img").attr("src");
    }
}
