package ind.habanero.realestatecalculator.Java;

import android.test.AndroidTestCase;

import models.BasicInformation;

/**
 * Created by stridentbean on 2/21/2015.
 */
public class BasicInformationTest extends AndroidTestCase {
    public static BasicInformation getMichael() {
        return new BasicInformation("Michael",
                "Borglin",
                "Habanero Industries",
                "16089213588",
                "borglin.me@gmail.com",
                "227 26th ave",
                "unit b",
                "San Francisco",
                "California",
                 94121);

    }
}
