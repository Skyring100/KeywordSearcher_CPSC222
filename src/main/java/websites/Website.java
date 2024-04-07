package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A general structure to implement websites to work with the webscrapers
 */
public abstract class Website {
    private final String name;
    private final String url1;
    private final String url2;
    private final ResultTypes resultType;
    public Website(String name,String url1, String url2, ResultTypes r){
        this.name = name;
        this.url1 = url1;
        this.url2 = url2;
        this.resultType = r;
    }
    public Website(String name,String url1, String url2){
        this(name, url1, url2, ResultTypes.TEXT);
    }
    public String getName() {
        return name;
    }
    public String getSearchUrl(String keyword){
        return url1 + keyword.replaceAll(" ", "%20") + url2;
    }

    public abstract Elements getSearchResultElements(Document resultPage);
    public abstract String getResultUrl(Element htmlElement);
    public abstract String findUsefulData(Document webpage);

    public ResultTypes getResultType() {
        return resultType;
    }

    /**
     * Used to specify what kind of data the webscraper is extracting from it. Specifies how the data is represented on the website
     */
    public enum ResultTypes{
        NO_RESULT("?"),TEXT("<p>?</p>"), IMAGE("<img src=\"?\">"), VIDEO("<iframe> width=\"420\" height=\"315\"src=\"?\"></iframe>");
        private final String tag;
        ResultTypes(String tag){
            this.tag = tag;
        }
        public String injectData(String data){
            return tag.replace("?",data);
        }
    }
}
