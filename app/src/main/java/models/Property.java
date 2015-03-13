package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stridentbean on 2/7/2015.
 */
public class Property implements Parcelable {

    int _id;
    String mAddressLine1;
    String mAddressLine2;
    String mCity;
    String mState;
    String mZip;
    int mAskingPrice;
    String mBedrooms;
    String mBathrooms;
    int mSquareFootage;
    int mYearBuilt;
    int mADOM;
    long mCreateDate;
    int mARV;
    int mProfit;
    int mMoneyCosts;
    int mTransferTaxes;
    int mClosingCosts;
    private int mRepairEstimate;

    public Property(int _id, String addressLine1, String addressLine2, String city, String state, String zip, int askingPrice, String bedrooms, String bathrooms, int squareFootage, int yearBuilt, int ADOM, long createDate) {
        setId(_id);
        setAddressLine1(addressLine1);
        setAddressLine2(addressLine2);
        setCity(city);
        setState(state);
        setZip(zip);
        setAskingPrice(askingPrice);
        setBedrooms(bedrooms);
        setBathrooms(bathrooms);
        setSquareFootage(squareFootage);
        setYearBuilt(yearBuilt);
        setADOM(ADOM);
        setmCreateDate(createDate);
        setARV(-1);
        setProfit(-1);
        setMoneyCosts(-1);
        setTransferTaxes(-1);
        setClosingCosts(-1);
        setRepairEstimate(-1);

    }
    public Property(String addressLine1, String addressLine2, String city, String state, String zip, int askingPrice, String bedrooms, String bathrooms, int squareFootage, int yearBuilt, int ADOM) {
        setId(-1);
        setAddressLine1(addressLine1);
        setAddressLine2(addressLine2);
        setCity(city);
        setState(state);
        setZip(zip);
        setAskingPrice(askingPrice);
        setBedrooms(bedrooms);
        setBathrooms(bathrooms);
        setSquareFootage(squareFootage);
        setYearBuilt(yearBuilt);
        setADOM(ADOM);
        setARV(-1);
        setProfit(-1);
        setMoneyCosts(-1);
        setTransferTaxes(-1);
        setClosingCosts(-1);
        setRepairEstimate(-1);
    }

    public Property(int _id, String addressLine1, String addressLine2, String city, String state, String zip, int askingPrice, String bedrooms, String bathrooms, int squareFootage, int yearBuilt, int ADOM, long createdDate, int ARV, int profit, int moneyCosts, int transferTaxes, int closingCosts) {
        setId(_id);
        setAddressLine1(addressLine1);
        setAddressLine2(addressLine2);
        setCity(city);
        setState(state);
        setZip(zip);
        setAskingPrice(askingPrice);
        setBedrooms(bedrooms);
        setBathrooms(bathrooms);
        setSquareFootage(squareFootage);
        setYearBuilt(yearBuilt);
        setADOM(ADOM);
        setmCreateDate(createdDate);
        setARV(ARV);
        setProfit(profit);
        setMoneyCosts(moneyCosts);
        setTransferTaxes(transferTaxes);
        setClosingCosts(closingCosts);
    }

    public Property(Parcel source) {

        setId(source.readInt());
        setAddressLine1(source.readString());
        setAddressLine2(source.readString());
        setCity(source.readString());
        setState(source.readString());
        setZip(source.readString());
        setAskingPrice(source.readInt());
        setBedrooms(source.readString());
        setBathrooms(source.readString());
        setSquareFootage(source.readInt());
        setYearBuilt(source.readInt());
        setADOM(source.readInt());
        setmCreateDate(source.readLong());
        setARV(source.readInt());
        setProfit(source.readInt());
        setMoneyCosts(source.readInt());
        setTransferTaxes(source.readInt());
        setClosingCosts(source.readInt());
        setRepairEstimate(source.readInt());

    }

