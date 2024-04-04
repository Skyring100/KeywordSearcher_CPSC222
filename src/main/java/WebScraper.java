import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import websites.Website;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

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
    @Override
    public void run() {
        //connect to the search result page
        Document searchResultPage;
        try {
            System.out.println("Going to "+url);
            searchResultPage = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Failed to reach "+url+"\n"+e.getMessage());
            dataQueue.add(new MinedInfo(website.getName(), "N/A",""+maxResults, Website.ResultTypes.NO_RESULT));
            return;
        }
        debugFile(searchResultPage);
        //getting all elements that contain valuable search result links
        Elements resultElements = website.getSearchResultElements(searchResultPage);
        if(resultElements.isEmpty()){
           dataQueue.add(new MinedInfo(website.getName(), "N/A",""+maxResults, Website.ResultTypes.NO_RESULT));
            return;
        }
        int noDataEntries = maxResults - resultElements.size();
        //parse through a result elements and grab their links
        for (int i = 0; i < Math.min(maxResults, resultElements.size()); i++) {
            Element ele = resultElements.get(i);
            String resultLink = website.getResultUrl(ele);
            //extract the data from the resulting url
            DataExtractor extractor = new DataExtractor(resultLink);
            extractor.start();
        }
        if(noDataEntries > 0){
            dataQueue.add(new MinedInfo(website.getName(), "N/A",""+noDataEntries, Website.ResultTypes.NO_RESULT));
        }
    }
    private class DataExtractor extends Thread {
        private final String dataURL;
        DataExtractor(String url){
            this.dataURL = url;
        }
        @Override
        public void run() {
            //System.out.println(website.getName()+" "+dataURL);
            Document resultDocument;
            try {
                resultDocument = Jsoup.connect(dataURL).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String usefulData = website.findUsefulData(resultDocument);

            dataQueue.add(new MinedInfo(website.getName(), dataURL, usefulData, website.getResultType()));
        }
    }
    private static void debugFile(Document page) {
        try {
            FileWriter writer = new FileWriter("debug"+page.title()+".html");
            writer.write(page.html());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
