package io.n0sense.examsystem.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.aot.hint.ProxyHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class HttpServletRequestRuntimeHint implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        try {
            ProxyHints proxies = hints.proxies();
            proxies.registerJdkProxy(HttpServletRequest.class);
            proxies.registerJdkProxy(HttpSession.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
