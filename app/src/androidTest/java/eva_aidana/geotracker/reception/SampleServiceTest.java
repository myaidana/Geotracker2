package eva_aidana.geotracker.reception;
import android.content.Intent;
import android.test.ServiceTestCase;
import reception.SampleService;

/**
 * Created by anurla92 on 5/29/2015.
 */
public class SampleServiceTest extends ServiceTestCase<SampleService> {

    public SampleServiceTest() {
        super(SampleService.class);
    }

    public void testNotificationService() {
        Intent intent = new Intent();
        intent.setClass(getContext(), SampleService.class);
        startService(intent);
    }
}
