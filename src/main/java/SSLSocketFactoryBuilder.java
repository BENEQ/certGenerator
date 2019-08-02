import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLSocketFactoryBuilder {
    private SSLSocketFactoryBuilder builder;
    private KeyManager[] keyManagers;

    public SSLSocketFactoryBuilder() {
        this.builder = this;
        this.keyManagers = null;
    }

    public SSLSocketFactoryBuilder setKey(String keyFile, String keyPassword, String keyType) throws CertGenException {
        try (InputStream inputStream = new FileInputStream(keyFile)) {
            String algorithm = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(algorithm);

            KeyStore keyStore = KeyStore.getInstance(keyType);
            keyStore.load(inputStream, keyPassword.toCharArray());
            keyManagerFactory.init(keyStore, keyPassword.toCharArray());
            keyManagers = keyManagerFactory.getKeyManagers();
        } catch (FileNotFoundException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (IOException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (KeyStoreException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (CertificateException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (UnrecoverableKeyException e) {
            throw new CertGenException(e.getMessage(), e);
        }
        return builder;
    }

    public SSLSocketFactory build() throws CertGenException {
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLSv1.2");
            context.init(keyManagers, null, null);
        } catch (NoSuchAlgorithmException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (KeyManagementException e) {
            throw new CertGenException(e.getMessage(), e);
        }
        return context.getSocketFactory();
    }
}
