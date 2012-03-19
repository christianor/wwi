
import models.Series;
import models.Episode;
import models.WWIUser;
import org.junit.Before;
import org.junit.Test;
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
public class UserAddingEpisodesTest extends UnitTest {

   @Before
   public void setUp() {
       Fixtures.deleteDatabase();
       Fixtures.loadModels("data.yml");
   }
   
   @Test
   public void TestAddingEpisodes() {
       WWIUser user = WWIUser.find("email", "chris.ortiz.reina@googlemail.com").first();
       
       user.series.add((Series)Series.find("serviceSeriesId", "tt0773262").first());

       Episode e = new Episode();
       e.episodeNumber = 1;
       e.seasonNumber = 1;
       e.series = Series.find("serviceSeriesId", "tt0773262").first();
       e.save();
       
       user.episodes.add(e);
       user.save();
       
       Episode e2 = new Episode();
       e2.episodeNumber = 2;
       e2.seasonNumber = 1;
       e2.series = Series.find("serviceSeriesId", "tt0773262").first();
       e2.save();
       
       user.episodes.add(e2);
       user.save();
       
       assertEquals(user.episodes.size(), 2);
       
   }
}
