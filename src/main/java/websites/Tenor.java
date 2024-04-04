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
         return gifSection.select("img");
    }

    @Override
    public String getResultUrl(Element htmlElement) {
        return htmlElement.select("img").first().attr("src");
    }

    @Override
    public String findUsefulData(Document webpage) {
        return webpage.select("img").first().attr("src");
    }
}
