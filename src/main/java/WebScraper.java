import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import websites.Website;
import java.io.IOException;

public class WebScraper extends Thread{
    private final String url;
    private final Website website;
    private final int maxResults;
    public WebScraper(Website w, String keyword, int maxResults){
        website = w;
        url = w.getSearchUrl(keyword);
        this.maxResults = maxResults;
    }
    public WebScraper(Website w, String keyword){
        this(w,keyword,15);
    }
    @Override
    public void run() {
        //connect to the search result page
        Document searchResultPage;
        try {
            System.out.println("Going to "+url);
            searchResultPage = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //getting all elements that contain valuable search result links
        Elements resultElements = website.getSearchResultsFromPage(searchResultPage);
        if(resultElements.size() == 0){
            System.out.println("No results found");
            return;
        }
        //get the links from those search result elements
        for (int i = 0; i < maxResults; i++) {
            Element ele = resultElements.get(i);
            String resultLink = website.getSearchResultUrl(ele);
            Document resultDocument;
            try {
                resultDocument = Jsoup.connect(resultLink).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //later on, will use new threads to get data
            String usefulData = website.findUsefulData(resultDocument);
            System.out.println(resultLink+"\n"+usefulData);
        }
    }
}
