package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test extends Website{
    public Test() {
        super("Test", "https://www.bing.com/images/search?q=", "&go=Search&qs=ds&first=1");
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
