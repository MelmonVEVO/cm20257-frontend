package com.cm20257.frontend.recipePage;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

    private int id;
    private String title, time, ingredients, imgURL, url;

    public Recipe(int id, String title, String time, String ingredients, String imgURL, String url) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.ingredients = ingredients;
        this.imgURL = imgURL;
        this.url = url;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        title = in.readString();
        time = in.readString();
        ingredients = in.readString();
        imgURL = in.readString();
        url = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String returnAll() {
        return this.id + " " + this.title + " "  + this.time + " "  + this.ingredients + " "  + this.imgURL + " "  + this.url + " " ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(time);
        dest.writeString(ingredients);
        dest.writeString(imgURL);
        dest.writeString(url);
    }
}
