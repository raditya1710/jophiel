package org.iatoki.judgels.jophiel.services;

import com.nimbusds.oauth2.sdk.AuthorizationCode;
import org.iatoki.judgels.play.Page;
import org.iatoki.judgels.jophiel.AccessToken;
import org.iatoki.judgels.jophiel.Client;
import org.iatoki.judgels.jophiel.ClientNotFoundException;
import org.iatoki.judgels.jophiel.IdToken;
import org.iatoki.judgels.jophiel.RefreshToken;

import java.util.List;

public interface ClientService {

    List<Client> findAll();

    List<Client> findAllClientByTerm(String term);

    boolean isClientAuthorized(String clientJid, List<String> scopes);

    boolean isValidAccessTokenExist(String token);

    boolean clientExistByClientJid(String clientJid);

    boolean clientExistByClientName(String clientName);

    Client findClientById(long clientId) throws ClientNotFoundException;

    Client findClientByJid(String clientJid);

    AuthorizationCode generateAuthorizationCode(String clientJid, String URI, String responseType, List<String> scopes, long expireTime);

    String generateAccessToken(String code, String userId, String clientId, List<String> scopes, long expireTime);

    void generateRefreshToken(String code, String userId, String clientId, List<String> scopes);

    void generateIdToken(String code, String userId, String clientId, String nonce, long authTime, String accessToken, long expireTime);

    org.iatoki.judgels.jophiel.AuthorizationCode findAuthorizationCodeByCode(String code);

    AccessToken regenerateAccessToken(String code, String userId, String clientId, List<String> scopes, long expireTime);

    AccessToken findAccessTokenByAccessToken(String token);

    AccessToken findAccessTokenByCode(String code);

    RefreshToken findRefreshTokenByRefreshToken(String token);

    RefreshToken findRefreshTokenByCode(String code);

    IdToken findIdTokenByCode(String code);

    long redeemAccessTokenById(long tokenId);

    void redeemRefreshTokenById(long tokenId);

    void redeemIdTokenById(long tokenId);

    void createClient(String name, String applicationType, List<String> scopes, List<String> redirectURIs);

    void updateClient(long clientId, String name, List<String> scopes, List<String> redirectURIs);

    void deleteClient(long clientId);

    Page<Client> pageClients(long pageIndex, long pageSize, String orderBy, String orderDir, String filterString);

}
