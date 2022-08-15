package com.helloboss.money365.userguide;

import com.helloboss.money365.R;

public class UserGuideModel {

    String title;
    String link;
    int iconId;

    public UserGuideModel(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public UserGuideModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {

        switch (iconId) {

            case "youtube":
                this.iconId = R.drawable.youtube;
            break;

            case "web":
                this.iconId = R.drawable.web;
                break;
        }
    }
}
