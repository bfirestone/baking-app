package com.bfirestone.udacity.cookbook.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("unused")
public class Ingredient implements Parcelable {

    @Json(name = "quantity")
    private String quantity;
    @Json(name = "measure")
    private String measurement;
    @Json(name = "ingredient")
    private String name;
    public final static Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {

        @SuppressWarnings({"unchecked"})
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return (new Ingredient[size]);
        }

    };
    private final static long serialVersionUID = -1044070802655570954L;

    private Ingredient(Parcel in) {
        this.quantity = ((String) in.readValue((String.class.getClassLoader())));
        this.measurement = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Ingredient() {
    }

    /**
     * @param measurement  measurement name i.e CUP/TEASPOON
     * @param name     ingredient name
     * @param quantity quantity of ingredient
     */
    public Ingredient(String quantity, String measurement, String name) {
        super();
        this.quantity = quantity;
        this.measurement = measurement;
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("quantity", quantity)
                .append("measurement", measurement)
                .append("name", name)
                .toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(quantity);
        dest.writeValue(measurement);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}
