package ind.habanero.realestatecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Utilities.PDFUtility;
import butterknife.ButterKnife;
import butterknife.InjectView;
import database.RepairItemDataSource;
import database.StorageUtility;
import models.BasicInformation;
import models.Property;
import models.RepairItem;


public class RepairEstimateActivity extends ActionBarActivity {

    public static final String TAG = RepairEstimateActivity.class.getSimpleName();

    @InjectView(R.id.assignmentButton) Button mAssignmentButton;
    @InjectView(R.id.doubleCloseButton) Button mDoubleCloseButton;
    @InjectView(R.id.repairItemList) ExpandableListView mRepairListView;
    @InjectView(R.id.homeButton) Button mHomeButton;
    @InjectView(R.id.emailRepairEstimateButton) TextView mEmailRepairEstimateButton;
    private Property mProperty;

    private List<RepairItem> groupList;
    private RepairListAdapter repairListAdapter;
    private int childCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_estimate);
        ButterKnife.inject(this);
        setTitle(getResources().getString(R.string.title_activity_repair_estimate));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_launcher);

        ArrayList<Property> properties = getIntent().getParcelableArrayListExtra("property");
        mProperty = properties.get(0);
        retrieveData();
        updateExpandableListHeight();   //needed to dynamically assign height

        repairListAdapter = new RepairListAdapter(this, groupList);

        mRepairListView.setAdapter(repairListAdapter);
        repairListAdapter.calculateTotal(); //need this to refresh totals after data is retrieved
        mRepairListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    childCount--;
                } else {
                    childCount++;
                }
                updateExpandableListHeight();
                return false;
            }
        });

        //email handler
        mEmailRepairEstimateButton.setOnClickListener(new View.OnClickListener() {
                                                          @Override
            public void onClick(View v) {
                if (isValidForm()) {
                    persistData();

                    //get basic info
                    Gson gson = new Gson();
                    String json = StorageUtility.read(getString(R.string.basic_information_location), getApplicationContext());
                    BasicInformation basicInformation = gson.fromJson(json, BasicInformation.class);

                    Intent mailer = PDFUtility.getRepairEstimateEmailIntent(basicInformation, mProperty, groupList, getApplicationContext());
                    startActivity(mailer);
                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                    }
                }
            });

        //Assignment Handler
        mAssignmentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                if(isValidForm()) {
                    persistData();
                    Intent i = new Intent(getApplicationContext(), AssignmentActivity.class);
                    ArrayList<Property> toSend = new ArrayList<Property>();
                    toSend.add(mProperty);
                    i.putParcelableArrayListExtra("property", toSend);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        //Double Close Handler
        mDoubleCloseButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                if(isValidForm()) {
                    persistData();

                    Intent i = new Intent(getApplicationContext(), DoubleCloseActivity.class);
                    ArrayList<Property> toSend = new ArrayList<Property>();
                    toSend.add(mProperty);
                    i.putParcelableArrayListExtra("property", toSend);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        }

        );

        //Home handler
        mHomeButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
            persistData();
            Intent i = new Intent(getApplicationContext(), EstimatePropertyActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        }

        );
    }

    private boolean isValidForm() {
        boolean isValid = true;
        if(repairListAdapter.calculateTotal() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.warning_repair_cost))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            isValid = false;
        }
        ArrayList<RepairItem> switchedAndEmpty;
        if((switchedAndEmpty = repairListAdapter.getSwitchedAndEmpty()).size() > 0) {

            String allSwitchedAndEmpty = "";
            for(RepairItem repairItem: switchedAndEmpty) {
                allSwitchedAndEmpty += repairItem.getTitle() + ", ";
            }
            allSwitchedAndEmpty = allSwitchedAndEmpty.substring(0, allSwitchedAndEmpty.length()-2);

            new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.warning_switched_and_empty) + allSwitchedAndEmpty)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


            isValid = false;
        }

        return isValid;
    }

    private void updateExpandableListHeight() {
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) mRepairListView.getLayoutParams();
        //TODO check to make sure the dividers are not the reason for this +1
        param.height = 148 * (groupList.size() + 1)  + 307 * childCount ;

        mRepairListView.setLayoutParams(param);
        mRepairListView.refreshDrawableState();

        Log.i(TAG, "childCount: " + childCount + " param.height: " +param.height);
    }

    private ArrayList<RepairItem> createGroupList() {
        ArrayList<RepairItem> list = new ArrayList<RepairItem>();
        list.add(new RepairItem(this, RepairItem.Type.OUTSIDE_PAINT, mProperty.getSquareFootage()));
        list.add(new RepairItem(this, RepairItem.Type.INSIDE_PAINT, mProperty.getSquareFootage()));
        list.add(new RepairItem(this, RepairItem.Type.REMOVE_POPCORN, mProperty.getSquareFootage()));
        list.add(new RepairItem(this, RepairItem.Type.TEXTURE_WALLS, mProperty.getSquareFootage()));
        list.add(new RepairItem(this, RepairItem.Type.BASE_BOARD_MOULDING, mProperty.getSquareFootage()));
        list.add(new RepairItem(this, RepairItem.Type.FLOORING, mProperty.getSquareFootage()));
        list.add(new RepairItem(this, RepairItem.Type.ELECTRICAL, 0));
        list.add(new RepairItem(this, RepairItem.Type.PLUMBING, 0));
        list.add(new RepairItem(this, RepairItem.Type.GARAGE_DOOR, 0));
        list.add(new RepairItem(this, RepairItem.Type.ROOF, 0));
        list.add(new RepairItem(this, RepairItem.Type.FRONT_DOOR, 0));
        list.add(new RepairItem(this, RepairItem.Type.BACK_DOOR, 0));
        list.add(new RepairItem(this, RepairItem.Type.WATER_HEATER, 0));
        list.add(new RepairItem(this, RepairItem.Type.HVAC, 0));
        list.add(new RepairItem(this, RepairItem.Type.POOL, 0));
        list.add(new RepairItem(this, RepairItem.Type.DUMPSTER, 0));
        list.add(new RepairItem(this, RepairItem.Type.PERMITS, 0));
        list.add(new RepairItem(this, RepairItem.Type.OTHER, 0));
        list.add(new RepairItem(this, RepairItem.Type.LANDSCAPING, 0));
        list.add(new RepairItem(this, RepairItem.Type.WINDOWS, 0));
        list.add(new RepairItem(this, RepairItem.Type.LIGHTS, 0));
        list.add(new RepairItem(this, RepairItem.Type.BEDROOMS, 0));
        list.add(new RepairItem(this, RepairItem.Type.BATHROOMS, 0));
        list.add(new RepairItem(this, RepairItem.Type.KITCHEN, 0));

        return list;

    }

    private void retrieveData() {
        RepairItemDataSource ds = new RepairItemDataSource(this);
        groupList = ds.getRepairItemsForProperty(mProperty);

        //if nothing is retrieved, create new
        if(groupList.isEmpty()) {
            groupList = createGroupList();
        }
    }

    private void persistData() {
        RepairItemDataSource ds = new RepairItemDataSource(this);
        mProperty.setRepairEstimate(repairListAdapter.calculateTotal());
        ds.create(groupList, mProperty);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), PropertyInfoActivity.class);
        ArrayList<Property> toSend = new ArrayList<Property>();
        toSend.add(mProperty);
        i.putParcelableArrayListExtra("property", toSend);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
