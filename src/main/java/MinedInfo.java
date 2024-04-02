public class MinedInfo {
    private final String websiteName;
    private final String url;
    private final String mainContent;
    public MinedInfo(String websiteName, String url, String mainContent){
        this.websiteName = websiteName;
        this.url = url;
        this.mainContent = mainContent;
    }

    public String getWebsiteName() {
        return websiteName;
    }
    public String getUrl() {
        return url;
    }
    public String getMainContent() {
        return mainContent;
    }
}
