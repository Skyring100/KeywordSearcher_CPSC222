package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UNBC extends Website{
    public UNBC() {
        super("https://www2.unbc.ca/search/","","li.search-result");
    }

    @Override
    public Elements getSearchResultElements(Document resultPage) {
        return resultPage.select(getSearchResultElement());
    }

    @Override
    public String getResultUrl(Element htmlElement) {
        return "https://www2.unbc.ca/"+htmlElement.select("a").first().attr("href");
    }

    @Override
    public String findUsefulData(Document webpage) {
        String data = webpage.title()+"\n";
        //main "article" section on each webpage has the content of the webpage
        Element article = webpage.select("article").first();
        //UNBC lacks webpage structure, so guess which element is the main content based on text length
        //main content generally is in p and span tags. It can also be in div tags, but these are broadly used as larger containers not for housing raw text
        String mainContent = "";
        Elements possibleMains = article.select("p,span");
        for(Element candidate : possibleMains){
            String text = removeHTMLTags(candidate.html());
            //main content should be long, but if it's too long that element is likely not correct
            if(text.length() > mainContent.length() && text.length() < 700){
                mainContent = text;
            }
        }
        //HTML text also includes embedded invisible characters, so remove these from our text
        data += mainContent.replaceAll("&nbsp;"," ");
        return data;
    }
}