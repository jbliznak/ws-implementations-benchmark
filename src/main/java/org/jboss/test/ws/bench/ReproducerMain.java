/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.ws.bench;


import org.jboss.test.ws.war.Endpoint;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.logging.Logger;

public class ReproducerMain {

    private static final String endpointURL = "http://localhost:8080/test-deployment/TestService";
    private static final String targetNS = "http://war.ws.test.jboss.org/";
    private static final int BENCHMARK_SECONDS = Integer.getInteger("bench.seconds", 120);
    private static final int WARMUP_REQUESTS = Integer.getInteger("warmup.requests", 10000);

    public static void main(String[] args) throws Exception {


        URL wsdlURL = new URL(endpointURL + "?wsdl");
        QName serviceName = new QName(targetNS, "EndpointService");

        Service service = Service.create(wsdlURL, serviceName);
        Endpoint port = service.getPort(Endpoint.class);

        //warm up
        System.out.println();
        System.out.println("WARM UP before benchmark ("+WARMUP_REQUESTS+" invocations)");
        for (int i = 0; i < WARMUP_REQUESTS; i++) {
            String ignore = port.echo("jojo");
            if (i % 1000 == 0) {
                System.out.println("iteration: " + i + " | ");
            }
        }

        System.out.println("WARM UP done, executing " + BENCHMARK_SECONDS + "sec benchmark");
        long start = System.currentTimeMillis();
        for (int i = 1; ; i++) {
            String ignore = port.echo("jojo");
            if (i % 1000 == 0) {
                System.out.print("iteration: " + i + " | ");
                System.out.println((System.currentTimeMillis() - start) / 1000f + "s");
            }
            if (System.currentTimeMillis() - start > 1000 * BENCHMARK_SECONDS) {
                System.out.println("FINAL iteration: " + i);
                break;
            }
        }
    }
}
