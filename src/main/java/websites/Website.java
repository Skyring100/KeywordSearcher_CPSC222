package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class Website {
    private String url1;
    private String url2;
    private String searchResultElement;
    public Website(String url1, String url2, String searchResultElement){
        this.url1 = url1;
        this.url2 = url2;
        this.searchResultElement = searchResultElement;
    }
    public String getSearchUrl(String keyword){
        return url1 + keyword + url2;
    }
    protected String getSearchResultElement(){
        return searchResultElement;
    }
    public abstract Elements getSearchResultsFromPage(Document resultPage);
    public abstract String getSearchResultUrl(Element htmlElement);
    public abstract String findUsefulData(Document webpage);
    public String getTextOnly(String html){
        return html.replaceAll("<.*?>","");
    }
}
