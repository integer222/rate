package ru.pap.rate.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 09.11.16.
 */

public class QuotesContainer extends BaseModel {

    private int count;
    private Date created;
    private String lang;

    private List<Quote> mQuotes;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<Quote> getQuotes() {
        if (mQuotes == null) {
            mQuotes = new ArrayList<>();
        }
        return mQuotes;
    }
}
