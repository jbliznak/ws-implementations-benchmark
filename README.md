# Simple single-threaded WS client benchmark

Do X invocations of webservice hosted on running server.

##Usage

1. assuming you have AS running

2. First just build it to have deployment and deploy
    mvn clean install jboss-as:deploy

3. Then you need to re-prepare libraries and archives according to what you actually want to test:

    * current JDK RI
    mvn clean install

    * RI at certain version added as libs on classpath
    mvn clean install -Pmetro

    * CXF at certain version added as libs on classpath
    mvn clean install -Pcxf

    * JBOSSWS at certain version added as libs on classpath
    mvn clean install -Pjbossws

4. Run the benchmark
  * using java directly, with own classpath (same for all implemetations, target/lib always contains the libs you compiled it with)
java -cp target/classes/:target/lib/* org.jboss.test.ws.bench.ReproducerMain

   NOTE: Logging:
   for CXF and RI, I modified $JAVA_HOME/jre/lib/logging.properties to set default level to SEVERE, http://pastebin.test.redhat.com/258217
   for JBossWS, log4j.xml is included in this project

  * using JBoss Modules:
    change the -dep according to what you want to test
    I used:
    RI: no -dep
    CXF: -dep org.apache.cxf,org.apache.cxf.impl,javax.xml.ws.api,javax.jws.api
    JBOSSWS: -dep org.apache.cxf,org.apache.cxf.impl,org.jboss.ws.jaxws-client,javax.xml.ws.api,javax.jws.api,org.jboss.ws.api

    example:
    java -jar $JBOSS_HOME/jboss-modules.jar -mp $JBOSS_HOME/modules/ -cp target/classes/ -dep org.apache.cxf,org.apache.cxf.impl,org.jboss.ws.jaxws-client,javax.xml.ws.api,javax.jws.api,org.jboss.ws.api org.jboss.test.ws.bench.ReproducerMain

    NOTE: using this way, you must set logging preferences elsewise, probably by system properties..(I did not try that)



## Notes
by default 10000 warmup invocation are performed before the benchmark itself
by default benchmark runs for 120 seconds
you can alter this by setting system properties
-Dwarmup.requests=10000 -Dbench.seconds=120