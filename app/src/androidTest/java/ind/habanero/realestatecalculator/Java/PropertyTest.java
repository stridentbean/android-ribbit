package ind.habanero.realestatecalculator.Java;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import database.PropertyDataSource;
import database.RepairItemDataSource;
import models.Property;

/**
 * Created by stridentbean on 2/20/2015.
 */
public class PropertyTest extends AndroidTestCase{

    public static final String TAG = RepairItemDataSourceTest.class.getSimpleName();

    PropertyDataSource ds;
    RenamingDelegatingContext context;
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        context = new RenamingDelegatingContext(getContext(), "test_");
        ds = new PropertyDataSource(context);

    }

    public static Property getProperty() {
        return new Property("1 Dr Carlton B Goodlett Place",
                "",
                "San Francisco",
                "California",
                "94102",
                2000000000,
                "8",
                "7",
                40000,
                1906,
                70
        );
    }

    public void testCreate(Property property) {
        ds.create(getProperty());
    }
}
