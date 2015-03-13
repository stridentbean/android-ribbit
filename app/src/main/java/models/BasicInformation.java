package models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stridentbean on 2/7/2015.
 */
public class BasicInformation {
    public static final String TAG = BasicInformation.class.getSimpleName();

    String mId;
    String mFirstName;
    String mLastName;
    String mLLC;
    String mPhone;
    String mEmail;
    String mAddressLine1;
    String mAddressLine2;
    String mCity;
    String mState;
    int mZip;
    private String mAddressId;

    @Override
    public String toString() {
        return "BasicInformation{" +
                "id='" + mId + '\'' +
                "mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mLLC='" + mLLC + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mAddressLine1='" + mAddressLine1 + '\'' +
                ", mAddressLine2='" + mAddressLine2 + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mState='" + mState + '\'' +
                ", mZip=" + mZip +
                '}';
    }

    public BasicInformation(String firstName, String lastName, String LLC, String phone, String email, String addressLine1, String addressLine2, String city, String state, int zip) {
        setId("");
        setFirstName(firstName);
        setLastName(lastName);
        setLLC(LLC);
        setPhone(phone);
        setEmail(email);
        setAddressId("");
        setAddressLine1(addressLine1);
        setAddressLine2(addressLine2);
        setCity(city);
        setState(state);
        setZip(zip);
    }
    public BasicInformation(String id, String firstName, String lastName, String LLC, String phone, String email, String addressId, String addressLine1, String addressLine2, String city, String state, int zip) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setLLC(LLC);
        setPhone(phone);
        setEmail(email);
        setAddressId(addressId);
        setAddressLine1(addressLine1);
        setAddressLine2(addressLine2);
        setCity(city);
        setState(state);
        setZip(zip);
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName.trim();
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName.trim();
    }

    public String getLLC() {
        return mLLC;
    }

    public void setLLC(String LLC) {
        mLLC = LLC.trim();
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone.trim();
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email.trim();
    }

    public String getAddressLine1() {
        return mAddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        mAddressLine1 = addressLine1.trim();
    }

    public String getAddressLine2() {
        return mAddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        mAddressLine2 = addressLine2.trim();
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city.trim();
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state.trim();
    }

    public int getZip() {
        return mZip;
    }

    public void setZip(int zip) {
        mZip = zip;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("firstName", getFirstName());
            jsonObject.put("lastName", getLastName());
            jsonObject.put("LLC", getLLC());
            jsonObject.put("phone", getPhone());
            jsonObject.put("email", getEmail());
            jsonObject.put("addressId", getAddressId());
            jsonObject.put("addressLine1", getAddressLine1());
            jsonObject.put("addressLine2", getAddressLine2());
            jsonObject.put("city", getCity());
            jsonObject.put("state", getState());
            jsonObject.put("zip", getZip());
        } catch (JSONException e) {
            Log.e(TAG, "error in Write", e);
        }

        Log.i(TAG, jsonObject.toString());
        return jsonObject;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setAddressId(String addressId) {
        mAddressId = addressId;
    }

    public String getAddressId() {
        return mAddressId;
    }
}
