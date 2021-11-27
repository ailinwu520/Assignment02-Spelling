
/**
 * Record containing a token ({@link String}) and {@link TokenInfo}.
 *
 * Ordered on {@link TokenInfo#getFreq()}.
 */
public class TokenRecord implements Comparable<TokenRecord> {

    private final String token;
    private final TokenInfo info;

    public TokenRecord(String token, TokenInfo info) {
        this.token = token;
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int compareTo(TokenRecord a) {
        return Long.compare(this.info.getFreq(), a.info.getFreq());
    }

}