package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * An implementation to make Dictionary.com to work with the webscrapers
 */
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
        Elements definitions = getSearchResultElements(webpage).select("ol[data-type=definition-content-list]");
        int listCount = 0;
        //loop through each definition, having a list number per definition
        for(Element e : definitions){
            //get the header grammar type, such as whether it is a noun, verb etc.
            String grammarType = e.select("span.luna-pos").text();
            data.append(grammarType+"\n");
            //for every definition under this grammar type, list the definitions
            for(Element listItem : e.select("li")){
                data.append(++listCount +". "+listItem.text()+"\n");
            }
        }
        return data.toString();
    }
}
