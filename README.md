# certGenerator
Aplikacja cert generator służy do wygenerowania truststore z certyfikatami, dla połączenia z danym serwerem po https. Istnieje również możliwość podpięcia certyfikatu klienta, jeśli serwer tego wymaga. Możliwe jest wówczas wygenerowanie keystore z podanym kluczem klienta.

### Instalacja

Pobieramy repozytorium i kompilujemy kod przy pomocy maven

```sh
$ git clone https://github.com/BENEQ/certGenerator
$ cd certGenerator/
$ mvn package
```
Po poprawnym skompilowaniu, w folderze `target` mamy plik `certGenerator.jar`.
### Uruchamianie
Aby uruchomić program należy wpisać komendę w konsoli
```sh
$ java -jar certGenerator.jar https://google.pl
```
Spowoduje to pobranie certyfikatów serwera `https://google.pl` i umieszczenie ich w pliku truststore.jks. Adresy serwerów trzeba poprzedzać nazwą protokołu, czyli tak jak w powyższym przykładzie dodając `https://`. W przeciwnym wypadku dostaniemy błąd `ERROR: no protocol: google.pl`

### Opcje
Program można uruchamiać z wieloma opcjami. Kolejność podawania opcji jest dowolna. Wyświetlenie wszystkich opcji wraz z opisem:
```sh
$ java -jar certGenerator.jar -help
```
#### -tsFile [...]
```sh
$ java -jar certGenerator.jar https://google.pl -tsFile nowanazwa.jks
```
Pobrane certyfikaty zostaną zapisane w pliku `nowanazwa.jks`(domyślnie zawsze zapisuje do `truststore.jks`)
#### -tsPass [...]
```sh
$ java -jar certGenerator.jar https://google.pl -tsPass innehaslo
```
Pobrane certyfikaty zostaną zapisane w pliku `truestore.jks` z hasłem `innehaslo` (domyślnie hasło to zawsze `changeit`)
`truststore.jks`)
#### -key [...], keyPass [...], keyType [...], keyCPass [...]
```sh
$ java -jar certGenerator.jar https://google.pl -key plikZKluczemKlienta.p12 -keyPass haslo
```
Polecenie pozwala podpiąć certyfikat klienta `plikZKluczemKlienta.p12`, którego wymagają niekiedy serwery. Hasło do pliku podajemy w parametrze `keyPass` (jeśli hasło jest pustym ciągiem znaków to należy równiej je podać `-keyPass ""`. Domyślnie wymagany jest format `PKCS12`. Aby podać klucz klienta w innym formacie np `JKS`, używamy parametru `-keyType JKS`. Jeżeli jest ustawione hasło na klucz prywatny inne niż na cały plik, to używamy polecenia `-keyCPass`.
#### -gks
```sh
$ java -jar certGenerator.jar https://google.pl -gks -key plikZKluczemKlienta.p12 -keyPass haslo
```
Polecenie spowoduje wygenerowanie, oprócz truststore.jks, keystore.jks z certyfikatem klienta (domyślnie ten plik nie jest generowany). Hasło do keystore.jks to `changeit123!`.
#### -ksFile [...], -ksPass[...], -kskPass[...], -kskAliass[...]
Przy użyciu flagi `-gks`, tworzony jest domyślnie `keystore.jks` z hasłem `changeit123!`. Aby zmienić domyślną nazwę używamy `-ksFile`. Do zmiany hasła całego pliku służy polecenie `-ksPass`. Aby nadać alias naszemu kluczowi prywatnemu używamy polecenia `kskAliass` (domyślnie jest taki sam alias jaki w pliku z kluczem, przekazanego parametrem `-key`). Ustawianie hasła do klucza prywatnego to polecenie `-kskAliass` (domyślnie jest takie samo hasło do pliku z kluczem, przekazywane w parametrze `-keyPass`).
#### -url [...]
Parametr wskazujący adres serwera. Jest to parametr opcjonalny. Poniższe polecenia są równoważne
```sh
$ java -jar -url certGenerator.jar
$ java -jar certGenerator.jar 
```

**Wszystkie uwagi, błędy, pomysły zgłaszajcie na githubie**
