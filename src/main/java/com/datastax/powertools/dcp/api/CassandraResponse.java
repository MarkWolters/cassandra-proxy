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
package com.datastax.powertools.dcp.api;

import com.datastax.oss.driver.api.core.cql.ResultSet;

public class CassandraResponse {

    private String result;
    public void setResult(String result) {
        this.result = result;
    }
    public String getResult() {
        return result;
    }

    private String error;
    public void setError(String error) {
        this.error = error;
    }
    public boolean hasError(){
        return error != null;
    }
    public String getError() {
        return error;
    }

    private final int statusCode;
    public int getStatusCode() {
        return statusCode;
    }

    public CassandraResponse(String result, int statusCode) {
        this.result = result;
        this.statusCode = statusCode;
    }

}
