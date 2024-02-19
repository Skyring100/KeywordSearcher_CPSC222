import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
    }
    private static void sample(){
        //get a keyword from the user (might need to watch out for spaces for Google search)
        Scanner inputReader = new Scanner(System.in);
        System.out.print("Enter a word to get data on: ");
        String keyword =  inputReader.nextLine();
        //prepare a search
        String searchURL = "https://www.google.com/search?q=" +keyword;
        try {
            //get the html from the search results
            String htmlResults = Jsoup.connect(searchURL).get().html();
            //create a file to store the data in
            File htmlFile = new File(keyword+".html");
            FileWriter writer = new FileWriter(htmlFile);
            writer.write(htmlResults);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
