package org.iatoki.judgels.jophiel.services;

import org.iatoki.judgels.play.Page;
import org.iatoki.judgels.jophiel.UserActivity;

import java.util.Set;

public interface UserActivityService {

    Page<UserActivity> pageUserActivities(long pageIndex, long pageSize, String orderBy, String orderDir, String filterString, Set<String> clientsNames, String username);

    Page<UserActivity> pageUsersActivities(long pageIndex, long pageSize, String orderBy, String orderDir, String filterString, Set<String> clientsNames, Set<String> usernames);

    void createUserActivity(String clientJid, String userJid, long time, String log, String ipAddress);

}
