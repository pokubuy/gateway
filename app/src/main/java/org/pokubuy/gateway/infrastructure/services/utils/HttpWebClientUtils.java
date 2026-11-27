package org.pokubuy.gateway.infrastructure.services.utils;

import org.pokubuy.gateway.infrastructure.exceptions.HttpWebClientException;

public final class HttpWebClientUtils {

    public static HttpWebClientException mapException(String serviceName, Throwable source) {
        return new HttpWebClientException(serviceName, source);
    }
}
