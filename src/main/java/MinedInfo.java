import websites.Website;

public class MinedInfo {
    private final String websiteName;
    private final String url;
    private final String mainContent;
    private final Website.ResultTypes dataType;
    private static final MinedInfo NO_DATA_VALUE = new MinedInfo("","","", Website.ResultTypes.TEXT);
    public MinedInfo(String websiteName, String url, String mainContent, Website.ResultTypes dataType){
        this.websiteName = websiteName;
        this.url = url;
        this.mainContent = mainContent;
        this.dataType = dataType;
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

    public Website.ResultTypes getDataType() {
        return dataType;
    }
    public static MinedInfo getNoDataValue(){
        return NO_DATA_VALUE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MinedInfo o){
            return websiteName.equals(o.websiteName) && url.equals(o.url) && mainContent.equals(o.mainContent);
        }
        return false;
    }
}