    public String getAddressLine1() {
        return mAddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        mAddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return mAddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        mAddressLine2 = addressLine2;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String zip) {
        mZip = zip;
    }

    public int getAskingPrice() {
        return mAskingPrice;
    }

    public void setAskingPrice(int askingPrice) {
        mAskingPrice = askingPrice;
    }

    public String getBedrooms() {
        return mBedrooms;
    }

    public void setBedrooms(String bedrooms) {
        mBedrooms = bedrooms;
    }

    public String getBathrooms() {
        return mBathrooms;
    }

    public void setBathrooms(String bathrooms) {
        mBathrooms = bathrooms;
    }

    public int getSquareFootage() {
        return mSquareFootage;
    }

    public void setSquareFootage(int squareFootage) {
        mSquareFootage = squareFootage;
    }

    public int getYearBuilt() {
        return mYearBuilt;
    }

    public void setYearBuilt(int yearBuilt) {
        mYearBuilt = yearBuilt;
    }

    public int getADOM() {
        return mADOM;
    }

    public void setADOM(int ADOM) {
        mADOM = ADOM;
    }
    public int getARV() {
        return mARV;
    }

    public void setARV(int mARV) {

        this.mARV = mARV;
    }

    public int getProfit() {
        return mProfit;
    }

    public void setProfit(int mProfit) {
        this.mProfit = mProfit;
    }

    public int getMoneyCosts() {
        return mMoneyCosts;
    }

    public void setMoneyCosts(int mMoneyCosts) {
        this.mMoneyCosts = mMoneyCosts;
    }

    public int getTransferTaxes() {
        return mTransferTaxes;
    }

    public void setTransferTaxes(int mTransferCosts) {
        this.mTransferTaxes = mTransferCosts;
    }

    public int getClosingCosts() {
        return mClosingCosts;
    }

    public void setClosingCosts(int mClosingCosts) {
        this.mClosingCosts = mClosingCosts;
    }


    public void setId(int id) {
        this._id = id;
    }

    public int get_id() {
        return _id;
    }

    public long getmCreateDate() {
        return mCreateDate;
    }

    public void setmCreateDate(long mCreateDate) {
        this.mCreateDate = mCreateDate;
    }

    @Override
    public String toString() {

        //return a truncated version if it is too long
        if(toStringLong().length() > 28) {
            return toStringLong().substring(0, 25) + "...";
        }
        return toStringLong();
    }


    public String toStringLong() {
        return (mAddressLine1.trim() + " " + mAddressLine2 + " ").trim() +
                ", " + mCity;
    }


    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(_id);
        dest.writeString(mAddressLine1);
        dest.writeString(mAddressLine2);
        dest.writeString(mCity);
        dest.writeString(mState);
        dest.writeString(mZip);
        dest.writeInt(mAskingPrice);
        dest.writeString(mBedrooms);
        dest.writeString(mBathrooms);
        dest.writeInt(mSquareFootage);
        dest.writeInt(mYearBuilt);
        dest.writeInt(mADOM);
        dest.writeLong(mCreateDate);
        dest.writeInt(mARV);
        dest.writeInt(mProfit);
        dest.writeInt(mMoneyCosts);
        dest.writeInt(mTransferTaxes);
        dest.writeInt(mClosingCosts);
        dest.writeInt(mRepairEstimate);

    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    public boolean hasARV() {
        return getARV() != -1;
    }

    public boolean hasProfit() {
        return getProfit() != -1;
    }

    public boolean hasMoneyCosts() {
        return getMoneyCosts() != -1;
    }

    public boolean hasTransferTaxes() {
        return getTransferTaxes() != -1;
    }

    public boolean hasClosingCosts() {
        return getClosingCosts() != -1;
    }

    public void setRepairEstimate(int repairEstimate) {
        mRepairEstimate = repairEstimate;
    }

    public int getRepairEstimate() {
        return mRepairEstimate;
    }
}
