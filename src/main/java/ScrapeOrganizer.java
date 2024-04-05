import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import websites.Website;

import java.io.File;
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
    private int responsesRemaining;
    private final int maxResults;
    private final String keyword;
    public ScrapeOrganizer(String keyword, Website[] websites, int maxResults){
        dataQueue = new LinkedBlockingQueue<>();
        this.keyword = keyword;
        this.maxResults = maxResults;
        //set up the webscrapers
        scrapers = new WebScraper[websites.length];
        for(int i = 0; i < websites.length; i++){
            scrapers[i] = new WebScraper(websites[i], keyword, dataQueue, maxResults);
        }
        responsesRemaining = websites.length * maxResults;
    }
    public ScrapeOrganizer(String keyword, Website[] websites){
        this(keyword, websites, 3);
    }
    @Override
    public void run() {
        //begin by starting the scrapers before any other work
        for(WebScraper scraper : scrapers){
            scraper.start();
        }
        StringBuilder tableSection = new StringBuilder();
        String[] baseHTML;
        //prepare a custom webpage by copying over a base, default webpage
        try {
            File basePlate = new File("html_baseplate/basePlate.html");
            Document baseDoc = Jsoup.parse(basePlate);
            String rawHtml = baseDoc.html().replace("\"mainTitle\">","\"mainTitle\">"+keyword.toUpperCase());
            //split the code into 2 parts, splitting at the table tags
            int tableStart = rawHtml.indexOf("<table>");
            int tableEnd = rawHtml.indexOf("</table>");
            baseHTML = new String[]{rawHtml.substring(0, tableStart+7), rawHtml.substring(tableEnd)};
        }catch (IOException e){
            throw new RuntimeException(e);
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
                if(d.getDataType().equals(Website.ResultTypes.NO_RESULT)){
                    int nullResponses = Integer.parseInt(d.getMainContent());
                    responsesRemaining -= nullResponses;
                    System.out.println("No result returned from "+d.getWebsiteName()+". Quantity: "+nullResponses);
                }else {
                    responsesRemaining--;
                    //eventually write this as "fill in the blank" HTML code but for now just write the contents of the data
                    String webHeader = "<th>"+d.getWebsiteName()+"</th>";
                    String mainContentSection = "<td>"+d.getDataType().injectData(d.getMainContent())+"</td>";
                    mainContentSection = mainContentSection.replaceAll("\n","<br>");
                    String urlFooter = "<td><a href=\""+d.getUrl()+"\" target=\"_blank\">Source</a>";
                    tableSection.append("<tr>").append(webHeader).append(mainContentSection).append(urlFooter).append("</tr>");
                }
                if(responsesRemaining == 0){
                    running = false;
                }
            }
        }
        //add all the data gathered into the webpage
        File customWebpage = new File("created_webpages/" + keyword + ".html");
        try {
            FileWriter writer = new FileWriter(customWebpage);
            writer.write(baseHTML[0]+tableSection+baseHTML[1]);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
