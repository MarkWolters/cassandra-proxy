/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.powertools.dcp;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.powertools.dcp.managed.dse.CassandraManager;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CqlProxyDualWrites extends CqlProxyLogic {
    private final static Logger logger = LoggerFactory.getLogger(CqlProxyDualWrites.class);
    private CassandraManager otherCassandraManager;
    private ProxyConfiguration otherConfiguration;

    public CqlProxyDualWrites(CassandraManager cassandraManager, ProxyConfiguration configuration, Environment environment) {
        super(cassandraManager, configuration, environment);
        try {
            otherConfiguration = extractOtherConfig(configuration);
            otherCassandraManager = new CassandraManager();
            otherCassandraManager.configure(otherConfiguration);
            environment.lifecycle().manage(otherCassandraManager);
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getStackTrace());
        }
    }

    private ProxyConfiguration extractOtherConfig(ProxyConfiguration configuration) {
        return ProxyConfiguration.dualFromPrimary(configuration);
    }

    @Override
    public ResultSet read(String statement) throws IOException {
        logger.debug(String.format("Executing statement %s", statement));
        ResultSet result = cassandraManager.getSession().execute(statement);
        return result;
    }

    @Override
    public void write(String statement) throws IOException {
        logger.debug(String.format("Executing statement %s in cluster %s", statement, configuration.getClusterName()));
        cassandraManager.getSession().executeAsync(statement);
        logger.debug(String.format("Executing statement %s in cluster %s", statement, otherConfiguration.getClusterName()));
        otherCassandraManager.getSession().executeAsync(statement);
    }

}
