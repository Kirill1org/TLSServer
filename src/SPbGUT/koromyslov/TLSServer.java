package SPbGUT.koromyslov;

import sun.rmi.runtime.Log;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class TLSServer {

    private KeyStore trustStore;
    private KeyStore keyStore;
    private TrustManagerFactory trustManagerFactory;
    private KeyManagerFactory keyManagerFactory;
    private SSLServerSocket sslServerSocket;
    private SSLContext sslContext;

    public void loadTrustStoreCert() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        trustStore = KeyStore.getInstance("BKS");
        trustStore.load(new FileInputStream("C:/1/Key/keystore.bks"), "1234qwer!@#$QWER".toCharArray());
        System.out.println(("LOADED CERT IS:" + trustStore.size()));

    }

    public void initTrustManagerFactory() throws NoSuchAlgorithmException, KeyStoreException {
        trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
    }


    public void loadServerCert() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        keyStore = KeyStore.getInstance("BKS");
        keyStore.load(new FileInputStream("C:/1/Key/truststore,bks"), "123qwe!@#QWE".toCharArray());
    }

    public void initKeyManagerFactory() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {

        keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "12345678".toCharArray());

    }

    public void initSSLSocket() throws NoSuchAlgorithmException, IOException, KeyManagementException {

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(),null);

        sslServerSocket=(SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(9090);
        sslServerSocket.setNeedClientAuth(true);

        //System.out.println("Server is successfully created!"+" "+sslServerSocket.getSSLParameters());


    }

}
