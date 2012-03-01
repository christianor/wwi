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
import models.User;
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
        User user = User.findById(Long.parseLong(session.get("userid")));
        render(user);
    }

    public static void allSeries() {

        List<Object[]> results = JPA.em().createNativeQuery("SELECT serviceSeriesId, COUNT(*) AS anz FROM User_Series, Series WHERE Series.id = User_Series.series_id GROUP BY serviceSeriesId ORDER BY anz desc LIMIT 0,9").getResultList();
        List<String> ids = new ArrayList<String>();

        for (Object[] val : results) {
            ids.add(val[0].toString());
        }

        render(ids);
    }

    public static void showSeries(String serviceSeriesId) {
        models.Series series = models.Series.find("serviceSeriesId", serviceSeriesId).first();
        if (series == null) {
            response.status = 500;
        } else {
            // Long.parseLong(session.get("userid")), serviceSeriesId 
            List<Episode> episodes = JPA.em().createNativeQuery("SELECT Episode.id, Episode.episodeNumber, Episode.seasonNumber, Series.id AS series_id FROM "
                    + "User, Series, Episode, User_Series, User_Episode "
                    + "WHERE "
                    + "User.id = :userid "
                    + "and Series.serviceSeriesId = :serviceseriesid "
                    + "and Episode.series_id = Series.id "
                    + "and User_Series.users_id = User.id "
                    + "and User_Series.series_id = Series.id "
                    + "and User_Episode.users_id = User.id "
                    + "and User_Episode.episodes_id = Episode.id", Episode.class).setParameter("userid", Long.parseLong(session.get("userid"))).setParameter("serviceseriesid", serviceSeriesId).getResultList();

            JSONSerializer flex = new JSONSerializer().include(
                    "episodeNumber",
                    "seasonNumber").exclude("*");

            String json = "{}";

            if (episodes != null) {
                json = flex.serialize(episodes);
            }


            render(serviceSeriesId, json);
        }
    }

    public static void trackEpisode(String seriesId, int seasonNumber, int episodeNumber, String authenticityToken) {
        checkAuthenticity();
        User user = User.find("id", Long.parseLong(session.get("userid"))).first();
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
                JPA.em().createNativeQuery("DELETE FROM User_Episode WHERE users_id = ? AND episodes_id = ?").setParameter(1, user.id).setParameter(2, episode.id).executeUpdate();
                json = "{ \"response\": \"removed\"}";

            } else {
                JPA.em().createNativeQuery("INSERT INTO User_Episode (users_id, episodes_id) VALUES (?, ?)").setParameter(1, user.id).setParameter(2, episode.id).executeUpdate();
                json = "{ \"response\": \"added\"}";
            }

            renderJSON(json);

        } else {
            response.status = 500;
        }


    }
    
    public static void addToMySeries(String seriesId, String authenticityToken)
    {
        models.Series s = models.Series.find("serviceSeriesId", seriesId).first();
        if (s == null)
        {
            s = new models.Series();
            s.serviceSeriesId = seriesId;
            String json = helpers.UrlHelper.getTextByUrl("http://series-ortiz.rhcloud.com/series/" + seriesId + "/info?s=thetvdb");
            
            if (json.equals(""))
            {
                response.status = 500;
                return;
            }
            
            Gson gson = new Gson();
            jsonMappings.Series jsonResult = gson.fromJson(json, jsonMappings.Series.class);
            
            s.name = jsonResult.name;
            
            s.save();
        }
        
        checkAuthenticity();
        models.User u = models.User.findById(Long.parseLong(session.get("userid")));
        
        if (u.series.contains(s))
        {
            renderJSON("{\"response\": \"nothing\"}");
        }
        else
        {
            u.series.add(s);
            u.save();
            renderJSON("{\"response\": \"added\"}");
        }
        
        
        
    }
}
