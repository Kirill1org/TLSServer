package SPbGUT.koromyslov;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class Main {


    public static void main(String[] args) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, KeyManagementException {

        TLSServer tlsServer = new TLSServer();

        tlsServer.loadTrustStoreCert();
        tlsServer.initTrustManagerFactory();
        tlsServer.loadServerCert();
        tlsServer.initKeyManagerFactory();
        tlsServer.initSSLSocket();
    }

}
