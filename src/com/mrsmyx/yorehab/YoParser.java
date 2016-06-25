package com.mrsmyx.yorehab;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mrsmyx.yorehab.callbacks.YoSearchListener;
import com.mrsmyx.yorehab.enums.Month;
import com.mrsmyx.yorehab.models.YoItem;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import static com.mrsmyx.yorehab.utils.Utils.isNumeric;

/**
 * Created by cj on 1/24/16.
 */
public final class YoParser {
    private static String baseUrl = "http://yorehab.com/?s=%s",
            baseUrlPage = "http://yorehab.com/page/%s/?s=%s";


    public static List<YoItem> Search(String item, boolean checkforcat) throws IOException {
        List<YoItem> yoItemList = new ArrayList<>();
        String s = (item.contains("http")) ? item : String.format(baseUrl, URLEncoder.encode(item, "UTF-8"));
        Document document = Jsoup.parse(new URL(s), 10000);
        return Parse(document, null, checkforcat);
    }

    public static void SearchAsync(String item, YoSearchListener yoSearchListener) {
        try {
            List<YoItem> yoItemList = new ArrayList<>();
            String s = (item.contains("http")) ? item : String.format(baseUrl, URLEncoder.encode(item, "UTF-8"));
            Document document = Jsoup.parse(new URL(s), 10000);
            Elements elements = document.getElementsByClass("yorehab-item");
            if (elements == null || elements.size() == 0) {
                yoSearchListener.OnItemSearchError("Item Not Found");
            } else {
                for (Element element : elements) {
                    String baseUrl = element.getElementsByTag("a").attr("href");
                    String srcImg = element.getElementsByTag("img").attr("src");
                    String title = "";
                    String price = "";
                    String date = "";
                    Month month = null;
                    int year = 0;
                    String[] bitchAssDate = srcImg.split("/");
                    int go = 0;
                    for (String d : bitchAssDate) if (isNumeric(d)) date += go++ != 0 ? "/" + d : d;
                    bitchAssDate = date.split("/");
                    if (bitchAssDate.length > 1) {
                        String b1 = bitchAssDate[0];
                        String b2 = bitchAssDate[1];
                        date = (month = Month.values()[Integer.parseInt(b2)]).name() + ", " + String.valueOf(year = (Integer.parseInt(b1)));
                    }
                    go = 0;
                    for (Element e : element.getElementsByTag("strong"))
                        title += (go++ != 0) ? (" " + e.text().trim()) : e.text().trim();
                    go = 0;
                    for (Element e : element.getElementsByTag("p"))
                        if (!e.html().contains("<strong"))
                            price += (go++ != 0) ? (" " + e.text().trim()) : (e.text().trim());
                    yoItemList.add(YoItem.Builder().setItem_base_url(baseUrl).setItem_img(srcImg).setItem_name(title).setItem_price(price).setItem_rel(date).setItem_month(month).setItem_year(year));
                }
                yoSearchListener.OnItemSearchFound(yoItemList);
                Element page = document.getElementById("pagination-flickr");
                if (page != null) {
                    Elements e = page.getElementsByClass("next");
                    if (e != null) {
                        for (Element x : e) {
                            String link = x.getElementsByTag("a").attr("href");
                            if (link != null && !link.isEmpty()) {
                                SearchAsync(link, yoSearchListener);
                            }
                        }
                    }
                }
            }
        }catch (Exception ex){
            yoSearchListener.OnItemSearchError(ex.getMessage());
        }
    }


