import sun.security.x509.X509CertImpl;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

public class CertGenerator {

    private String trueStoreFile = "truststore.jks";
    private String truestorePassword = "changeit";
    private String keyStoreFile = "keystore.jks";
    private String keyStorePassword = "changeit123!";
    private String keyType = "PKCS12";
    private String keyFile;
    private String keyPassword = "";
    private String keyClientPassword;
    private String keyStoreKeyAlias;
    private String keyStoreKeyPassword;
    private boolean generateKS = false;
    private SSLSocketFactoryBuilder sslsfb = new SSLSocketFactoryBuilder();

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

    public void setKeyClientPassword(String keyClientPassword) {
        this.keyClientPassword = keyClientPassword;
    }

    public void setKeyStoreKeyAlias(String keyStoreKeyAlias) {
        this.keyStoreKeyAlias = keyStoreKeyAlias;
    }

    public void setKeyStoreKeyPassword(String keyStoreKeyPassword) {
        this.keyStoreKeyPassword = keyStoreKeyPassword;
    }

    private void initParamDefault() throws CertGenException {
        if (keyClientPassword == null) {
            keyClientPassword = keyPassword;
        }
        if (keyStoreKeyPassword == null) {
            keyStoreKeyPassword = keyPassword;
        }
        if (generateKS) {
            saveKeyInFile();
        }
        if (keyFile != null && keyPassword != null) {
            sslsfb.setKey(keyFile, keyPassword, keyType);
        }

    }

    public void generateCertificates(String aURL) throws CertGenException {
        initParamDefault();

        HttpsURLConnection conn = null;
        try {
            URL destinationURL = new URL(aURL);
            conn = (HttpsURLConnection) destinationURL.openConnection();
            conn.setSSLSocketFactory(sslsfb.build());

            conn.connect();
            Certificate[] certs = conn.getServerCertificates();
            System.out.printf("Serwer przy request na URL %s przedstawia sie %s certyfikatami.%n", aURL, certs.length);
            saveCertInFile(certs);

        } catch (IOException e) {
            throw new CertGenException(e.getMessage());
        }


    }

    public String getCN(String dn) {
        String[] dnArray = dn.split(",");
        for (int i = 0; i < dnArray.length; i++) {
            String[] subdnArray = dnArray[i].split("=");
            if ("CN".equalsIgnoreCase(subdnArray[0].trim())) {
                return subdnArray[1];
            }
        }
        return dn;
    }

    public void saveCertInFile(Certificate[] listCerts) throws CertGenException {
        System.out.printf("Zapisuje certyfikaty serwera do pliku %s:%n", trueStoreFile);
        try {
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(null, null);

            for (Certificate cert : listCerts) {

                String subjectDN = ((X509CertImpl) cert).getSubjectDN().getName();
                String issuerDN = ((X509CertImpl) cert).getIssuerDN().getName();

                String nameCert = getCN(subjectDN) + " (" + getCN(issuerDN) + ")";
                keystore.setCertificateEntry(nameCert, cert);
                System.out.printf("    %s%n", nameCert);
            }

            FileOutputStream out = new FileOutputStream(trueStoreFile);
            keystore.store(out, truestorePassword.toCharArray());
            out.close();
        } catch (FileNotFoundException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (CertificateException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (KeyStoreException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (IOException e) {
            throw new CertGenException(e.getMessage(), e);
        }
    }

    public void saveKeyInFile() throws CertGenException {
        System.out.printf("Zapisuje klucz klienta do pliku %s%n", keyStoreFile);
        try (InputStream inputStream = new FileInputStream(keyFile)) {
            KeyStore keyStore = KeyStore.getInstance(keyType);
            keyStore.load(inputStream, keyPassword.toCharArray());

            KeyStore keyStoreOut = KeyStore.getInstance("JKS");
            keyStoreOut.load(null, null);

            Enumeration<String> aliases = keyStore.aliases();

            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Key key = keyStore.getKey(alias, keyClientPassword.toCharArray());
                keyStoreOut.setKeyEntry((keyStoreKeyAlias == null ? alias : keyStoreKeyAlias), key, keyStoreKeyPassword.toCharArray(), keyStore.getCertificateChain(alias));
            }

            FileOutputStream out = new FileOutputStream(keyStoreFile);
            keyStoreOut.store(out, keyStorePassword.toCharArray());
            out.close();
        } catch (FileNotFoundException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (CertificateException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (KeyStoreException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (IOException e) {
            throw new CertGenException(e.getMessage(), e);
        } catch (UnrecoverableKeyException e) {
            throw new CertGenException(e.getMessage(), e);
        }
    }
}