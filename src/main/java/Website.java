public enum Website {
    WIKIPEDIA("https://en.wikipedia.org/wiki/Special:Search?go=Go&search=", "&ns0=1","div.searchresults");
    private String url1;
    private String url2;
    private String searchResultSection;
    Website(String url1, String url2, String searchResultSection){
        this.url1 = url1;
        this.url2 = url2;
        this.searchResultSection = searchResultSection;
    }
    public String getSearchUrl(String keyword){
        return url1 + keyword + url2;
    }
    public String getSearchResultSection(){
        return searchResultSection;
    }
}
