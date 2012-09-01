package org.cunxin.api.module;

import com.google.inject.AbstractModule;
import org.cunxin.api.adapter.AliPayDonationAdapter;
import org.cunxin.app.dao.CunxinDonationDao;
import org.cunxin.app.service.CunxinDonationService;

public class CunxinServiceModule extends AbstractModule {
    protected void configure() {
        bind(CunxinDonationDao.class).asEagerSingleton();
        bind(AliPayDonationAdapter.class).asEagerSingleton();
        bind(CunxinDonationService.class).asEagerSingleton();
    }
}
