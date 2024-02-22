import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import websites.Website;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WebScraper extends Thread{
    private final String url;
    private final Website website;
    public WebScraper(Website w, String keyword){
        website = w;
        url = w.getSearchUrl(keyword);
    }
    @Override
    public void run() {
        Document document;
        try {
            System.out.println("Going to "+url);
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //FOR DEBUG
        try {
            writeDebugFile(document.html());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //getting all element that contain valuable search result links
        Elements resultElements = document.select(website.getSearchResultElement());
        if(resultElements.size() == 0){
            System.out.println("No results found");
            return;
        }
        //get the links from those search result elements
        for (Element ele : resultElements) {
            String resultLink = website.getResultUrl(ele);
            String resultHTML;
            try {
                resultHTML = Jsoup.connect(resultLink).get().html();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //later on, will use new threads to get data
            String usefulData = website.findResultData(resultHTML);
            System.out.println(resultLink+"\n"+usefulData);
        }



    }
    private void writeDebugFile(String html) throws IOException {
        File htmlFile = new File("result_pages/"+website.getSearchResultElement() +".html");
        FileWriter writer = new FileWriter(htmlFile);
        writer.write(html);
        writer.close();
    }
}
