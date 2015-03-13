package ind.habanero.realestatecalculator.Java;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import java.util.ArrayList;

import database.PropertyDataSource;
import database.RepairItemDataSource;
import models.Property;
import models.RepairItem;

/**
 * Created by michaelborglin on 2/19/15.
 */
public class RepairItemDataSourceTest extends AndroidTestCase {

    public static final String TAG = RepairItemDataSourceTest.class.getSimpleName();

    RepairItemDataSource mRepairItemDataSource;
    RenamingDelegatingContext context;

    Property mProperty1;
    Property mProperty2;
    PropertyDataSource mPropertyDataSource;
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        context = new RenamingDelegatingContext(getContext(), "test_");
        mRepairItemDataSource = new RepairItemDataSource(context);

        mProperty1 = PropertyTest.getProperty();
        mProperty2 = PropertyTest.getProperty();
        mPropertyDataSource = new PropertyDataSource(context);
        mPropertyDataSource.create(mProperty1);
        mPropertyDataSource.create(mProperty2);
    }


    public void testCreate() {
        RepairItem toAdd = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        mRepairItemDataSource.create(toAdd, mProperty1);
        Log.i(TAG, "ID: " + toAdd.getM_id());
        assertTrue(toAdd.getM_id() != -1);
    }

    public void testCreateMany(){
        RepairItem toAdd1 = new RepairItem(context, RepairItem.Type.BEDROOMS, 100);
        RepairItem toAdd2 = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        RepairItem toAdd3 = new RepairItem(context, RepairItem.Type.OUTSIDE_PAINT, 100);

        ArrayList<RepairItem> items = new ArrayList<RepairItem>();
        items.add(toAdd1);
        items.add(toAdd2);
        items.add(toAdd3);
        mRepairItemDataSource.create(items, mProperty1);

        assertTrue(toAdd1.getM_id() != -1);
        assertTrue(toAdd2.getM_id() != -1);
        assertTrue(toAdd3.getM_id() != -1);
        Log.i(TAG, "id1: " + toAdd1.getM_id());
        Log.i(TAG, "id2: " + toAdd2.getM_id());
        Log.i(TAG, "id3: " + toAdd3.getM_id());
    }

    public void testUpdate() {

        RepairItem toAdd = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        mRepairItemDataSource.create(toAdd, mProperty1);
        int originalID = toAdd.getM_id();
        Log.i(TAG, "id: " + originalID);
        toAdd.setAmount(50);
        toAdd.setPrice(100);
        toAdd.flipSwitch();
        mRepairItemDataSource.create(toAdd, mProperty1);

        Log.i(TAG, "id: " + toAdd.getM_id());
        assertTrue(originalID == toAdd.getM_id());
        assertTrue(toAdd.getAmount() == 50);
        assertTrue(toAdd.getPrice() == 100);
        assertTrue(toAdd.isSwitched() == true);
    }

    public void testUpdateMany(){
        RepairItem toAdd1 = new RepairItem(context, RepairItem.Type.BEDROOMS, 100);
        RepairItem toAdd2 = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        RepairItem toAdd3 = new RepairItem(context, RepairItem.Type.OUTSIDE_PAINT, 100);

        ArrayList<RepairItem> items = new ArrayList<RepairItem>();
        items.add(toAdd1);
        items.add(toAdd2);
        items.add(toAdd3);
        mRepairItemDataSource.create(items, mProperty1);

        int originalID1 = toAdd1.getM_id();
        int originalID2 = toAdd2.getM_id();
        int originalID3 = toAdd3.getM_id();

        assertTrue(toAdd1.getM_id() != -1);
        assertTrue(toAdd2.getM_id() != -1);
        assertTrue(toAdd3.getM_id() != -1);

        //updating
        toAdd1.setPrice(50);
        toAdd2.setAmount(75);
        toAdd3.flipSwitch();

        mRepairItemDataSource.create(items, mProperty1);

        assertTrue(toAdd1.getM_id() == originalID1);
        assertTrue(toAdd2.getM_id()== originalID2);
        assertTrue(toAdd3.getM_id() ==originalID3);
    }

    public void testRetrieve() {
        RepairItem toAdd1 = new RepairItem(context, RepairItem.Type.BEDROOMS, 100);
        RepairItem toAdd2 = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        RepairItem toAdd3 = new RepairItem(context, RepairItem.Type.OUTSIDE_PAINT, 100);
        toAdd1.flipSwitch();
        
        ArrayList<RepairItem> items = new ArrayList<RepairItem>();
        items.add(toAdd1);
        items.add(toAdd2);
        items.add(toAdd3);

        mPropertyDataSource.create(mProperty1);
        mRepairItemDataSource.create(items, mProperty1);
        ArrayList<RepairItem> retrieved = mRepairItemDataSource.getRepairItemsForProperty(mProperty1);

        retrieved.get(0).finishInit(context);
        retrieved.get(1).finishInit(context);
        retrieved.get(2).finishInit(context);

        assertTrue(toAdd1.equals(retrieved.get(0)));
        assertTrue(toAdd2.equals(retrieved.get(1)));
        assertTrue(toAdd3.equals(retrieved.get(2)));
    }

    //retrieve the correct list when multiple properties exist
    public void testRetrieve2() {

        RepairItem toAdd1 = new RepairItem(context, RepairItem.Type.BEDROOMS, 100);
        RepairItem toAdd2 = new RepairItem(context, RepairItem.Type.BATHROOMS, 100);
        RepairItem toAdd3 = new RepairItem(context, RepairItem.Type.OUTSIDE_PAINT, 100);
        toAdd1.flipSwitch();

        ArrayList<RepairItem> items = new ArrayList<RepairItem>();
        items.add(toAdd1);
        items.add(toAdd2);
        mRepairItemDataSource.create(items, mProperty1);

        ArrayList<RepairItem> items2 = new ArrayList<RepairItem>();
        items2.add(toAdd3);
        mRepairItemDataSource.create(items2, mProperty2);

        ArrayList<RepairItem> retrieved = new ArrayList<RepairItem>();
        retrieved = mRepairItemDataSource.getRepairItemsForProperty(mProperty1);

        retrieved.get(0).finishInit(context);
        retrieved.get(1).finishInit(context);


        assertTrue(toAdd1.equals(retrieved.get(0)));
        assertTrue(toAdd2.equals(retrieved.get(1)));
        assertTrue(retrieved.size() == items.size());

        ArrayList<RepairItem> retrieved2;
        retrieved2 = mRepairItemDataSource.getRepairItemsForProperty(mProperty2);
        retrieved2.get(0).finishInit(context);
        assertTrue(toAdd3.equals(retrieved2.get(0)));
        assertTrue(retrieved2.size() == items2.size());
    }

    public void tearDown() throws Exception{
        super.tearDown();
    }
}
