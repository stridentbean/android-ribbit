package ind.habanero.realestatecalculator.Java;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import junit.framework.TestCase;

import database.RepairItemDataSource;
import ind.habanero.realestatecalculator.AssignmentActivity;
import models.RepairItem;

/**
 * Created by michaelborglin on 2/19/15.
 */
public class RepairItemTest extends AndroidTestCase {

    public static final String TAG = RepairItemDataSourceTest.class.getSimpleName();

    RepairItemDataSource ds;
    RenamingDelegatingContext context;
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        context = new RenamingDelegatingContext(getContext(), "test_");
        ds = new RepairItemDataSource(context);

    }

    public void testEquals() {
        RepairItem tmp1 = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        RepairItem tmp2 = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        assertTrue(tmp1.equals(tmp2));
    }
    public void testNotEqualsPrice() {
        RepairItem tmp1 = new RepairItem(context, RepairItem.Type.BATHROOMS, 1);
        RepairItem tmp2 = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        assertFalse(tmp1.equals(tmp2));
    }
    public void testNotEqualsType() {
        RepairItem tmp1 = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        RepairItem tmp2 = new RepairItem(context, RepairItem.Type.BEDROOMS, 100);
        assertFalse(tmp1.equals(tmp2));
    }
}
