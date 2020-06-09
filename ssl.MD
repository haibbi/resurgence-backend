Generate public and private key

> cat public.crt private.crt > combined.pem

> openssl pkcs12 -export -in combined.pem -out cert.p12

> keytool -importkeystore -srckeystore cert.p12 -srcstoretype pkcs12 -destkeystore cert.jks
