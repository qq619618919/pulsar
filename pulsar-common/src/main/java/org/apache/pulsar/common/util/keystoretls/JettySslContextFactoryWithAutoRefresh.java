/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pulsar.common.util.keystoretls;

import javax.net.ssl.SSLContext;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.util.Set;

/**
 * SslContextFactoryWithAutoRefresh that create SSLContext for web server, and refresh in time.
 */
public class JettySslContextFactoryWithAutoRefresh extends SslContextFactory.Server {
    private final NetSslContextBuilder sslCtxRefresher;

    public JettySslContextFactoryWithAutoRefresh(String sslProviderString,
                                                 String keyStoreTypeString,
                                                 String keyStore,
                                                 String keyStorePassword,
                                                 boolean allowInsecureConnection,
                                                 String trustStoreTypeString,
                                                 String trustStore,
                                                 String trustStorePassword,
                                                 boolean requireTrustedClientCertOnConnect,
                                                 Set<String> ciphers,
                                                 Set<String> protocols,
                                                 long certRefreshInSec) {
        super();
        sslCtxRefresher = new NetSslContextBuilder(
                sslProviderString,
                keyStoreTypeString,
                keyStore,
                keyStorePassword,
                allowInsecureConnection,
                trustStoreTypeString,
                trustStore,
                trustStorePassword,
                requireTrustedClientCertOnConnect,
                certRefreshInSec);
        if (ciphers != null && ciphers.size() > 0) {
            this.setIncludeCipherSuites(ciphers.toArray(new String[0]));
        }
        if (protocols != null && protocols.size() > 0) {
            this.setIncludeProtocols(protocols.toArray(new String[0]));
        }
        if (sslProviderString != null) {
            setProvider(sslProviderString);
        }
    }

    @Override
    public SSLContext getSslContext() {
        return sslCtxRefresher.get();
    }
}
