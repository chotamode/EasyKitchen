package com.easykitchen.project.model;

public enum ItemCategory {
    VEGAN("VEGAN"), LOW_CALORIES("LOW_CALORIES"), MASS_GAIN("MASS_GAIN");

    private final String name;

    ItemCategory(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
