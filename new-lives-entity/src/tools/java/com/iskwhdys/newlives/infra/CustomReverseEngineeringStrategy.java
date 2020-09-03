package com.iskwhdys.newlives.infra;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

public class CustomReverseEngineeringStrategy extends DelegatingReverseEngineeringStrategy {

    private static final String CLASS_SUFFIX = "Entity";

    public CustomReverseEngineeringStrategy(final ReverseEngineeringStrategy delegate) {
        super(delegate);
    }

    @Override
    public String tableToClassName(final TableIdentifier tableIdentifier) {
        return super.tableToClassName(tableIdentifier) + CLASS_SUFFIX;
    }
}
