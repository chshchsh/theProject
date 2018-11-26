package com.jct.bd.theproject.model.backend;

import com.jct.bd.theproject.model.datasource.FireBase_DB_manager;

public class FactoryBackend {

    public static FireBase_DB_manager getInstance() {
        return new FireBase_DB_manager();
    }

    }
