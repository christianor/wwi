
import exceptions.SeriesNotFoundException;
import models.WWIUser;
import org.junit.Before;
import org.junit.Test;
import play.db.jpa.GenericModel;
import play.test.Fixtures;
import play.test.UnitTest;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Christian
 */
public class EpisodeTrackingTest extends UnitTest {

   @Before
   public void setUp() {
       Fixtures.deleteDatabase();
       Fixtures.loadModels("data.yml");
   }

    @Test(expected = SeriesNotFoundException.class)
    public void testIllegalEpisodeTracking() throws SeriesNotFoundException {

        WWIUser.trackEpisode(getUser().id, "xxx", 1, 1);


    }

    @Test
    public void testTrackAndUntrack() throws SeriesNotFoundException {

        models.Series series = models.Series.find("serviceSeriesId", "1").first();

        WWIUser.trackEpisode(getUser().id, series.serviceSeriesId, 1, 1);

        assertEquals(WWIUser.getEpisodes(getUser().id, series.serviceSeriesId).size(), 1);

        WWIUser.trackEpisode(getUser().id, series.serviceSeriesId, 1, 1);

        assertEquals(WWIUser.getEpisodes(getUser().id, series.serviceSeriesId).size(), 0);


    }
    
    public WWIUser getUser() {
        return WWIUser.find("email", "chris.ortiz.reina@googlemail.com").first();
    }
}
