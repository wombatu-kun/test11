### test11
Spring Microservices - Parcel Delivery APP  
Implement a solution that enables the user stories provided. This solution must be implemented as a micro-service architecture that is deployable using container technology.  
#### User Stories
##### User
- Can create a user account [POST http://localhost:8080/auth-service/v1/users];
- Can log in with JWT [POST http://localhost:8080/auth-service/oauth/token];   
- Can create a parcel delivery order [POST http://localhost:8080/order-service/v1/orders];  
- Can change the destination of a parcel delivery order [PUT http://localhost:8080/order-service/v1/orders/{orderId}/destination];  
- Can cancel a parcel delivery order [DELETE http://localhost:8080/order-service/v1/orders/{orderId}];  
- Can see the details of a delivery [GET http://localhost:8080/order-service/v1/orders/{orderId}];  
- Can see all parcel delivery orders that he/she created [GET http://localhost:8080/order-service/v1/orders];  
  
##### Admin
- Can change the status of a parcel delivery order [PUT http://localhost:8080/order-service/v1/orders/{orderId}/status];  
- Can view all parcel delivery orders [GET http://localhost:8080/order-service/v1/orders];  
- Can assign parcel delivery order to courier [PUT http://localhost:8080/order-service/v1/orders/{orderId}];  
- Can log in [POST http://localhost:8080/auth-service/oauth/token];  
- Can create a courier account [POST http://localhost:8080/auth-service/auth-service/v1/couriers];  
- Can track the delivery order by coordinates [GET http://localhost:8080/shipping-service/v1/orders/{orderId}/shipping];  
- Can see list of couriers with their statuses [GET http://localhost:8080/query-service/v1/couriers-stats];  
- ^Can see list of users with their statuses [GET http://localhost:8080/query-service/v1/users-stats].  
  
##### Courier
- Can log in [POST http://localhost:8080/auth-service/oauth/token];  
- Can view all parcel delivery orders that assigned to him [GET http://localhost:8080/order-service/v1/orders];  
- Can change the status of a parcel delivery order [PUT http://localhost:8080/order-service/v1/orders/{orderId}/status];  
- Can see the details of a delivery order [GET http://localhost:8080/order-service/v1/orders/{orderId}];  
- ^Can save track (current coordinates) while shipping orders [POST http://localhost:8080/shipping-service/v1/orders/shipping].  

#### Preferable technologies
- Java 8+, Gradle (preferable) or Maven - OK (Java 17, Gradle);  
- Spring Boot, Spring Data JPA (use Postgres), Liquibase - OK (Boot 2.7.9, Boot 3.0.6, PostgreSQL 13.4);  
- Proxy/API Gateway - OK (Spring cloud gateway);  
- Message Brokers - OK (Kafka);  
- Swagger (detailed API description for resources) - OK (springdoc-openapi in every service);  
- Unit tests written on Spock Framework (preferable) or JUnit - OK (Spock);  
- Provide detailed deployment instructions for getting your solution up and running - OK;  
- Create an opportunity to start services using docker compose or minikube - OK (docker-compose);  
- Feel free to add any additional features and practices to your submission in order to demonstrate your expertise in writing production grade software.  

#### Additional features and practices
- Spring Cloud Configuration Server;
- Service Discovery with Eureka;
- Monitoring with Spring boot Admin Server;
- Monitoring with Actuators, Micrometer, Prometheus, Grafana;
- Distributed tracing with Spring Cloud Sleuth & Zipkin;  
- Cache with Redis;  
- Resilience4j in gateway-service: RequestRateLimiter (redis), Retry, CircuitBreaker, TimeLimiter.

#### Solution Architecture Diagram
<img src="https://raw.githubusercontent.com/wombatu-kun/test11/main/diagram.png?sanitize=true&raw=true" />  
  
#### Build, start in docker & set up:  
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 
./gradlew bootBuildImage  
docker-compose -f docker-compose.yml up

Setting up Dashboard in Grafana:  
Open the http://localhost:3000/login in browser, login with credentials (admin: password).  
Go to Add Data Source page, select Prometheus as the time series data-base, configure the Prometheus URL (http://localhost:9090), save & test.  
Click the Dashboard icon on the left menu, select the Manage option, and click the Import button, select Import Via grafana.com to import the following dashboard: https://grafana.com/grafana/dashboards/11378/

Log in to wk-delivery-app as Admin:  
POST http://localhost:8080/auth-service/oauth/token  
(Authorization: Basic d2stZGVsaXZlcnktYXBwOnRoaXNpc3NlY3JldA==)  
Basic Auth:  
wk-delivery-app:thisissecret  
x-www-form-urlencoded:  
grant_type:password  
username:admin@company.com  
password:password  

Get value of access_token field from response and use it as Authorization header of every subsequent request (Authorization: Bearer access_token_value).  

#### Хорошо бы доделать/переделать
##### Имеющиеся недочёты
- использование deprecated WebSecurityConfigurerAdapter в ресурс-серверах;
- симметричная подпись JWT;  
- открытый доступ к актуаторам и adminserver;  
- 500ка вместо 401/403 в gatewayserver'е при протухшем токене.  
##### Возможные улучшательства
- переделать на Spring Boot 3 (gatewayserver и auth-service);
- сделать swagger с GroupOpenAPI на gatewayserver'е;
- embedded keycloak в качестве сервера аутентификации (или не keycloak, но самодельный JWT без спрингового oauth2 и множественные роли);    
- объединить в один инстанс ConfigServer и Eureka;  
- запросы получения/записи треков в shipping-service через websocket;  
- сервис-хранилище всех event'ов;  
- заменить Date-поля на ZonedDateTime;  
- log aggregation with ELK (but "The Amazon CloudFront distribution is configured to block access from your country");   
- k8s deployment with helm;  
- enable Sleuth for async.  