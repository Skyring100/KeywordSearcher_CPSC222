import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import websites.Website;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
            //list out all the websites we are searching on
            StringBuilder searchList = new StringBuilder();
            for(WebScraper s : scrapers){
                Website w = s.getWebsite();
                //create a list of websites used in the search
                String listTag = "<li style=\"background-color:#"+getWebsiteColour(w.getName())+"\">";
                String aTag = "<a href=\""+w.getSearchUrl(keyword)+"\" target=\"_blank\">";
                searchList.append(listTag).append(aTag).append(w.getName()).append(" Search</a></li>");
            }
            rawHtml = rawHtml.replace("<ul>","<ul>\n"+searchList);
            //split the code into 2 parts, splitting at the table tags. This makes for easy insertion of search results later
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
                    //if a webscraper gave no result, subtract the amount of null responses from what we are expecting
                    int nullResponses = Integer.parseInt(d.getMainContent());
                    responsesRemaining -= nullResponses;
                    System.out.println("No result returned from "+d.getWebsiteName()+". Quantity: "+nullResponses);
                }else {
                    responsesRemaining--;
                    //formulate a new data entry row into the table
                    String webHeader = "<th style=\"background-color:#"+getWebsiteColour(d.getWebsiteName())+"\";>"+d.getWebsiteName()+"</th>";
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
        System.out.println("A webpage has been created about \""+keyword+"\" at "+customWebpage.getAbsolutePath());
    }

    /**
     *A unique and robust way to get a hex color is to use some sort of object's hash code
     * Note that this is completely arbitrary, just an interesting way to generate unique hex numbers per website
     * @param w website name
     * @return the corresponding color for that hex code
     */
    private String getWebsiteColour(String w){
        return Integer.toHexString(w.hashCode()).substring(0,6).toUpperCase();
    }
}
