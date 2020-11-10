package com.easykitchen.project.model;

public enum Unit {
    KG("KILOGRAM"), G("GRAM"), PCS("PIECE");

    private final String name;

    Unit(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
