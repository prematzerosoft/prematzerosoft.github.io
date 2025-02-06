@Configuration
public class OAuth2ClientConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(ServletContext servletContext) {
        String clientId = servletContext.getInitParameter("clientId");
        String clientSecret = servletContext.getInitParameter("clientSecret");

        // Retrieve values from your config POJO
        OAuth2ConfigProperties config = new OAuth2ConfigProperties();
        String authority = config.AUTHORITY;
        String graphApiUrl = config.GRAPH_API_URL;
        String graphWebSSO = config.GRAPH_EXTENSION_WEBSSO;

        ClientRegistration azureClientRegistration = ClientRegistration.withRegistrationId("azure")
            .clientId(clientId)
            .clientSecret(clientSecret)
            .scope("openid", "profile", "email")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .issuerUri(authority) // Use authority from config
            .authorizationUri(authority + "/oauth2/v2.0/authorize")
            .tokenUri(authority + "/oauth2/v2.0/token")
            .jwkSetUri(authority + "/discovery/v2.0/keys")
            .userInfoUri(graphApiUrl + "/v1.0/me") // Use Graph API URL
            .userNameAttributeName("id")
            .build();

        return new InMemoryClientRegistrationRepository(azureClientRegistration);
    }
}
