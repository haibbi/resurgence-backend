# Web-Socket Client Tarafında Kullanımı

Client olarak flutter/web/mobile düşünülebilir.

#### WS adresi belirlenmesi

`ws{s}://[hostname]:[port]/ws/[serverId]/[sessionId]/websocket`

web-socket adresi yukardaki gibi tanımlanmalıdır.

Server ve session id random olarak belirlenmesi gerekiyor. (Şimdilik)

Ek olarak `access_token` parametresi de verilmelidir.

`ws://localhost:8080/ws/1234/abcd1234/websocket?access_token=[TOKEN]` şeklinde http parametresi olarak verilebilir.

See: [sockjs](https://github.com/sockjs/sockjs-client/blob/master/README.md)

### Temel Komutlar

#### Connect

```
["CONNECT\naccept-version:1.1,1.0\nheart-beat:10000,10000\n\n\u0000"]
```

#### Subscribe

```
["SUBSCRIBE\nid:sub-0\ndestination:/topic/[TOPIC_NAME]n\n\u0000"]

["SUBSCRIBE\nid:sub-0\ndestination:/topic/carleone\n\n\u0000"]
```

### Send a Message

```
["SEND\ndestination:[TOPIC_NAME]\ncontent-type:application/json;charset=UTF-8\n\n[MESSAGE]\u0000"]

["SEND\ndestination:/carleone\ncontent-type:application/json;charset=UTF-8\n\n{\"content\":\"HALIL\"}\u0000"]
```
### Disconnect

```
["DISCONNECT\n\n\u0000"]
```
