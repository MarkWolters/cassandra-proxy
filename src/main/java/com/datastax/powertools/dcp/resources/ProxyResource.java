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

import com.datastax.oss.driver.api.core.cql.ResultSet;
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
import java.util.UUID;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ProxyResource {

    private final CassandraManager dseManager;
    private CqlProxyLogic cdt;
    private Logger logger = LoggerFactory.getLogger(ProxyResource.class);

    public ProxyResource(CassandraManager dsManager, CqlProxyLogic cdt) {
        this.dseManager = dsManager;
        this.cdt = cdt;
    }

    @POST
    @ManagedAsync
    @Consumes("text/plain")
    @Produces("application/json")
    public void asyncRequestHandler(@Suspended final AsyncResponse asyncResponse, @Context HttpHeaders headers, @HeaderParam("action") String action, String payload) {
        //TODO: This should have a bunch of different methods split out by transaction type
        // For now it's basically a pass-through
        CassandraResponse response = null;
        UUID txId = UUID.randomUUID();
        try {
            logger.info("Executing transaction %s", txId.toString());
            if (action.equalsIgnoreCase("WRITE")) {
                cdt.write(payload);
                response = new CassandraResponse(null, 200);
            } else if (action.equalsIgnoreCase("READ")) {
                ResultSet rs = cdt.read(payload);
                response = new CassandraResponse(rs, 200);
            }
            logger.info("Transaction %s completed", txId.toString());
        } catch (Throwable e) {
            logger.error("Throwable caught for transaction %s", txId.toString());
            logger.error(e.getMessage(), e.getStackTrace());
        }
        finally {
            //TODO: Just throwing any error into the "500" bucket for now...
            if (response == null) {
                throw new WebApplicationException("Internal Error", 500);
            } else if (response.getStatusCode() == 200) {
                Response httpResponse;
                Response.ResponseBuilder responseBuilder = Response.ok(response.getResult()).status(response.getStatusCode());
                httpResponse = responseBuilder.build();
                asyncResponse.resume(httpResponse);
            } else {
                throw new WebApplicationException(response.getError(), response.getStatusCode());
            }
        }
    }
}