import websites.Website;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Organizes data received from a queue of data and makes a custom HTML page with the information gathered
 */
public class ScrapeOrganizer extends Thread{
    private BlockingQueue<MinedInfo> dataQueue;
    private final WebScraper[] scrapers;
    private FileWriter htmlWriter;
    private int responsesRemaining;
    public ScrapeOrganizer(String keyword, Website[] websites, int maxResults){
        dataQueue = new LinkedBlockingQueue<>();
        scrapers = new WebScraper[websites.length];
        for(int i = 0; i < websites.length; i++){
            scrapers[i] = new WebScraper(websites[i], keyword, dataQueue, maxResults);
        }
        responsesRemaining = websites.length * maxResults;
        try {
            Path newFilePath = Path.of("created_webpages/" + keyword + ".html");
            Files.deleteIfExists(newFilePath);
            Files.copy(Path.of("html_baseplate/basePlate.html"), newFilePath);
            htmlWriter = new FileWriter(newFilePath.toFile());

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
                try {
                    htmlWriter.write("\n"+d.getWebsiteName()+" says: \n"+d.getMainContent()+"\n\nFrom "+d.getUrl()+"\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if(responsesRemaining == 0){
                    running = false;
                }
            }
        }
        try {
            htmlWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("---DONE COLLECTING DATA---");
    }
}
