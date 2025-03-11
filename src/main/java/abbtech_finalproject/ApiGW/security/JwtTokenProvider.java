package abbtech_finalproject.ApiGW.security;

import io.jsonwebtoken.Jwts;

public class JwtTokenProvider {

    private final String secretKey;

    public JwtTokenProvider(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
