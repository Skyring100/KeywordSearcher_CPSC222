import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import websites.Website;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Organizes data received from a queue of data and makes a custom HTML page with the information gathered
 */
public class ScrapeOrganizer extends Thread{
    private BlockingQueue<MinedInfo> dataQueue;
    private final WebScraper[] scrapers;
    private final Element[] tableRows = new Element[3];
    private int responsesRemaining;
    public ScrapeOrganizer(String keyword, Website[] websites, int maxResults){
        dataQueue = new LinkedBlockingQueue<>();
        scrapers = new WebScraper[websites.length];
        for(int i = 0; i < websites.length; i++){
            scrapers[i] = new WebScraper(websites[i], keyword, dataQueue, maxResults);
        }
        //setup the rows for website, main content and link
        for(int i = 0; i < tableRows.length; i++){
            tableRows[i] = new Element("tr");
        }
        responsesRemaining = websites.length * maxResults;
        try {
            Path newFilePath = Path.of("created_webpages/" + keyword + ".html");
            Files.deleteIfExists(newFilePath);
            Files.copy(Path.of("html_baseplate/basePlate.html"), newFilePath);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public ScrapeOrganizer(String keyword, Website[] websites){
        this(keyword, websites, 3);
    }
    @Override
    public void run() {
        for(WebScraper scraper : scrapers){
            scraper.start();
        }
        boolean running = true;
        while (running){
            if(dataQueue.peek() != null){
                //extract data
                MinedInfo d;
                try {
                    d = dataQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                responsesRemaining--;


                //eventually write this as "fill in the blank" HTML code but for now just write the contents of the data
                System.out.println("\n"+d.getWebsiteName()+" says: \n"+d.getMainContent()+"\n\nFrom "+d.getUrl()+"\n");
                addDataToPage(d);

                if(responsesRemaining == 0){
                    running = false;
                }
            }
        }
        System.out.println("---DONE COLLECTING DATA---");
    }
    private void addDataToPage(MinedInfo d){
        tableRows[0].append("<th>"+d.getWebsiteName()+"</th>");
        tableRows[1].append("<td>"+d.getMainContent()+"</td>");
        tableRows[2].append("<td>"+d.getUrl()+"</td>");
    }
}