    private static List<YoItem> Parse(Document document, String cat, boolean checkforcat) throws IOException {
        String category = "";
        if (checkforcat) {
            Element ele = document.getElementById("pagebody-right");
            if (ele != null) {
                Element elem = ele.getElementsByClass("post").get(0);
                if (elem != null) {
                    Elements element = elem.getElementsByTag("h1");
                    if (element != null) {
                        for (Element e : element) {
                            if (category.isEmpty()) {
                                category += e.text();
                                if (category.length() > 0) {
                                    cat = category;
                                }
                            } else
                                break;
                        }
                    }
                }
            }
        }
        List<YoItem> yoItemList = new ArrayList<>();
        Elements elements = document.getElementsByClass("yorehab-item");
        if (elements == null) {
            return yoItemList;
        } else {
            for (Element element : elements) {
                String baseUrl = element.getElementsByTag("a").attr("href");
                String srcImg = element.getElementsByTag("img").attr("src");
                String title = "";
                String price = "";
                String date = "";
                Month month = null;
                int year = 0;
                String[] bitchAssDate = srcImg.split("/");
                int go = 0;
                for (String d : bitchAssDate) if (isNumeric(d)) date += go++ != 0 ? "/" + d : d;
                bitchAssDate = date.split("/");
                if (bitchAssDate.length > 1) {
                    String b1 = bitchAssDate[0];
                    String b2 = bitchAssDate[1];
                    date = (month = Month.values()[Integer.parseInt(b2)]).name() + ", " + String.valueOf(year = (Integer.parseInt(b1)));
                }
                go = 0;
                for (Element e : element.getElementsByTag("strong"))
                    title += (go++ != 0) ? ((e.text().trim().length() == 0) ? "" : " " + e.text().trim()) : (e.text().trim().length() == 0) ? "" : e.text().trim();
                go = 0;
                if (title.length() != 0)
                    title = (title.charAt(0) == ' ') ? title.substring(1) : title;
                else title = "Unknown";
                for (Element e : element.getElementsByTag("p"))
                    if (!e.html().contains("<strong"))
                        price += (go++ != 0) ? (" " + e.text().trim()) : (e.text().trim());
                if (price.length() == 0)
                    for (Element e : element.getElementsByTag("p"))
                        price += (go++ != 0) ? (" " + e.text().trim()) : (e.text().trim());
                price = price.replace(title + " ", "").replace(title, "");
                yoItemList.add(YoItem.Builder().setItem_base_url(baseUrl).setItem_img(srcImg).setItem_name(title).setItem_price(price).setItem_rel(date).setItem_month(month).setItem_year(year).setItem_cat(cat == null ? "" : cat).setItem_cat_base_url(document.baseUri()));
            }
            Element page = document.getElementById("pagination-flickr");
            if (page != null) {
                Elements e = page.getElementsByClass("next");
                if (e != null) {
                    for (Element x : e) {
                        String link = x.getElementsByTag("a").attr("href");
                        if (link != null && !link.isEmpty()) {
                            System.out.println("--Next Page");
                            yoItemList.addAll(Search(link, checkforcat));
                            return yoItemList;
                        }
                    }
                }
            }
            return yoItemList;
        }
    }

    public static boolean Dump(String link, String file) throws IOException {
        Document d = Jsoup.parse(new URL(link), 20000);
        Element ele = d.getElementById("pagebody-right");
        if (ele != null) {
            Element elem = ele.getElementsByClass("post").get(0);
            if (elem != null) {
                Elements element = elem.getElementsByTag("h1");
                if (element != null) {
                    String category = "";
                    for (Element e : element) {
                        if (category.isEmpty())
                            category += e.text();
                        else
                            break;
                    }
                    List<YoItem> yoItemList = Parse(d, category, true);
                    String s = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(yoItemList);
                    if (!new File("JSON/" + category + ".json").exists()) {
                        FileOutputStream fos = new FileOutputStream(new File("JSON/" + category.replace("/", "") + ".json"));
                        fos.write(s.getBytes());
                        fos.flush();
                        fos.close();
                    } else {
                        File find = findNonExisting("JSON/", category.replace("/", " "), ".json");
                        FileOutputStream fos = new FileOutputStream(find);
                        fos.write(s.getBytes());
                        fos.flush();
                        fos.close();
                    }
                    System.out.println("Dumped: " + category);
                    return true;
                } else {
                    System.out.println("Element is null");
                }
            } else {
                System.out.println("elem is null");
            }
        } else {
            System.out.println("ele is null");
        }
        return false;
    }

    private static File findNonExisting(String s, String category, String s1) {
        int i = 0;
        while (true) {
            if (!new File(s + category + "-" + String.valueOf(i) + ".json").exists()) {
                return new File(s + category + "-" + String.valueOf(i) + ".json");
            }
            i++;
        }
    }

    public static List<YoItem> GrabJsonToList(File file) throws IOException {
        if (file.toString().endsWith(".json")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String ph = null;
            StringBuilder sb = new StringBuilder();
            while ((ph = br.readLine()) != null)
                sb.append(ph);
            Type listType = new TypeToken<ArrayList<YoItem>>() {
            }.getType();
            return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().fromJson(sb.toString(), listType);
        } else {
            return new ArrayList<YoItem>();
        }
    }

    public static void Json2Sql(List<YoItem> yoParserList) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (YoItem yoItem : yoParserList){
            String query = String.format("INSERT INTO %s (item_name,item_price,item_cat,item_rel,item_img,item_month,item_year,item_base_url,item_cat_base_url) VALUES(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\");\n",
                    "`yoprice`.`Items`", yoItem.getItem_name(), yoItem.getItem_price(), yoItem.getItem_cat(), yoItem.getItem_rel()
            , yoItem.getItem_img(), yoItem.getItem_month(), yoItem.getItem_year(), yoItem.getItem_base_url(), yoItem.getItem_cat_base_url());
            sb.append(query);
        }
        FileOutputStream fileOutputStream = new FileOutputStream(new File("YoPrice.sql"));
        fileOutputStream.write(sb.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
