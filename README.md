berikut adalah standard flow yang kita request untuk dibuatkan sample code-nya :
1.  Standard flow untuk master data (view data)
REST/SOAP <-->  Camel <--> JDG <--> JDV

QueryService
harbour-container--svc-jdv
harbour-container--svc-jdv2

Contoh Odata
http://localhost:8080/odata/SpinerVDB.2/Spiner_VBL_NewGen.TOS_CONTAINER_LAST_STACK?$format=json


2.  Standard flow untuk view transactional data in Oracle DB
REST/SOAP <--> EJB <--> JDV

harbour-container-svc


3.  Standard flow untuk insert/update/delete
SOAP/REST <--> Camel <--> AMQ <--> Camel <--> EJB

FrontendAdapter
CommonService
BackendAdapter

4.  Standard flow untuk komunikasi antar core services di EAP
SOAP/REST <--> EJB <--> EJB

harbour-container-svc

disamping itu untuk masing-masing standard flow di atas juga sudah mencakup untuk hal-hal sbb:
a.  Error handling --> Camel EIP
b.  Logging --> Default Platform
c.  Monitoring --> JBOSS On Network, JMX Monitoring


EJB/EAP7
--- Log database? sisi EAP configuration
