/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.gson.Gson;
import exceptions.SeriesNotFoundException;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.List;
import models.Episode;
import models.WWIUser;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.With;

/**
 *
 * @author Christian
 */
@With(Secure.class)
public class Series extends Controller {

    public static void userSeries() {
        WWIUser user = WWIUser.findById(Long.parseLong(session.get("userId")));
        render(user);
    }

    public static void allSeries() {

        List<models.Series> series = models.Series.getPopularSeries();

        JSONSerializer flex = new JSONSerializer().include("serviceSeriesId").exclude("*");

        String jsonSeries = flex.serialize(series);

        render(jsonSeries);
    }

    
    public static void showSeries(String serviceSeriesId) {

        List<Episode> episodes = null;
        try {
            episodes = WWIUser.getEpisodes(Secure.connectedId(), serviceSeriesId);
        } catch (Exception e) {
            error(404, serviceSeriesId + " - " + e.getMessage());
        }

        JSONSerializer flex = new JSONSerializer().include(
                "episodeNumber",
                "seasonNumber").exclude("*");

        String json = "{}";

        if (episodes != null) {
            json = flex.serialize(episodes);
        }


        render(serviceSeriesId, json);

    }

    public static void trackEpisode(String seriesId, int seasonNumber, int episodeNumber, String authenticityToken) {
        checkAuthenticity();

        String json = "{}";

        try {
            boolean result = WWIUser.trackEpisode(Secure.connectedId(), seriesId, seasonNumber, episodeNumber);
            
            if (result == false) {
                json = "{ \"response\": \"removed\"}";
            } else {
                json = "{ \"response\": \"added\"}";
            }

        } catch (SeriesNotFoundException e) {
            error(404, seriesId + " - " + e.getMessage());
        }


        renderJSON(json);
    }

    public static void addToMySeries(String seriesId, String authenticityToken) {
        checkAuthenticity();
        models.Series s = models.Series.find("serviceSeriesId", seriesId).first();
        if (s == null) {
            s = new models.Series();
            s.serviceSeriesId = seriesId;
            String json = helpers.UrlHelper.getTextByUrl("http://series-ortiz.rhcloud.com/series/" + seriesId + "/info?s=thetvdb");

            if (json.equals("")) {
                response.status = 500;
                return;
            }

            Gson gson = new Gson();
            jsonMappings.Series jsonResult = gson.fromJson(json, jsonMappings.Series.class);

            s.name = jsonResult.name;

            s.save();
        }


        WWIUser u = WWIUser.findById(Secure.connectedId());

        if (u.series.contains(s)) {
            renderJSON("{\"response\": \"nothing\"}");
        } else {
            u.series.add(s);
            u.save();
            renderJSON("{\"response\": \"added\"}");
        }



    }
}
