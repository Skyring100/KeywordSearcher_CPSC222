package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Tenor extends Website{
    public Tenor() {
        super("Tenor", "https://tenor.com/search/", "-gifs", "div.Gif", ResultTypes.IMAGE);
    }

    @Override
    public Elements getSearchResultElements(Document resultPage) {
         Element gifSection = resultPage.select("div.GifList").first();
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
            return "DIV FAILURE";
        }
        return gifSection.select("img").attr("src");
    }
}
