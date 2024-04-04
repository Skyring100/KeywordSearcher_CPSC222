package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        return url1 + keyword + url2;
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

    public ResultTypes getResultType() {
        return resultType;
    }

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
