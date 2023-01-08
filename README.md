### Token Based Authentication & Authorization
#### HTTP Authentication Schemes:
 - Basic
 - Digest
 - Bearer
 - API Key
 - OAuth (1.0 and 2.0)
   - is an <b>authorization</b> protocol (The OAuth 2.0 authorization framework is a protocol 
 that allows a user to grant a third-party website or application access to the user's protected resources, 
 without necessarily revealing their long-term credentials or even their identity)
   - OpenID Connect (OIDC) is a protocol that enables websites or applications to grant access to users 
by <b>authenticating</b> them through another service or provider
 
#### OAuth 2.0 defines three flows to get an Access Token
1. Authorization Code Flow: used by Web Apps executing on a server.
2. Implicit Flow with Form Post: used by JavaScript-centric apps (Single-Page Applications) executing on the user's browser.
3. Client Credentials Flow: used for machine-to-machine communication.

#### Common steps for all flows:
1. https://console.cloud.google.com/apis/dashboard - create Client App
2. Enable Gmail API
3. Create Consent Screen
4. Create Client Credentials (Callback URL, Client Secret, Client Secret)

#### Authorization Code Flow steps:
documentation: https://developers.google.com/identity/protocols/oauth2/web-server<br>
5. Authorize App in Postman:
 - HTTP-method GET https://accounts.google.com/o/oauth2/v2/auth
 - Query Params:
   - scope = https://mail.google.com/ (possibility to Read, compose, send, and permanently delete all your email from Gmail)
   - access_type = offline (we will get Access Token & Refresh Token)
   - include_granted_scopes = true
   - response_type = code
   - state = state_parameter_passthrough_value
   - redirect_uri = https://localhost:8080 (equals to Callback URL, can be unavailable URL)
   - client_id = get Client ID from p.4 above<br>
6. Obtain Authorization Code. 
 - Copy Http-request and call it in browser, get response from URL bar, 
 where value of query parameter code = Authorization Code
7. Obtain Access Token & Refresh Token. In Postman:<br>
   Body x-www-form-urlencoded
 - HTTP-method POST https://oauth2.googleapis.com/token
    - client_id = get Client ID from p.4 above<br>
    - client_secret = get Client Secret from p.4 above<br>
    - code = paste value of Authorization code from p.6
    - grant_type = authorization_code
    - redirect_uri = https://localhost:8080 (equals to Callback URL, can be unavailable URL)
8. *(Optional) Refreshing an Access Token. In Postman:
 - HTTP-method POST https://oauth2.googleapis.com/token
     - client_id = get Client ID from p.4 above<br>
     - client_secret = get Client Secret from p.4 above<br>
     - grant_type = refresh_token
     - refresh_token = paste value of Refresh Token from p.7
 
#### Implicit Flow steps:
documentation: https://developers.google.com/identity/protocols/oauth2/javascript-implicit-flow<br>
9. Authorize App in Postman:
- HTTP-method GET https://accounts.google.com/o/oauth2/v2/auth
- Query Params:
    - scope = https://mail.google.com/ (possibility to Read, compose, send, and permanently delete all your email from Gmail)
    - include_granted_scopes = true
    - response_type = token
    - state = state_parameter_passthrough_value
    - redirect_uri = https://localhost:8080 (equals to Callback URL, can be unavailable URL)
    - client_id = get Client ID from p.4 above<br>
10. Obtain Access Token (Authorization Code or Refresh Token are not generated)
- Copy Http-request and call it in browser, get response from URL bar,
where value of query parameter code = Access Token

#### OpenID Connect (OIDC) steps:
documentation: https://developers.google.com/identity/openid-connect/openid-connect<br>
11. Authentication in Postman:
- HTTP-method GET https://accounts.google.com/o/oauth2/v2/auth
- Query Params:
    - response_type = code
    - client_id = get Client ID from p.4 above<br>
    - scope = openid%20email%20https://mail.google.com/
    - redirect_uri = https://localhost:8080 (equals to Callback URL, can be unavailable URL)
    - state = security_token%3D138r5719ru3e1%26url%3Dhttps%3A%2F%2Foauth2-login-gmail.com%2FmyHome
    - nonce = 0394852-3190485-2490358
    - hd = gmail.com
12. Obtain Authorization Code.
- Copy Http-request and call it in browser, get response from URL bar,
where value of query parameter code = Authorization Code
13. Obtain ID-Token (JWT Token) in Postman:<br>
Body x-www-form-urlencoded
 - client_id = get Client ID from p.4 above<br>
 - client_secret = get Client Secret from p.4 above<br>
 - code = paste value of Authorization code from p.12, replace %2F to /
 - grant_type = authorization_code
 - redirect_uri = https://localhost:8080 (equals to Callback URL, can be unavailable URL)<br>
*possibility to decode and get info (head, payload, signature) from JWT Token: https://jwt.io/

***

### Session Based Authentication
Authentication details are stored on the server side, less secure (CSRF Token to prevent CSRF attacks), only for single domain.
Server creates session in DB or local memory with user info and sends session ID back to the client.
Session ID in the form of a Cookie is stored by the client's browser.
 - Form based authorization<br><br>
Get Spring Security Application for testing purposes, steps:
1. Go to repository: https://github.com/dangeabunea/RomanianCoderExamples 
2. Download and unzip
3. Go to application: RomanianCoderExamples-master\SpringBootSecurity\Forms
4. mvnw.cmd clean install
5. cd target
6. java -jar springboot-security-forms-0.0.1-SNAPSHOT.jar
7. Enter in browser https://localhost:8443
***
