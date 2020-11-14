package com.easykitchen.project.dao;

import org.springframework.stereotype.Repository;
import com.easykitchen.project.model.Address;
import com.easykitchen.project.dao.BaseDao;

@Repository
public class AddressDao extends BaseDao<Address> {
    protected AddressDao() {
        super(Address.class);
    }
}

