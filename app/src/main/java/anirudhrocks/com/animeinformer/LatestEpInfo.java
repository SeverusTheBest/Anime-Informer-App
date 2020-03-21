package anirudhrocks.com.animeinformer;

import android.support.design.widget.Snackbar;
import android.view.View;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.UnknownHostException;

public class LatestEpInfo implements Runnable {
    private volatile String[] latestEpInfo;
    private static final String basicUrl = "https://www16.gogoanime.io/";
    private String animeTitle, languageType;
    private View view;

    public LatestEpInfo(String animeTitle, String languageType, View view) {
        latestEpInfo = new String[2];
        this.animeTitle = animeTitle.toLowerCase().replace(' ', '-');
        this.languageType = languageType;
        this.view = view;
    }

    @Override
    public void run() {

        try {

            String urlToBeSearched = basicUrl + "category/" + animeTitle;
            if(languageType.equals("DUB")) {
                urlToBeSearched += "-" + languageType.toLowerCase();
            }
            Connection connection = Jsoup.connect(urlToBeSearched);

            System.out.println(urlToBeSearched);
            System.out.println("-------------------------------");

            Document doc = connection.get();
            System.out.println(doc.title());
            System.out.println("-------------------------------");

            Element lastEpElem = doc.getElementById("episode_page").children().last();
            String episode = lastEpElem.text();

            latestEpInfo = new String[2];
            System.out.println(episode);
            // validation check required when only one episode has been released.
            if(episode.contains("-")) {
                latestEpInfo[0] = episode.split("-")[1];
            } else {
                latestEpInfo[0] = episode;
            }
            System.out.println(latestEpInfo[0]);
            System.out.println("-------------------------------");

            // url of the latest Episode
            String epLink;
            epLink = basicUrl + animeTitle;
            if(languageType.equals("DUB")) {
                latestEpInfo[1] += "-" + languageType.toLowerCase();
            }
            epLink += "-episode-" + latestEpInfo[0];
            latestEpInfo[1] = epLink;
            System.out.println(latestEpInfo[1]);
            System.out.println("-------------------------------");

        } catch (HttpStatusException e) {
            Snackbar.make(view, "No such anime present, please check if the name entered is correct.", Snackbar.LENGTH_LONG).show();
        } catch (UnknownHostException e) {
            Snackbar.make(view, "Host name: " + e.getMessage() + ", may have some error in it, or check your internet connection", Snackbar.LENGTH_LONG).show();
        } catch (IOException e) {
            Snackbar.make(view, "IOException occured", Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getLatestEpInfo() {
        return latestEpInfo;
    }



}
