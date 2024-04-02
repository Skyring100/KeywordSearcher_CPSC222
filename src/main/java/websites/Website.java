package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class Website {
    private final String name;
    private String url1;
    private String url2;
    private String searchResultElement;
    public Website(String name,String url1, String url2, String searchResultElement){
        this.name = name;
        this.url1 = url1;
        this.url2 = url2;
        this.searchResultElement = searchResultElement;
    }
    public String getName() {
        return name;
    }
    public String getSearchUrl(String keyword){
        return url1 + keyword + url2;
    }
    protected String getSearchResultElement(){
        return searchResultElement;
    }
    public abstract Elements getSearchResultElements(Document resultPage);
    public abstract String getResultUrl(Element htmlElement);
    public abstract String findUsefulData(Document webpage);

    /**
     * Removes all HTML tags from given HTML code, leaving only the text directly on the website
     * @param html the HTML code in String format
     * @return the text without any HTML code
     */
    public String removeHTMLTags(String html){
        return html.replaceAll("<.*?>","");
    }
}
