package com.github.galimru.whatsappbot.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Update {

    @JsonProperty(value = "old")
    protected String oldItem;

    @JsonProperty(value = "new")
    protected String newItem;

    public String getOldItem() {
        return oldItem;
    }

    public void setOldItem(String oldItem) {
        this.oldItem = oldItem;
    }

    public String getNewItem() {
        return newItem;
    }

    public void setNewItem(String newItem) {
        this.newItem = newItem;
    }
}
