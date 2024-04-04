package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Pixabay extends Website{
    public Pixabay() {
        super("Pixabay", "https://pixabay.com/images/search/", "/", ResultTypes.IMAGE);
    }

    @Override
    public Elements getSearchResultElements(Document resultPage) {
        return null;
    }

    @Override
    public String getResultUrl(Element htmlElement) {
        return null;
    }

    @Override
    public String findUsefulData(Document webpage) {
        return null;
    }
}
