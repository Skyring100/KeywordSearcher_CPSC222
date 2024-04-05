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
        return resultPage.select("main[data-type=page]");
    }

    @Override
    public String getResultUrl(Element htmlElement) {
        return htmlElement.ownerDocument().location();
    }

    @Override
    public String findUsefulData(Document webpage) {
        StringBuilder data = new StringBuilder().append(webpage.title()+"\n");
        //get all the definitions
        Elements definitions = getSearchResultElements(webpage).select("div[data-type=word-definitions]");
        int listCount = 0;
        for(Element e : definitions){
            String grammarType = e.select("span.luna-pos").text();
            data.append(grammarType+"\n");
            for(Element listItem : e.select("li")){
                data.append(++listCount +". "+listItem.text()+"\n");
            }
        }
        return data.toString();
    }
}
