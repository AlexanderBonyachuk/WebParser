import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebScrape {
    public static void main(String[] args) {

        final String url =
                "https://www.telegraph.co.uk/markets-hub/?icid=gotomarketshubnow";
        try {
            final Document document = Jsoup.connect(url).get();

            for (Element row : document.select(
                    "div.col-lg-8 screener-table-container tr")) {
                if (row.select("td:nth-of-type(1)").text().equals("")) {
                    continue;
                } else {
                    final String ticker = row.select("td:nth-of-type(1)").text();
                    System.out.println(ticker);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
