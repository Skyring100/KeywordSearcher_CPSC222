package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiHow extends Website{
    public WikiHow(){
        super("WikiHow","https://www.wikihow.com/wikiHowTo?search=", "&Search=");
    }


    @Override
    public Elements getSearchResultElements(Document resultPage) {
        Elements rawResults = resultPage.select("a.result_link");
        Elements cleansedResults = new Elements();
        //exclude "Category" results for Wikihow
        for(Element r : rawResults){
            if(!getResultUrl(r).contains("https://www.wikihow.com/Category:")){
                cleansedResults.add(r);
            }
        }
        return cleansedResults;
    }

    @Override
    public String getResultUrl(Element htmlElement) {
        return htmlElement.attr("href");
    }

    @Override
    public String findUsefulData(Document page) {
        //In Wikihow, each page has a brief summary along with the numbered list of what to do
        String data = page.title()+"\n";
        //get the summary paragraph section
        String summary = removeHTMLTags(page.select("div.mf-section-0").html());
        data += summary+"\n\n";
        Elements steps = page.select("div.step");
        int stepNumber = 1;
        for(Element s : steps){
            //from this "step", only get the bolded text for the step
            data += "|STEP "+stepNumber++ +"| "+removeHTMLTags(s.select("b.whb").html()).replaceAll("\n"," ")+"\n";
        }
        return data;
    }
}
