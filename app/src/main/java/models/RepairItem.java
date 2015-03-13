package models;

import android.content.Context;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

import Exceptions.NotFullyInitializedException;
import Utilities.Utility;
import ind.habanero.realestatecalculator.R;

/**
 * Created by michaelborglin on 2/18/15.
 */
public class RepairItem {

    private boolean mSwitched;
    private double mAmount;
    private int mPrice;
    private boolean mDisplaySpinner;
    private boolean mDisplayAmount;
    private String mTitle;
    private String[] mSpinnerValues;
    private HashBiMap mSpinnerMap;  //Used to convert Spinner string into Amounts
    private Type mType;
    private int m_id;
    private boolean mFullyInitialized;

    public RepairItem(Context context, Type type, int amount) {
        setM_id(-1);
        setType(type);
        setTitle(type);
        setSwitched(false);     //default unless set otherwise
        setDisplayAmount(true); //default unless set otherwise
        setType(type, amount);
        setPrice(getDefaultPrice(context, type));
        setSpinnerValues(context, type);
        setSpinnerMap(context, type);
        setFullyInitialized(true);

    }

    /**
     * Spinner values and map values have not been set
     */
    public RepairItem(int id, String _type, int price, String _amount, int switched) {

        Type type = Type.valueOf(_type);
        double amount = Double.valueOf(_amount).doubleValue();
        setM_id(id);
        setDisplayAmount(true); //default unless set otherwise
        setType(type, (int)amount);
        setAmount(amount);  //overrides the above amount
        setTitle(type);
        setSwitched(switched == 1);
        setPrice(price);
        setFullyInitialized(false);
    }

    /**
     * Finishes initialization for spinners
     * @param context
     * the context of the application
     */
    public void finishInit(Context context) {
        if(getFullyInit() == false) {
            setSpinnerValues(context, mType);
            setSpinnerMap(context, mType);
            setFullyInitialized(true);
        }
    }

    private void setType(Type type, int amount) {
        switch (type) {
            //the following are square foot based items
            case OUTSIDE_PAINT:
            case INSIDE_PAINT:
            case REMOVE_POPCORN:
            case TEXTURE_WALLS:
            case BASE_BOARD_MOULDING:
            case FLOORING:
            case ROOF:
                setDisplaySpinner(false);
                setAmount(amount);
                break;
            //The Following are simple switch items
            case ELECTRICAL:
            case PLUMBING:
            case GARAGE_DOOR:
            case FRONT_DOOR:
            case BACK_DOOR:
            case WATER_HEATER:
            case HVAC:
            case POOL:
            case DUMPSTER:
            case OTHER:
            case PERMITS:
                setDisplaySpinner(false);
                setDisplayAmount(false);
                setAmount(1);
                break;
            //the following are spinner items
            case LANDSCAPING:
                setDisplaySpinner(true);
                setAmount(amount);
                break;
            case WINDOWS:
                setDisplaySpinner(true);
                setAmount(amount);
                break;
            case LIGHTS:
                setDisplaySpinner(true);
                //TODO This amount should be a special calculation based on square footage
                setAmount(amount);
                break;
            case BEDROOMS:
                setDisplaySpinner(true);
                setAmount(amount);
                break;
            case BATHROOMS:
                setDisplaySpinner(true);
                setAmount(amount);
                break;
            case KITCHEN:
                setDisplaySpinner(true);
                setAmount(amount);
                break;
        }
        setType(type);
    }

    public void setType(Type type) {
        mType = type;
    }

    public Type getType() {
        return mType;
    }

    private void setSpinnerMap(Context context, Type type) {

        HashBiMap dictionary = HashBiMap.create();
        String[] tags = new String[0];

        switch (type) {
            case KITCHEN:
                tags = context.getResources().getStringArray(R.array.kitchen_map);
                break;
        }
        for(String tag : tags) {
            String[] pair = tag.split(":");

            String key = pair[0];
            String value = pair[1];

            dictionary.put(key, value);
        }

        dictionary.inverse();
        setSpinnerMap(dictionary);
    }

