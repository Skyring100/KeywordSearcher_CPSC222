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
        return webpage.select("div.Gif").first().select("img").attr("src");
    }
}
