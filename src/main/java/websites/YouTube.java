package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YouTube extends Website{
    public YouTube() {
        super("YouTube", "https://www.youtube.com/results?search_query=", "", "a#video-title", ResultTypes.VIDEO);
    }

    @Override
    public Elements getSearchResultElements(Document resultPage) {
        Elements mainSection = resultPage.select("div#content");
        return mainSection.next().next().select(getSearchResultElement());
    }

    @Override
    public String getResultUrl(Element htmlElement) {
        return htmlElement.attr("href");
    }

    @Override
    public String findUsefulData(Document webpage) {
        return webpage.location();
    }
}
