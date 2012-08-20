package org.cunxin.api.service;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Environment;
import org.cunxin.api.configuration.CharityEventConfiguration;

public class CharityEventService extends Service<CharityEventConfiguration> {

    @Override
    protected void initialize(CharityEventConfiguration charityEventConfiguration, Environment environment) throws Exception {

    }
}
