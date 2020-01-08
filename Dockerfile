FROM openjdk:11

COPY target/cql-cassandra-proxy-0.1.0.jar /opt/cql-cassandra-proxy/cql-cassandra-proxy-0.1.0.jar
COPY conf/cql-cassandra-proxy.yaml.template /opt/cql-cassandra-proxy/cql-cassandra-proxy.yaml

CMD java -jar /opt/cql-cassandra-proxy/cql-cassandra-proxy-0.1.0.jar server /opt/cql-cassandra-proxy/cql-cassandra-proxy.yaml
