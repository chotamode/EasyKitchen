package com.easykitchen.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Address extends AbstractEntity{
    private String Street;
    private String Number;
    private String ZIP;
    private String City;
}
