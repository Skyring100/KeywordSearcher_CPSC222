package websites;

public enum WebsiteEnum {
    WIKIHOW("https://www.wikihow.com/wikiHowTo?search=", "&Search=", "a.result_link"),
    REDDIT("https://www.reddit.com/search/?q=","","a.absolute.inset-0");
    private String url1;
    private String url2;
    private String searchResultElement;
    WebsiteEnum(String url1, String url2, String searchResultSection){
        this.url1 = url1;
        this.url2 = url2;
        this.searchResultElement = searchResultSection;
    }
    public String getSearchUrl(String keyword){
        return url1 + keyword + url2;
    }
    public String getSearchResultElement(){
        return searchResultElement;
    }
}
