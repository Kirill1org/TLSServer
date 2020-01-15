package SPbGUT.koromyslov;

import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;

public class TLSServer {

    private KeyStore trustStore;
    private KeyStore keyStore;
    private TrustManagerFactory trustManagerFactory;
    private KeyManagerFactory keyManagerFactory;
    private SSLContext sslContext;

    private byte[] recivedData;

    private static int PORT=9090;

    private void loadTrustStoreCert() {
        try (FileInputStream fileInputStream = new FileInputStream("C:/1/key/1/Server/truststore.jks")) {
            trustStore = KeyStore.getInstance("JKS");
            trustStore.load(fileInputStream, "123qwe!@#QWE".toCharArray());
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("LoadTrustCert error");
        }

    }

    private void initTrustManagerFactory() throws NoSuchAlgorithmException, KeyStoreException {
        trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
    }


    private void loadKeyStoreCert() {
        try (FileInputStream fileInputStream = new FileInputStream("C:/1/key/1/Server/keystore.jks")) {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(fileInputStream, "123qwe!@#QWE".toCharArray());
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("LoadServerCert error");
        }
    }

    private void initKeyManagerFactory() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {

        keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "123qwe!@#QWE".toCharArray());

    }

    private void initSSLSocket() {

        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException("LoadServerCert error");
        }

        try (SSLServerSocket sslServerSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(PORT);
             SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
             InputStream inputStream = sslSocket.getInputStream();
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {

            sslServerSocket.setNeedClientAuth(true);


            int length = dataInputStream.readInt();
            if (length != 0) {
                recivedData = new byte[length];
                dataInputStream.readFully(recivedData, 0, length);
            }
            System.out.println(Arrays.toString(recivedData));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("InitSSL socket error");
        }


    }

    public void init() {
        try {
            loadTrustStoreCert();
            initTrustManagerFactory();
            loadKeyStoreCert();
            initKeyManagerFactory();
            initSSLSocket();
        } catch (KeyStoreException | NoSuchAlgorithmException
                | UnrecoverableKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("InitKeys error");
        }

    }
}
