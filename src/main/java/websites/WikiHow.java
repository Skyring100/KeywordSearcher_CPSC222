package websites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * An implementation to make WikiHow to work with the webscrapers
 */
public class WikiHow extends Website{
    public WikiHow(){
        super("WikiHow","https://www.wikihow.com/wikiHowTo?search=", "&Search=");
    }


    @Override
    public Elements getSearchResultElements(Document resultPage) {
        Elements rawResults = resultPage.select("a.result_link");
        Elements cleansedResults = new Elements();
        //exclude "Category" results for Wikihow. This does not contain any directly useful data, so ignore it
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
        StringBuilder data = new StringBuilder();
        data.append(page.title()).append("\n");
        //get the summary paragraph section
        String summary = page.select("div.mf-section-0").text();
        data.append(summary).append("\n\n");
        Elements steps = page.select("div.step");
        int stepNumber = 1;
        for(Element s : steps){
            //from this "step", only get the bolded text for the step
            data.append("|STEP ").append(stepNumber++).append("| ");
            String text = s.select("b.whb").text();

            //check for embedded symbols. These do not function as intended when not on the WikiHow website, so remove them
            int invalidCharIndex = text.indexOf("{\"smallUrl\"");
            if(invalidCharIndex != -1){
                text = text.substring(0, invalidCharIndex);
            }
            data.append(text).append("\n");
        }
        return data.toString();
    }
}
