import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import websites.Website;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Goes to a specified, preconfigured website, searches a keyword, and grabs all the "useful data" per search result
 */
public class WebScraper extends Thread{
    private final String url;
    private final Website website;
    private final int maxResults;
    private BlockingQueue<MinedInfo> dataQueue;
    public WebScraper(Website w, String keyword, BlockingQueue<MinedInfo> d, int maxResults){
        website = w;
        url = w.getSearchUrl(keyword);
        dataQueue = d;
        this.maxResults = maxResults;
    }
    public WebScraper(Website w, String keyword, BlockingQueue<MinedInfo> d){
        this(w,keyword,d,5);
    }

    public Website getWebsite() {
        return website;
    }

    @Override
    public void run() {
        //connect to the search result page
        Document searchResultPage;
        try {
            System.out.println("Going to "+url);
            searchResultPage = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            dataQueue.add(new MinedInfo(website.getName(), "N/A",""+maxResults, Website.ResultTypes.NO_RESULT));
            return;
        }
        debugFile(searchResultPage);
        //getting all elements on the search results page that contain search result links
        Elements resultElements = website.getSearchResultElements(searchResultPage);
        if(resultElements.isEmpty()){
            //there were no results for this search
           dataQueue.add(new MinedInfo(website.getName(), "N/A",""+maxResults, Website.ResultTypes.NO_RESULT));
            return;
        }
        int noDataEntries = maxResults - resultElements.size();
        //parse through all the result elements and grab their links
        for (int i = 0; i < Math.min(maxResults, resultElements.size()); i++) {
            Element ele = resultElements.get(i);
            String resultLink = website.getResultUrl(ele);
            //create a new thread to extract the data from the resulting url
            DataExtractor extractor = new DataExtractor(resultLink);
            extractor.start();
        }
        if(noDataEntries > 0){
            //if there were fewer results returned than the desired max results, inform the organizer as such
            dataQueue.add(new MinedInfo(website.getName(), "N/A",""+noDataEntries, Website.ResultTypes.NO_RESULT));
        }
    }

    /**
     * Given a search result link, this will grab the "useful data" from the webpage
     */
    private class DataExtractor extends Thread {
        private final String dataURL;
        DataExtractor(String url){
            this.dataURL = url;
        }
        @Override
        public void run() {
            //connect to the webpage
            Document resultDocument;
            try {
                resultDocument = Jsoup.connect(dataURL).get();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                dataQueue.add(new MinedInfo(website.getName(), dataURL,""+1, Website.ResultTypes.NO_RESULT));
                return;
            }
            //extract the "useful data" as defined by the website object, which was passed in on webscraper's creation
            String usefulData = website.findUsefulData(resultDocument);
            dataQueue.add(new MinedInfo(website.getName(), dataURL, usefulData, website.getResultType()));
        }
    }

    /**
     * Creates a local version of containing the html of a specified webpage
     * @param page the webpage to download the html of
     */
    private static void debugFile(Document page) {
        try {
            FileWriter writer = new FileWriter("debug.html");
            writer.write(page.html());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
