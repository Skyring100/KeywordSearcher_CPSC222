package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * An implementation to make UNBC's website to work with the webscrapers
 */
public class UNBC extends Website{
    public UNBC() {
        super("UNBC","https://www.unbc.ca/search?site_keyword=","");
    }

    @Override
    public Elements getSearchResultElements(Document resultPage) {
        return resultPage.select("div.views-row");
    }

    @Override
    public String getResultUrl(Element htmlElement) {
        return "https://www2.unbc.ca/"+htmlElement.select("a").first().attr("href");
    }

    @Override
    public String findUsefulData(Document webpage) {
        String data = webpage.title()+"\n\n";
        //main "article" section on each webpage has the content of the webpage
        Element mainSection = webpage.select("article").first();
        //check if the article section is null
        if(mainSection == null){
            //the timberwolves pages comes up a lot when searching, so have a special case for that
            if(webpage.location().equals("https://unbctimberwolves.com/")){
                return "This link went to the Timberwolves website for UNBC!";
            }
            //if not, we will just search the entire page instead of a small section
            System.out.println("No article found on UNBC page, general scrape on "+webpage.location());
            mainSection = webpage.select("body").first();
        }
        //UNBC lacks webpage structure, so guess which element is the main content based on text length
        //main content generally is in p, div and span tags
        String mainContent = "";
        Elements possibleMains = mainSection.select("p,span,div");
        for(Element candidate : possibleMains){
            String text = candidate.text();
            //main content should be long, but if it's too long that element is likely not correct
            if(text.length() > mainContent.length() && text.length() < 1000){
                mainContent = text;
            }
        }
        //HTML text also includes embedded invisible characters, so remove these from our text
        data += mainContent.replaceAll("&nbsp;"," ");
        return data;
    }
}
