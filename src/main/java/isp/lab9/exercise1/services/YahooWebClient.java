package isp.lab9.exercise1.services;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import isp.lab9.exercise1.utils.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Uses htmlunit to retrieve stocks data from Yahoo Finance.
 *
 * @author Radu Miron
 */
public class YahooWebClient {
    private static final String YAHOO_FINANCE_URL = "https://finance.yahoo.com/?guccounter=1";
    private static final String CRUMB_URL = "https://query1.finance.yahoo.com/v1/test/getcrumb";
    private static final String QUOTES_QUERY1V7_BASE_URL = "https://query1.finance.yahoo.com/v7/finance/quote";
    private static final String CRUMB_ERR_MSG = "Unable to get the crumb";
    private static WebClient webClient;
    private static String crumb;

    private YahooWebClient() {
    }

    public static StockItem get(String symbol) throws IOException {
        try {
            return getQuotes(symbol).stream().findFirst().orElse(null);
        } catch (Exception e) {
            System.err.println("Yahoo is down. Returning dummy data!");
            return dummyDataFallback().get(symbol);
        }
    }

    public static List<StockItem> get(String[] symbols) throws IOException {
        try {
            return getQuotes(Utils.join(symbols, ","));
        } catch (Exception e) {
            System.err.println("Yahoo is down. Returning dummy data!");
            Map<String, StockItem> dummyData = dummyDataFallback();
            return Arrays.stream(symbols).map(dummyData::get).toList();
        }
    }

    private static List<StockItem> getQuotes(String query) throws IOException {
        initializeCrumb();
        Map<String, String> params = new LinkedHashMap();
        params.put("symbols", query);
        params.put("crumb", crumb);
        String url = QUOTES_QUERY1V7_BASE_URL + "?" + Utils.getURLParameters(params);
        String stocksJson = getWebClient().getPage(url).getWebResponse().getContentAsString().trim();

        if (!stocksJson.startsWith("{") && !stocksJson.startsWith("[")) {
            throw new IOException("Invalid response from Yahoo (not JSON): " + stocksJson);
        }

        return Utils.parseStocksJson(stocksJson);
    }

    private static WebClient getWebClient() throws IOException {
        if (webClient == null) {
            webClient = new WebClient(BrowserVersion.CHROME);
            webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.setCssErrorHandler(new SilentCssErrorHandler());
        }

        return webClient;
    }

    private static void initializeCrumb() throws IOException {
        if (crumb == null) {
            //go to home page - you get redirected to accept cookies page
            HtmlPage home = getWebClient().getPage(YAHOO_FINANCE_URL);

            //select Agree button and accept the cookies by clicking it
            HtmlButton agreeButton = (HtmlButton) home.getByXPath("//button[@name='agree'),").stream()
                    .findFirst().orElseThrow(() -> new RuntimeException(CRUMB_ERR_MSG));
            agreeButton.click();

            //now, you have all the needed cookies in webClient; you can retrieve the crumb

            //get the crumb
            WebRequest crumbRequest = new WebRequest(new URL(CRUMB_URL));
            Page crumbPage = getWebClient().getPage(crumbRequest);
            crumb = crumbPage.getWebResponse().getContentAsString().trim();

            if (crumb.isEmpty()) {
                throw new RuntimeException(CRUMB_ERR_MSG);
            }

            System.out.println("Got crumb: " + crumb);
        }
    }

    private static Map<String, StockItem> dummyDataFallback() {
        Set<StockItem> stocks = Set.of(new StockItem("INTC", new BigDecimal(66.78), new BigDecimal(2.31), "USD", "NasdaqGS", "Intel Corporation"),
                new StockItem("BABA", new BigDecimal(131.7), new BigDecimal(-3.46), "USD", "NYSE", "Alibaba Group Holding Limited"),
                new StockItem("TSLA", new BigDecimal(373.72), new BigDecimal(-3.56), "USD", "NasdaqGS", "Tesla, Inc."),
                new StockItem("AIR.PA", new BigDecimal(164.06), new BigDecimal(-2.44), "EUR", "Paris", "Airbus SE"),
                new StockItem("MSFT", new BigDecimal(415.75), new BigDecimal(-3.97), "USD", "NasdaqGS", "Microsoft Corporation"),
                new StockItem("AAPL", new BigDecimal(273.43), new BigDecimal(0.10), "USD", "NasdaqGS", "Apple Inc."),
                new StockItem("OHI", new BigDecimal(46.35), new BigDecimal(2.98), "USD", "NYSE", "Omega Healthcare Investors, Inc."),
                new StockItem("MMM", new BigDecimal(144.84), new BigDecimal(-0.64), "USD", "NYSE", "3M Company"),
                new StockItem("SWK", new BigDecimal(76.01), new BigDecimal(0.41), "USD", "NYSE", "Stanley Black & Decker, Inc."),
                new StockItem("PFE", new BigDecimal(26.67), new BigDecimal(-0.49), "USD", "NYSE", "Pfizer Inc."),
                new StockItem("ABBV", new BigDecimal(200.95), new BigDecimal(0.22), "USD", "NYSE", "AbbVie Inc."),
                new StockItem("JNJ", new BigDecimal(230.65), new BigDecimal(2.01), "USD", "NYSE", "Johnson & Johnson"),
                new StockItem("MDT", new BigDecimal(83.79), new BigDecimal(0.68), "USD", "NYSE", "Medtronic plc"),
                new StockItem("RIO", new BigDecimal(98.85), new BigDecimal(-1.43), "USD", "NYSE", "Rio Tinto Group"),
                new StockItem("EPD", new BigDecimal(37.84), new BigDecimal(0.26), "USD", "NYSE", "Enterprise Products Partners L.P."),
                new StockItem("ET", new BigDecimal(19.15), new BigDecimal(0.42), "USD", "NYSE", "Energy Transfer LP"),
                new StockItem("USA", new BigDecimal(5.74), new BigDecimal(-1.20), "USD", "NYSE", "Liberty All-Star Equity Fund"),
                new StockItem("BHP", new BigDecimal(79.84), new BigDecimal(-0.89), "USD", "NYSE", "BHP Group Limited"),
                new StockItem("BP", new BigDecimal(46.35), new BigDecimal(-0.04), "USD", "NYSE", "BP p.l.c."),
                new StockItem("BCE", new BigDecimal(24.1), new BigDecimal(1.56), "USD", "NYSE", "BCE Inc."),
                new StockItem("VZ", new BigDecimal(47.22), new BigDecimal(2.70), "USD", "NYSE", "Verizon Communications Inc."),
                new StockItem("GOOG", new BigDecimal(337.75), new BigDecimal(0.01), "USD", "NasdaqGS", "Alphabet Inc."));

        return stocks.stream().collect(Collectors.toMap(StockItem::symbol, Function.identity()));
    }
}
