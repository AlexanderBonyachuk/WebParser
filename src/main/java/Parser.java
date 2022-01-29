import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static Document getPage() throws IOException {
        String url = "https://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    // \d{2}\.\d{2} pattern for identifying date, like 27.01
    private static Pattern patternDate = Pattern.compile("\\d{2}\\.\\d{2}");

    // \S+ regexp for finding first word in string
    private static Pattern patternFirsWord = Pattern.compile("\\S+");

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = patternDate.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from string!");
    }

    private static String getFirstWord(String inputString) throws Exception {
        Matcher matcher = patternFirsWord.matcher(inputString);
        String outString = "";
        if (matcher.find()) {
            outString = matcher.group();
            return outString;
        }
        throw new Exception("Can't extract date from string!");
    }

    private static int printPartValues(Elements values, int index) throws Exception {
        int iterationCount = 4;

        if (index == 0) {
            // treatment first several lines [1-4 lines]
            Element valueLn = values.get(3);
            String timesOfDay = getFirstWord(valueLn.text());
            switch (timesOfDay) {
                case "Утро":
                    iterationCount = 3;
                    break;
                case "День":
                    iterationCount = 2;
                    break;
                case "Вечер":
                    iterationCount = 1;
                    break;
                case "Ночь":
                    iterationCount = 0;
                    break;
            }
        }
        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index + i);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + "   ");
            }
            System.out.println();
        }
        System.out.println("_____________________________________________________________________");
        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        // css query language
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;

        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date +
                    "    Явления     Температура     Давление    Влажность   Ветер");
            int iterationCount = printPartValues(values, index);
            index += iterationCount;
        }
    }
}
