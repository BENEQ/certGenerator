import sun.security.x509.X509CertImpl;

import javax.net.ssl.HttpsURLConnection;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class CertGenerator {
    private String outKeyStoreName = "keystore.jks";
    private String keystorePassword = "changeit";

    public void generateCertificates(String aURL) {

        HttpsURLConnection conn = null;
        try {
            URL destinationURL = new URL(aURL);
            conn = (HttpsURLConnection) destinationURL.openConnection();
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
            int i = 0;
            for (Certificate cert : listCerts) {
                String subjectDN = ((X509CertImpl) cert).getSubjectDN().getName();

                String issuerDN = ((X509CertImpl) cert).getIssuerDN().getName();
                String nameCert = subjectDN.split(",")[0].split("=")[1] + "(" + issuerDN.split(",")[0].split("=")[1] + ")";
                keystore.setCertificateEntry(nameCert, cert);
                i++;
            }

            FileOutputStream out = new FileOutputStream(outKeyStoreName);
            keystore.store(out, keystorePassword.toCharArray());
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