    private void setAmount(double amount) {
        mAmount = amount;
    }

    public int get_id() {
        return m_id;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int mId) {
        this.m_id = mId;
    }

    private void setSpinnerMap(HashBiMap spinnerMap) {
        mSpinnerMap = spinnerMap;
    }

    private void setSpinnerValues(String[] spinnerValues) {
        mSpinnerValues = spinnerValues;
    }

    private void setFullyInitialized(boolean fullyInitialized) {
        mFullyInitialized = fullyInitialized;
    }

    public boolean isFullyInitialized() {
        return mFullyInitialized;
    }

    public int getSpinnerIndex() {
        switch (getType()) {
            case BATHROOMS:
                return Utility.getIndexOfValue(mSpinnerValues, getAmount());
            case KITCHEN:
                BiMap inverse = mSpinnerMap.inverse();
                String value = (String) inverse.get(getAmount() + "");
                int index = Utility.getIndexOfValue(mSpinnerValues, value);
                return index;
            default:
                return Utility.getIndexOfValue(mSpinnerValues, (int) getAmount());
        }
    }


    public static enum Type {
        OUTSIDE_PAINT,
        INSIDE_PAINT,
        REMOVE_POPCORN,
        TEXTURE_WALLS,
        BASE_BOARD_MOULDING,
        FLOORING,
        ELECTRICAL,
        PLUMBING,
        ROOF,
        FRONT_DOOR,
        BACK_DOOR,
        WATER_HEATER,
        HVAC,
        POOL,
        DUMPSTER,
        PERMITS,
        OTHER,
        LANDSCAPING,
        WINDOWS,
        LIGHTS,
        BEDROOMS,
        BATHROOMS,
        GARAGE_DOOR,
        KITCHEN
    }


    @Override
    public String toString() {
        return "RepairItem{" +
                "mTitle='" + mTitle + '\'' +
                ", mSwitched=" + mSwitched +
                ", mAmount=" + mAmount +
                ", mPrice=" + mPrice +
                '}';
    }

    public void flipSwitch() {
        setSwitched(!mSwitched);
    }

    public void setPrice(int price) {
        this.mPrice = price;
    }

    private void setDisplayAmount(boolean displayAmount) {
        mDisplayAmount = displayAmount;
    }

