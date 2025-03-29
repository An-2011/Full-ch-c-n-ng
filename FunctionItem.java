package com.example.ungdungcoxuongkhop;

public class FunctionItem {
    private String title;
    private int iconResId;
    private Class<?> activityClass;

    public FunctionItem(String title, int iconResId, Class<?> activityClass) {
        this.title = title;
        this.iconResId = iconResId;
        this.activityClass = activityClass;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }
}
