import models.Hotel;
import org.junit.Test;
import play.test.UnitTest;

public class BasicTest extends UnitTest {

    @Test
    public void aVeryImportantThingToTest() {
        Hotel.find("byCity","Paris").first();

    }

}
