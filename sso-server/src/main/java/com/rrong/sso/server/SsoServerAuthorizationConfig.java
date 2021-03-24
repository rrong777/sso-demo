package com.rrong.sso.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer // 这个注解之后 这个项目就是一个标准的Oauth2认证服务器了。
public class SsoServerAuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    // 配置应用服务器（认证服务器给哪些应用发令牌）
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("rrong1")
                .secret("rrongSecret1")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .and()
                .withClient("rrong2")
                .secret("rrongSecret2")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all");
    }

    // 配置生成令牌使用jwt的
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
    }

    // 认证服务器的安全配置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // isAuthenticated() 是SpringSecurity的一个授权表达式。下面这个配置是，要访问认证服务器的tokenKey的时候要经过身份认证。tokenKey
        // accessTokenConverter.setSigningKey("rrong"); 就是rrong
        // 发出去的是jwt，当应用拿到jwt的时候，需要解析里面的东西，需要去验证签名，需要验签就需要知道tokenKey，
        // 应用服务器可以到认证服务器拿这个密钥。你过来拿肯定要经过身份认证才能来拿。
        // tokenKeyAccess = "denyAll()"  默认是拒绝所有访问
        security.tokenKeyAccess("isAuthenticated()");
    }

    // 配置jwtTokenStore 仓库 生成jwt令牌
    @Bean
    public TokenStore jwtTokenStore() {
        // jwtTokenStore只管token的存取
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        // 设置签名使用的密钥
        accessTokenConverter.setSigningKey("rrong");
        return accessTokenConverter;
    }
}
