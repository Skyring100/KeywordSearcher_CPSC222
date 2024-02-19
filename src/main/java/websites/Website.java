package websites;

import org.jsoup.nodes.Element;

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
    public String getSearchResultElement(){
        return searchResultElement;
    }
    public abstract String getResultUrl(Element htmlElement);
    public abstract String findResultData(String htmlPage);
}
