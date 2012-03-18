/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.gson.Gson;
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
            episodes = Episode.getEpisodes(Secure.connectedId(), serviceSeriesId);
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
        WWIUser user = WWIUser.findById(Long.parseLong(session.get("userId")));
        String json = "{}";

        if (user != null) {
            models.Series series = models.Series.find("serviceSeriesId", seriesId).first();

            if (series == null) {
                series = new models.Series();
                series.serviceSeriesId = seriesId;
                series.save();
            }

            Episode episode = Episode.find("episodeNumber = ? and seasonNumber = ? and series_id = ?", episodeNumber, seasonNumber, series.id).first();

            if (episode == null) {
                episode = new Episode();
                episode.episodeNumber = episodeNumber;
                episode.seasonNumber = seasonNumber;
                episode.series = series;
                episode.save();
            }

            episode.series = series;

            if (user.episodes.contains(episode)) {
                user.episodes.remove(episode);
                JPA.em().createNativeQuery("DELETE FROM WWIUser_Episode WHERE users_id = ? AND episodes_id = ?").setParameter(1, user.id).setParameter(2, episode.id).executeUpdate();
                json = "{ \"response\": \"removed\"}";

            } else {
                JPA.em().createNativeQuery("INSERT INTO WWIUser_Episode (users_id, episodes_id) VALUES (?, ?)").setParameter(1, user.id).setParameter(2, episode.id).executeUpdate();
                json = "{ \"response\": \"added\"}";
            }

            renderJSON(json);

        } else {
            response.status = 500;
        }


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


        WWIUser u = WWIUser.findById(Long.parseLong(session.get("userId")));

        if (u.series.contains(s)) {
            renderJSON("{\"response\": \"nothing\"}");
        } else {
            u.series.add(s);
            u.save();
            renderJSON("{\"response\": \"added\"}");
        }



    }
}
