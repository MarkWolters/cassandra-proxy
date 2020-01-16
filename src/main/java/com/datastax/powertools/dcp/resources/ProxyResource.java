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
package com.datastax.powertools.dcp.resources;

import com.codahale.metrics.annotation.Timed;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.powertools.dcp.CqlProxyLogic;
import com.datastax.powertools.dcp.api.CassandraResponse;
import com.datastax.powertools.dcp.managed.dse.CassandraManager;
import org.glassfish.jersey.server.ManagedAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.UUID;

@Path("/proxy")
@Produces(MediaType.APPLICATION_JSON)
public class ProxyResource {

    private final CassandraManager dseManager;
    private CqlProxyLogic cdt;
    private Logger logger = LoggerFactory.getLogger(ProxyResource.class);

    public ProxyResource(CassandraManager dsManager, CqlProxyLogic cdt) {
        this.dseManager = dsManager;
        this.cdt = cdt;
    }

    //TODO: Make methods async
    //TODO: handle various error conditions

    @GET
    @Timed
    public CassandraResponse queryCassandra(@QueryParam("query") String query) {
        CassandraResponse response = null;
        UUID txId = UUID.randomUUID();
        try {
            logger.info(String.format("Executing transaction %s", txId.toString()));
            logger.debug(query);
            ResultSet rs = cdt.read(query);
            StringBuffer SB = new StringBuffer();
            Iterator<Row> it = rs.iterator();
            while (it.hasNext()) {
                SB.append(it.next().getFormattedContents());
            }
            response = new CassandraResponse(SB.toString(), 200);
            logger.info(String.format("Transaction: %s completed", txId.toString()));
        } catch (Throwable e) {
            logger.error(String.format("Throwable caught for transaction %s", txId.toString()));
            logger.error(e.getMessage(), e.getStackTrace());
            response = new CassandraResponse(null, 500);
        }
        return response;
    }

    @POST
    @Timed
    public CassandraResponse writeCassandra(String query) {
        CassandraResponse response = null;
        UUID txId = UUID.randomUUID();
        try {
            logger.info(String.format("Executing transaction %s", txId.toString()));
            logger.debug(query);
            cdt.write(query);
            response = new CassandraResponse(null, 200);
            logger.info(String.format("Transaction %s completed", txId.toString()));
        } catch (Throwable e) {
            logger.error(String.format("Throwable caught for transaction %s", txId.toString()));
            logger.error(e.getMessage(), e.getStackTrace());
            response = new CassandraResponse(null, 500);
        }
        return response;
    }

}