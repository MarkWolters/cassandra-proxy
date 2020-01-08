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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class ProxyConfiguration extends Configuration {

    //Cassandra Stuff
    @JsonProperty
    private int cqlPort = 9042;
    @JsonProperty
    private String contactPoints = "localhost";
    @JsonProperty
    private String localDC = "dc1";
    @JsonProperty
    private String cqlUserName = "cassandra";
    @JsonProperty
    private String cqlPassword = "cassandra";
    @JsonProperty
    private String keyspaceName = "dynamoks";
    @JsonProperty
    private String replicationStrategy = "{'class': 'SimpleStrategy', 'replication_factor': 1 }";
    @JsonProperty
    private String clusterName = "Test Cluster";

    //Cassandra dual writes stuff
    @JsonProperty
    private int dualCqlPort;
    @JsonProperty
    private String dualContactPoints;
    @JsonProperty
    private String dualLocalDC;
    @JsonProperty
    private String dualCqlUserName;
    @JsonProperty
    private String dualCqlPassword;
    @JsonProperty
    private String dualKeyspaceName;
    @JsonProperty
    private String dualReplicationStrategy;
    @JsonProperty
    private String dualClusterName;

    // Proxy configuration stuff
    @JsonProperty
    private ProxyType translatorImplementation = ProxyType.DUAL_WRITE;
    @JsonProperty
    private boolean dockerCassandra;

    //Cassandra Stuff getters and setters
    @JsonProperty
    public String getContactPoints() {
        return contactPoints;
    }
    @JsonProperty
    public void setContactPoints(String contactPoints) {
        this.contactPoints = contactPoints;
    }

    @JsonProperty
    public int getCqlPort() {
        return cqlPort;
    }
    @JsonProperty
    public void setCqlPort(int cqlPort) {
        this.cqlPort = cqlPort;
    }

    @JsonProperty
    public String getCqlUserName() {
        return cqlUserName;
    }
    @JsonProperty
    public void setCqlUserName(String cqlUserName) {
        this.cqlUserName = cqlUserName;
    }

    @JsonProperty
    public String getCqlPassword() {
        return cqlPassword;
    }
    @JsonProperty
    public void setCqlPassword(String cqlPassword) {
        this.cqlPassword = cqlPassword;
    }

    @JsonProperty
    public String getKeyspaceName() {
        return keyspaceName;
    }
    @JsonProperty
    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    @JsonProperty
    public String getReplicationStrategy() {
        return replicationStrategy;
    }
    @JsonProperty
    public void setReplicationStrategy(String replicationStrategy) {
        this.replicationStrategy = replicationStrategy;
    }

    @JsonProperty
    public String getLocalDC() {
        return localDC;
    }
    @JsonProperty
    public void setLocalDC(String localDC) { this.localDC = localDC; }

    @JsonProperty
    public String getClusterName() { return clusterName; }
    @JsonProperty
    public void setClusterName(String clusterName) { this.clusterName = clusterName; }

    //Cassandra dual writes stuff getters and setters
    @JsonProperty
    public String getDualContactPoints() {
        return dualContactPoints;
    }
    @JsonProperty
    public void setDualContactPoints(String dualContactPoints) {
        this.dualContactPoints = dualContactPoints;
    }

    @JsonProperty
    public int getDualCqlPort() {
        return dualCqlPort;
    }
    @JsonProperty
    public void setDualCqlPort(int dualCqlPort) {
        this.dualCqlPort = dualCqlPort;
    }

    @JsonProperty
    public String getDualCqlUserName() {
        return dualCqlUserName;
    }
    @JsonProperty
    public void setDualCqlUserName(String dualCqlUserName) {
        this.dualCqlUserName = dualCqlUserName;
    }

    @JsonProperty
    public String getDualCqlPassword() {
        return dualCqlPassword;
    }
    @JsonProperty
    public void setDualCqlPassword(String dualCqlPassword) {
        this.dualCqlPassword = dualCqlPassword;
    }

    @JsonProperty
    public String getDualKeyspaceName() {
        return dualKeyspaceName;
    }
    @JsonProperty
    public void setDualKeyspaceName(String dualKeyspaceName) {
        this.dualKeyspaceName = dualKeyspaceName;
    }

    @JsonProperty
    public String getDualReplicationStrategy() {
        return dualReplicationStrategy;
    }
    @JsonProperty
    public void setDualReplicationStrategy(String dualReplicationStrategy) {
        this.dualReplicationStrategy = replicationStrategy;
    }

    @JsonProperty
    public String getDualLocalDC() {
        return dualLocalDC;
    }
    @JsonProperty
    public void setDualLocalDC(String dualLocalDC) { this.dualLocalDC = dualLocalDC; }

    @JsonProperty
    public String getDualClusterName() {
        return dualClusterName;
    }
    @JsonProperty
    public void setDualClusterName(String dualClusterName) {
        this.dualClusterName = dualClusterName;
    }

    // Proxy configuration stuff getters and setters
    @JsonProperty
    public ProxyType getTranslatorImplementation() {
        return this.translatorImplementation;
    }
    @JsonProperty
    public void setTranslatorImplementation(ProxyType type) { this.translatorImplementation = type; }

    @JsonProperty
    public boolean isDockerCassandra() {
        return dockerCassandra;
    }
    @JsonProperty
    public void setDockerCassandra(boolean dockerCassandra) {
        this.dockerCassandra = dockerCassandra;
    }

    public static ProxyConfiguration dualFromPrimary(ProxyConfiguration other) {
        ProxyConfiguration conf = new ProxyConfiguration();

        conf.setCqlPort(other.dualCqlPort);
        conf.setContactPoints(other.dualContactPoints);
        conf.setLocalDC(other.dualLocalDC);
        conf.setCqlUserName(other.dualCqlUserName);
        conf.setCqlPassword(other.dualCqlPassword);
        conf.setKeyspaceName(other.dualKeyspaceName);
        conf.setReplicationStrategy(other.dualReplicationStrategy);
        conf.setClusterName(other.dualClusterName);

        conf.setTranslatorImplementation(other.getTranslatorImplementation());
        conf.setDockerCassandra(other.isDockerCassandra());

        return conf;
    }

}
