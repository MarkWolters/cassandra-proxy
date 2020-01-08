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
package com.datastax.powertools.dcp.managed.dse;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.powertools.dcp.ProxyConfiguration;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CassandraManager implements Managed {
    private final static Logger logger = LoggerFactory.getLogger(CassandraManager.class);

    public CqlSession getSession() {
        return session;
    }

    private ProxyConfiguration config;
    private String keyspaceName;
    private CqlSession session;

    public void configure(ProxyConfiguration config) {
        this.config = config;
        this.keyspaceName = config.getKeyspaceName();
    }

    public void start() {
        logger.info("Contact points {}", config.getContactPoints());

        CqlSessionBuilder builder = CqlSession.builder()
                .addContactPoints(Arrays.stream(config.getContactPoints().split(","))
                        .map(s -> new InetSocketAddress(s, config.getCqlPort()))
                        .collect(Collectors.toList()))
                .withLocalDatacenter(config.getLocalDC());

        if (config.getCqlUserName() != null)
            builder.withAuthCredentials(config.getCqlUserName(), config.getCqlPassword());

        if (config.isDockerCassandra()) {
            logger.info("Docker cassandra enabled in the yaml.");
            logger.info("Attempting to stand up container.");
            DockerHelper dh = new DockerHelper();
            dh.startDSE();

            //TODO
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        session = builder.build();
    }

    public void stop() throws Exception {
        session.close();

        if (config.isDockerCassandra()) {
            DockerHelper dh = new DockerHelper();
            dh.stopDSE();
        }
    }

    public String getKeyspaceName() {
        return keyspaceName;
    }

}
