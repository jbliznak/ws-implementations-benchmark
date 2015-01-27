# Simple single-threaded WS client benchmark

Invoke webservice hosted on running server in a loop for a configured time period.

##Usage

1. assuming you have AS running

2. First just build it to have deployment and deploy
    mvn clean install jboss-as:deploy

3. Then you need to re-prepare libraries and archives according to what you actually want to test:

    * current JDK RI
     * mvn clean install

    * RI at certain version added as libs on classpath
      * mvn clean install -Pmetro

    * CXF at certain version added as libs on classpath
      * mvn clean install -Pcxf

    * JBOSSWS at certain version added as libs on classpath
      * mvn clean install -Pjbossws

4. Run the benchmark using java directly, with own classpath (same for all implemetations, target/lib always contains the libs you compiled it with)
  * java -cp target/classes/:target/lib/* org.jboss.test.ws.bench.ReproducerMain

   To properly use JBossWS, you need to set endorsed dir to target/lib/endorsed like this:
 * java -Djava.endorsed.dirs=target/lib/endorsed/ -cp target/classes/:target/lib/* org.jboss.test.ws.bench.ReproducerMain

   ###### NOTE: Logging:
   * for CXF and RI, I modified $JAVA_HOME/jre/lib/logging.properties to set default level to SEVERE
   * for JBossWS, log4j.xml is included in this project
   * you can use log4j with CXF by adding this property to command:
     * -Dorg.apache.cxf.Logger=org.apache.cxf.common.logging.Log4jLogger 


## Notes
by default 10000 warmup invocation are performed before the benchmark itself
by default benchmark runs for 120 seconds
you can alter this by setting system properties
-Dwarmup.requests=10000 -Dbench.seconds=120