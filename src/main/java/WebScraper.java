import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WebScraper extends Thread{
    private String url;
    private String tag;

    public WebScraper(String url, String tagOfInterest){
        this.url = url;
        this.tag = tagOfInterest;
    }

    @Override
    public void run() {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements selections = document.select(tag);
        for(Element ele : selections){
            System.out.println(ele.html());
        }



        try {
            writeDebugFile(document.html());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void writeDebugFile(String html) throws IOException {
        File htmlFile = new File(tag+".html");
        FileWriter writer = new FileWriter(htmlFile);
        writer.write(html);
        writer.close();
    }
}
