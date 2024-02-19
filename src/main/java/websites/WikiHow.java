package websites;

import org.jsoup.nodes.Element;

public class WikiHow extends Website{
    public WikiHow(){
        super("https://www.wikihow.com/wikiHowTo?search=", "&Search=", "a.result_link");
    }


    @Override
    public String getResultUrl(Element htmlElement) {
        return htmlElement.attr("href");
    }

    @Override
    public String findResultData(String htmlPage) {
        return null;
    }

}
