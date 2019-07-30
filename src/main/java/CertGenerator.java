import sun.security.x509.X509CertImpl;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

public class CertGenerator {

    private String trueStoreFile = "truestore.jks";
    private String truestorePassword = "changeit";
    private String keyStoreFile = "keystore.jks";
    private String keyStorePassword = "changeit123!";
    private String keyType = "PKCS12";
    private String keyFile;
    private String keyPassword;
    private boolean generateKS = false;
    private SSLSocetFactoryBuilder sslsfb = new SSLSocetFactoryBuilder();

    public void setTrueStoreFile(String trueStoreFile) {
        this.trueStoreFile = trueStoreFile;
    }

    public void setTruestorePassword(String truestorePassword) {
        this.truestorePassword = truestorePassword;
    }

    public void setKeyStoreFile(String keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public void setGenerateKS(boolean generateKS) {
        this.generateKS = generateKS;
    }

    public void generateCertificates(String aURL) {
        if (generateKS) {
            saveKeyInFile();
        }
        if (keyFile != null && keyPassword != null) {
            sslsfb.setKey(keyFile, keyPassword, keyType);
        }

        HttpsURLConnection conn = null;
        try {
            URL destinationURL = new URL(aURL);
            conn = (HttpsURLConnection) destinationURL.openConnection();
            conn.setSSLSocketFactory(sslsfb.build());

            conn.connect();
            Certificate[] certs = conn.getServerCertificates();
            saveCertInFile(certs);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void saveCertInFile(Certificate[] listCerts) {
        try {
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(null, null);

            for (Certificate cert : listCerts) {
                String subjectDN = ((X509CertImpl) cert).getSubjectDN().getName();

                String issuerDN = ((X509CertImpl) cert).getIssuerDN().getName();
                String nameCert = subjectDN.split(",")[0].split("=")[1] + "(" + issuerDN.split(",")[0].split("=")[1] + ")";
                keystore.setCertificateEntry(nameCert, cert);
            }

            FileOutputStream out = new FileOutputStream(trueStoreFile);
            keystore.store(out, truestorePassword.toCharArray());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyInFile() {
        try (InputStream inputStream = new FileInputStream(keyFile)) {
            KeyStore keyStore = KeyStore.getInstance(keyType);
            keyStore.load(inputStream, keyPassword.toCharArray());

            KeyStore keyStoreOut = KeyStore.getInstance("JKS");
            keyStoreOut.load(null, null);

            Enumeration<String> aliases = keyStore.aliases();

            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                keyStoreOut.setCertificateEntry(alias, keyStore.getCertificate(alias));
            }

            FileOutputStream out = new FileOutputStream(keyStoreFile);
            keyStoreOut.store(out, keyStorePassword.toCharArray());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}