
import exceptions.SeriesNotFoundException;
import java.util.logging.Logger;
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

        
        WWIUser user = getUser();
        
        play.Logger.debug("starting tack and untrack test");
        
        WWIUser.trackEpisode(user.id, "1", 1, 1);
        
        play.Logger.debug("tracked episode");
        
        user.refresh();
        assertEquals(WWIUser.getEpisodes(user.id, "1").size(), 1);

        WWIUser.trackEpisode(user.id, "1", 1, 1);
        
        user.refresh();
        assertEquals(WWIUser.getEpisodes(user.id, "1").size(), 0);
        
        play.Logger.debug("finished tack and untrack test");


    }
    
    public WWIUser getUser() {
        return WWIUser.find("email", "chris.ortiz.reina@googlemail.com").first();
    }
}
