package com.adobe.core.raven.dto.qa;

import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

public @Data class CheckList {

    private String type;

    private String name;

    private String label;

    private String state;

    private ArrayList<Link> data;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckList checkList = (CheckList) o;
        return Objects.equals(type, checkList.type) && Objects.equals(name, checkList.name) && Objects.equals(label, checkList.label) && Objects.equals(state, checkList.state) && Objects.equals(data, checkList.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, label, state, data);
    }
}
