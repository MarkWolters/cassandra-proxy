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

import java.io.IOException;

public abstract class CqlProxyLogic {
    protected final CassandraManager cassandraManager;
    protected ProxyConfiguration configuration;
    protected Environment environment;

    public CqlProxyLogic(CassandraManager cassandraManager, ProxyConfiguration configuration, Environment environment) {
        this.cassandraManager = cassandraManager;
        this.configuration = configuration;
        this.environment = environment;
    }

    //TODO: This should contain all allowable actions as different methods (i.e. insert(),update(),select(),
    // etc.  For now just implementing a generic "read" and a generic "write"
    public abstract ResultSet read(String payload) throws IOException;

    public abstract void write(String payload) throws IOException;

}
