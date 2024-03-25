package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiHow extends Website{
    public WikiHow(){
        super("https://www.wikihow.com/wikiHowTo?search=", "&Search=", "a.result_link");
    }


    @Override
    public Elements getSearchResultsFromPage(Document resultPage) {
        Elements rawResults = resultPage.select(getSearchResultElement());
        Elements cleansedResults = new Elements();
        //exclude "Category" results for Wikihow
        for(Element r : rawResults){
            if(!getSearchResultUrl(r).contains("https://www.wikihow.com/Category:")){
                cleansedResults.add(r);
            }
        }
        return cleansedResults;
    }

    @Override
    public String getSearchResultUrl(Element htmlElement) {
        return htmlElement.attr("href");
    }

    @Override
    public String findUsefulData(Document page) {
        //In Wikihow, each page has a brief summary along with the numbered list of what to do
        String data = "";
        //get the title of the website
        data += page.title()+"\n";
        //get the summary paragraph section
        String summary = getTextOnly(page.select("div.mf-section-0").html());
        data += summary+"\n\n";
        Elements steps = page.select("div.step");
        for(Element s : steps){
            //from this "step", only get the bolded text for the step
            data += getTextOnly(s.select("b.whb").html().replaceAll("\n"," "))+"\n";
        }
        return data;
    }
}
