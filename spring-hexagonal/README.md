
### Installation

```bash
docker-compose up -d --build
```
### Keycloak Setup

1. Open the Keycloak admin console : http://localhost:8081/
Log in using the username/password defined in the `docker-compose.yml`.

2. Create a custom realms
![](./images/keycloak_manage_realms.png)

3. Create a client
![](./images/keycloak_create_client_1.png)
![](./images/keycloak_create_client_2.png)

⚠️ The Redirect URIs must match your environment (note: localhost will not work inside Docker).

4. Create a Realm roles
![](./images/keycloak_create_realm_role.png)

5. Create a user and assign roles
![](./images/keycloak_create_user.png)
![](./images/keycloak_create_user_password.png)
![](./images/keycloak_user_assign_role.png)


### Usefull endpoit
OpenID configuration : http://localhost:8081/realms/external/.well-known/openid-configuration


### Fetching a Token
From Insomnia (or Postman), configure the request as shown:
![](./images/keycloak_fetch_token_insomnia.png)

Then copy the Access Token and paste it into [jwt.io](https://www.jwt.io/) to inspect its contents.

### Swagger URL

http://localhost:8080/swagger-ui/index.html

