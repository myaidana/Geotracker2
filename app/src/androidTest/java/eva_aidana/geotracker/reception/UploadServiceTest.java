package eva_aidana.geotracker.reception;
import android.content.Intent;
import android.test.ServiceTestCase;
import reception.SampleService;
import reception.UploadService;

/**
 * Created by anurla92 on 5/29/2015.
 */
public class UploadServiceTest extends ServiceTestCase<UploadService> {

    public UploadServiceTest() {
        super(UploadService.class);
    }

    public void testNotificationService() {
        Intent intent = new Intent();
        intent.setClass(getContext(), SampleService.class);
        startService(intent);
    }
}
