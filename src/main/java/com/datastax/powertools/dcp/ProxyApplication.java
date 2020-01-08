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

import com.datastax.powertools.dcp.managed.dse.CassandraManager;
import com.datastax.powertools.dcp.resources.ProxyResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

//TODO: Extend this beyond setting up a restful interface to be able to
//      simulate a cluster connection from the client driver perspective

public class ProxyApplication extends Application<ProxyConfiguration> {

    public static void main(String[] args) throws Exception {
        new ProxyApplication().run(args);
    }

    @Override
    public String getName() {
        return "Cql-Cassandra-Proxy";
    }

    @Override
    public void initialize(Bootstrap<ProxyConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(ProxyConfiguration configuration,
                    Environment environment) {

        //DataStax
        CassandraManager dseManager = new CassandraManager();
        dseManager.configure(configuration);
        environment.lifecycle().manage(dseManager);

        CqlProxyLogic cdt;
        ProxyType translatorType = configuration.getTranslatorImplementation();
        if (translatorType == ProxyType.DUAL_WRITE) {
            cdt = new CqlProxyDualWrites(dseManager, configuration, environment);
        } else {
            //TODO: Implement other types
            cdt = new CqlProxyDualWrites(dseManager, configuration, environment);
        }

        final ProxyResource dcProxyResource = new ProxyResource(dseManager, cdt);
        environment.jersey().register(dcProxyResource);
    }

}
