import websites.Website;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Organizes data received from a queue of data and makes a custom HTML page with the information gathered
 */
public class ScrapeOrganizer extends Thread{
    private BlockingQueue<MinedInfo> dataQueue;
    private final WebScraper[] scrapers;
    private FileWriter htmlWriter;
    public ScrapeOrganizer(String keyword, Website[] websites, int maxResults){
        dataQueue = new LinkedBlockingQueue<>();
        scrapers = new WebScraper[websites.length];
        for(int i = 0; i < websites.length; i++){
            scrapers[i] = new WebScraper(websites[i], keyword, dataQueue, maxResults);
        }
        try {
            htmlWriter = new FileWriter("created_webpages/"+keyword + ".html");
            //duplicate the "html_baseplate/basePlate.html" file

        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    public ScrapeOrganizer(String keyword, Website[] websites){
        this(keyword, websites, 4);
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



                //eventually write this as "fill in the blank" HTML code but for now just write the contents of the data
                try {
                    htmlWriter.write("\n"+d.getWebsiteName()+" says: \n"+d.getMainContent()+"\nFrom "+d.getUrl()+"\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