    public boolean isDisplayAmount() {
        return mDisplayAmount;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isSwitched() {
        return mSwitched;
    }

    public double getAmount() {
        return mAmount;
    }

    public boolean hasSpinner() {
        return mDisplaySpinner;
    }

    public int getPrice() {
        return mPrice;
    }

    private void setDisplaySpinner(boolean mDisplaySpinner) {
        this.mDisplaySpinner = mDisplaySpinner;
    }

    public void setAmount(int amount) {
        this.mAmount = amount;
    }

    public void setAmount(String key) {
        double convertedValue = 0;
        switch (mType) {
            case BATHROOMS:
                //convertedValue = (int)Math.round(Double.valueOf(key).doubleValue()) * 10;
                convertedValue = Double.valueOf(key).doubleValue();
                break;
            case KITCHEN:
                convertedValue = Double.valueOf((String) mSpinnerMap.get(key)).doubleValue();
                //convertedValue = (int)Math.round(Double.valueOf((String) mSpinnerMap.get(key)).doubleValue()) * 10;
                break;
            default:
                convertedValue = Integer.valueOf(key).intValue();
                break;
        }

        setAmount(convertedValue);
    }

    public void setTitle(Type type) {
        switch(type) {
            case OUTSIDE_PAINT:
                mTitle = "Outside Paint";
                break;
            case INSIDE_PAINT:
                mTitle = "Inside Paint";
                break;
            case REMOVE_POPCORN:
                mTitle = "Remove Popcorn";
                break;
            case TEXTURE_WALLS:
                mTitle = "Texture Walls";
                break;
            case BASE_BOARD_MOULDING:
                mTitle = "Base Board Moulding";
                break;
            case FLOORING:
                mTitle = "Flooring";
                break;
            case ELECTRICAL:
                mTitle = "Electrical";
                break;
            case PLUMBING:
                mTitle = "Plumbing";
                break;
            case GARAGE_DOOR:
                mTitle = "Garage Door";
                break;
            case ROOF:
                mTitle = "Roof";
                break;
            case FRONT_DOOR:
                mTitle = "Front Door";
                break;
            case BACK_DOOR:
                mTitle = "Back Door";
                break;
            case WATER_HEATER:
                mTitle = "Water Heater";
                break;
            case HVAC:
                mTitle = "HVAC";
                break;
            case POOL:
                mTitle = "Pool";
                break;
            case DUMPSTER:
                mTitle = "Dumpster";
                break;
            case PERMITS:
                mTitle = "Fees";
                break;
            case OTHER:
                mTitle = "Other";
                break;
            case LANDSCAPING:
                mTitle = "Landscaping";
                break;
            case WINDOWS:
                mTitle = "Windows";
                break;
            case LIGHTS:
                mTitle = "Lights/Switches";
                break;
            case BEDROOMS:
                mTitle = "Bedrooms";
                break;
            case BATHROOMS:
                mTitle = "Bathrooms";
                break;
            case KITCHEN:
                mTitle = "Kitchen";
                break;
            default:
                mTitle = "Title Not Found";
        }
    }

    /**
     *
     * @param context
     * Context for this application
     * @param type
     * The type of a RepairItem
     * @return
     * The default price
     */
    private int getDefaultPrice(Context context, Type type) {

        Map dictionary = new HashMap();

        String[] tags = context.getResources().getStringArray(R.array.default_prices);
        for(String tag : tags) {
            String[] pair = tag.split(":");

            String key = pair[0].toLowerCase();
            String value = pair[1];

            dictionary.put(key, value);
        }

        return Utility.getIntValueFromCurrencyText((String) dictionary.get(type.name().toLowerCase()));
    }
    public String[] getSpinnerValues() throws NotFullyInitializedException {
        if(getFullyInit() == false) {
            throw new NotFullyInitializedException();
        }
        return mSpinnerValues;
    }

    private boolean getFullyInit() {
        return mFullyInitialized;
    }

    private void setSwitched(boolean switched) {
        mSwitched = switched;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepairItem that = (RepairItem) o;

        if (mAmount != that.mAmount) return false;
        if (mDisplayAmount != that.mDisplayAmount) return false;
        if (mDisplaySpinner != that.mDisplaySpinner) return false;
        if (mPrice != that.mPrice) return false;
        if (mSwitched != that.mSwitched) return false;
        if (!mTitle.equals(that.mTitle)) return false;

        return true;
    }

    /**
     *
     * @return Returns the total cost of this RepairItem.
     */
    public double getTotal() {
        if(isSwitched()) {
            return getPrice() * getAmount();
        }else{
            return 0;
        }

    }

    /**
     *
     * @param context
     * The application context
     * @param type
     * The type of a RepairItem
     * @return
     * The spinner values for this type
     */

    private void setSpinnerValues(Context context, Type type) {
        String [] spinnerValues = new String[0];
        switch (type) {
            case LANDSCAPING:
                spinnerValues = context.getResources().getStringArray(R.array.bedroom_values);
                break;
            case WINDOWS:
                spinnerValues = context.getResources().getStringArray(R.array.bedroom_values);
                break;
            case LIGHTS:
                //TODO This amount should be a special calculation based on square footage
                spinnerValues = context.getResources().getStringArray(R.array.bedroom_values);
                break;
            case BEDROOMS:
                spinnerValues = context.getResources().getStringArray(R.array.bedroom_values);
                break;
            case BATHROOMS:
                spinnerValues = context.getResources().getStringArray(R.array.bathroom_values);
                break;
            case KITCHEN:
                spinnerValues = context.getResources().getStringArray(R.array.kitchen_values);
                break;
        }
        setSpinnerValues(spinnerValues);
    }
}
