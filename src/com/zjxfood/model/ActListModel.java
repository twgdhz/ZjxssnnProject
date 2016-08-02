package com.zjxfood.model;

/**
 * Created by Administrator on 2016/6/16.
 */
public class ActListModel {
    private Object Id;
    private Object Title;
    private Object AdPositionCode;
    private Object LinkUrl;
    private Object Images;
    private Object IsActive;
    private Object CreateTime;
    private Object SortCode;
    private Object Content;
    private Object IOSForm;
    private Object AndroidForm;
   public ActListModel(){}

    public Object getLinkUrl() {
        return LinkUrl;
    }

    public void setLinkUrl(Object linkUrl) {
        LinkUrl = linkUrl;
    }

    public Object getImages() {
        return Images;
    }

    public void setImages(Object images) {
        Images = images;
    }

    public Object getIsActive() {
        return IsActive;
    }

    public void setIsActive(Object isActive) {
        IsActive = isActive;
    }

    public Object getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Object createTime) {
        CreateTime = createTime;
    }

    public Object getSortCode() {
        return SortCode;
    }

    public void setSortCode(Object sortCode) {
        SortCode = sortCode;
    }

    public Object getContent() {
        return Content;
    }

    public void setContent(Object content) {
        Content = content;
    }

    public Object getIOSForm() {
        return IOSForm;
    }

    public void setIOSForm(Object IOSForm) {
        this.IOSForm = IOSForm;
    }

    public Object getAndroidForm() {
        return AndroidForm;
    }

    public void setAndroidForm(Object androidForm) {
        AndroidForm = androidForm;
    }

    public Object getAdPositionCode() {
        return AdPositionCode;
    }

    public void setAdPositionCode(Object adPositionCode) {
        AdPositionCode = adPositionCode;
    }

    public Object getTitle() {
        return Title;
    }

    public void setTitle(Object title) {
        Title = title;
    }

    public Object getId() {
        return Id;
    }

    public void setId(Object id) {
        Id = id;
    }
}
