package io.github.malczuuu.mqttbot.infrastructure.mqtt;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class MqttInsecureSslAdapter {

  public void apply(MqttConnectOptions options) throws MqttSecurityException {
    options.setSocketFactory(sslSocketFactoryWithoutSSLValidation());
  }

  private SSLSocketFactory sslSocketFactoryWithoutSSLValidation() throws MqttSecurityException {
    try {
      SSLContext ctx = SSLContext.getInstance("TLS");
      ctx.init(null, trustAllCertificates(), null);
      return ctx.getSocketFactory();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new MqttSecurityException(e);
    }
  }

  private TrustManager[] trustAllCertificates() {
    return new TrustManager[] {
      new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {}

        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
      }
    };
  }
}
