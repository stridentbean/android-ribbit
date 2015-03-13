package ind.habanero.realestatecalculator;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Exceptions.NotFullyInitializedException;
import Utilities.Utility;
import models.RepairItem;

/**
 * Created by michaelborglin on 2/18/15.
 */
public class RepairListAdapter extends BaseExpandableListAdapter {

    public static final String TAG = RepairListAdapter.class.getSimpleName();
    private final List<RepairItem> repairItems;
    private Activity mContext;

    public RepairListAdapter(RepairEstimateActivity repairEstimateActivity, List<RepairItem> groupList) {
        this.mContext = repairEstimateActivity;
        this.repairItems = groupList;
    }

    @Override
    public int getGroupCount() {
        return repairItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return repairItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return repairItems.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        RepairItem repairItem = (RepairItem) getGroup(groupPosition);
        final GroupHolder groupHolder;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.repair_group_item,
                    null);

            //assigning ui elements
            groupHolder = new GroupHolder();
            groupHolder.title = (TextView)convertView.findViewById(R.id.title);
            groupHolder.mySwitch = (Switch) convertView.findViewById(R.id.toggle);


            /*
            It is CRITICAL that the watchers be assigned inside the (convertView == null) block.
            It prevents multiple text watchers from being assigned to the same element
             */
            //Switch handler
            groupHolder.mySwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "isChecked: " + ((RepairItem) getGroup(groupPosition)).getTitle());
                    //mySwitch.setOnClickListener(null);
                    ((RepairItem)groupHolder.mySwitch.getTag()).flipSwitch();
                    //mySwitch.setOnClickListener(this);
                    calculateTotal();
                }
            });

            convertView.setTag(groupHolder);

        } else {
            //loading a child that has been loaded before
            groupHolder = (GroupHolder) convertView.getTag();
        }

        groupHolder.mySwitch.setTag(getGroup(groupPosition));   //setting the tag so that the listener can use it

        //assigning values
        groupHolder.title.setText(repairItem.getTitle());
        groupHolder.mySwitch.setChecked(repairItem.isSwitched());

        return  convertView;
    }

    public int calculateTotal() {
        Log.i(TAG, "Calc: " + repairItems.toString());
        int sum = 0;
        for(RepairItem item: repairItems){
            if(item.isSwitched()) {

                sum += item.getTotal();
            }
        }

        TextView totalRepairCostText = (TextView) mContext.findViewById(R.id.totalRepairCostText);
        TextView perSquareFootText = (TextView) mContext.findViewById(R.id.perSquareFootText);
        Utility.formatCurrencyTextField(totalRepairCostText, sum);
        Utility.formatCurrencyTextField(perSquareFootText, sum / repairItems.size());
        return sum;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();

        ChildHolder childHolder;
        //if (convertView == null) {
        //creating a child for the first time
        convertView = inflater.inflate(R.layout.repair_child_item, null);

        //assign all the ui elements
        childHolder = new ChildHolder();
        childHolder.amountSpinner = (Spinner) convertView.findViewById(R.id.amountSpinner);
        childHolder.amountText = (EditText) convertView.findViewById(R.id.amountText);
        childHolder.priceText = (EditText) convertView.findViewById(R.id.priceText);
        childHolder.amountView = (TextView) convertView.findViewById(R.id.amountView);
        childHolder.repairItem = (RepairItem) getChild(groupPosition, childPosition);

        //Setting tags
        childHolder.priceText.setTag(getGroup(groupPosition)); //sets the tag so the listener can find it
        childHolder.amountText.setTag(getGroup(groupPosition)); //sets the tag so the listener can find it
        childHolder.amountSpinner.setTag(getGroup(groupPosition)); //sets the tag so the listener can find it

        convertView.setTag(childHolder);
        //setting or removing the spinner
        if(childHolder.repairItem.hasSpinner()) {
            String[] spinnerValues = new String[0];
            try {
                childHolder.repairItem.finishInit(mContext);
                spinnerValues = childHolder.repairItem.getSpinnerValues();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, spinnerValues);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                childHolder.amountSpinner.setAdapter(adapter);
                childHolder.amountSpinner.setSelection(childHolder.repairItem.getSpinnerIndex());
                childHolder.amountText.setVisibility(View.GONE);    //spinner and amount should not be displayed together
            } catch (NotFullyInitializedException e) {
                e.printStackTrace();
            }

        } else {
            childHolder.amountSpinner.setVisibility(View.GONE);
        }

        //setting visibility of the amount text manually element
        if(childHolder.repairItem.isDisplayAmount()) {
                  childHolder.amountText.setText((int)childHolder.repairItem.getAmount()+"");
        } else {
            childHolder.amountText.setVisibility(View.GONE);
        }

        //remove amount label
        if(childHolder.repairItem.hasSpinner() == false && childHolder.repairItem.isDisplayAmount() == false) {
            childHolder.amountView.setVisibility(View.GONE);
        }

        Utility.formatCurrencyTextField(childHolder.priceText, childHolder.repairItem.getPrice());

        //Assigning Handlers
        childHolder.priceText.addTextChangedListener(new PriceTextWatcher(convertView));
        childHolder.amountText.addTextChangedListener(new AmountTextWatcher(convertView));
        childHolder.amountSpinner.setOnItemSelectedListener(new AmountSpinnerWatcher(convertView));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     *
     * @return Returns true if a repair item exists with it's switch on and a total (price * amount) of 0.
     */
    public ArrayList<RepairItem> getSwitchedAndEmpty() {
        ArrayList<RepairItem> switchedAndEmpty = new ArrayList<RepairItem>();

        for(RepairItem repairItem: repairItems) {
            if(repairItem.isSwitched() && repairItem.getTotal() == 0) {
                switchedAndEmpty.add(repairItem);
            }
        }
        return switchedAndEmpty;
    }

    private class PriceTextWatcher implements TextWatcher {
        private View view;

        private PriceTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            EditText priceText = (EditText) view.findViewById(R.id.priceText);
            RepairItem repairItem = (RepairItem)priceText.getTag();

            priceText.removeTextChangedListener(this);
            //           priceText.addTextChangedListener(null);
            Utility.formatCurrencyTextField(priceText, editable);
            repairItem.setPrice(Utility.getIntValueFromCurrencyText(editable.toString()));

            Log.i(TAG, "PriceChanged: " + editable.toString() + " | " + repairItem.getTitle());
            calculateTotal();
            priceText.addTextChangedListener(this);
        }
    }

    private class AmountTextWatcher implements TextWatcher {
        private View view;

        private AmountTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            EditText amountText = (EditText) view.findViewById(R.id.amountText);
            RepairItem repairItem = (RepairItem)amountText.getTag();

            amountText.removeTextChangedListener(this);
            repairItem.setAmount(Utility.getIntValueFromCurrencyText(editable.toString()));

            Log.i(TAG, "AmountChanged: " + editable.toString() + " | " + repairItem.getTitle());
            calculateTotal();
            amountText.addTextChangedListener(this);
        }
    }

    private class AmountSpinnerWatcher implements AdapterView.OnItemSelectedListener{
        private View convertView;
        public AmountSpinnerWatcher(View convertView) {
            this.convertView  = convertView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner amountSpinner = (Spinner) convertView.findViewById(R.id.amountSpinner);
            RepairItem repairItem = (RepairItem)amountSpinner.getTag();

            repairItem.setAmount(amountSpinner.getSelectedItem().toString());
            Log.i(TAG, "Spinner Changed: " + " | " + repairItem.getTitle());
            calculateTotal();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class ChildHolder {
        public Spinner amountSpinner;
        public EditText amountText;
        public EditText priceText;
        public TextView amountView;
        public RepairItem repairItem;
    }

    private class GroupHolder {
        public TextView title;
        public Switch mySwitch;
    }
}
