package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Dictionary extends Website{
    public Dictionary() {
        super("Dictionary", "https://www.dictionary.com/browse/", "");
    }

    @Override
    public Elements getSearchResultElements(Document resultPage) {
        //does not have a search results page, so when searching we immediately land where content is
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
