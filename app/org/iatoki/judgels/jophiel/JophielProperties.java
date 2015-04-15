package org.iatoki.judgels.jophiel;

import com.amazonaws.services.s3.model.Region;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import play.Configuration;
import play.Play;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class JophielProperties {
    private static JophielProperties INSTANCE;

    private File avatarDir;
    private String aWSAccessKey;
    private String aWSSecretKey;
    private String aWSAvatarBucketName;
    private Region aWSAvatarBucketRegion;
    private String awsAvatarCloudFrontURL;
    private boolean useAWS;

    private JophielProperties() {

    }

    public File getAvatarDir() {
        return avatarDir;
    }

    public String getaWSAccessKey() {
        if (Play.isDev()) {
            return aWSAccessKey;
        } else {
            throw new RuntimeException();
        }
    }

    public String getaWSSecretKey() {
        if (Play.isDev()) {
            return aWSSecretKey;
        } else {
            throw new RuntimeException();
        }
    }

    public String getaWSAvatarBucketName() {
        return aWSAvatarBucketName;
    }

    public Region getaWSAvatarBucketRegion() {
        return aWSAvatarBucketRegion;
    }

    public String getAwsAvatarCloudFrontURL() {
        return awsAvatarCloudFrontURL;
    }

    public boolean isUseAWS() {
        return useAWS;
    }

    public static JophielProperties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JophielProperties();

            Configuration conf = Play.application().configuration();

            if (Play.isProd()) {
                verifyConfigurationProd(conf);
            } else if (Play.isDev()) {
                verifyConfigurationDev(conf);

                if (INSTANCE.useAWS) {
                    INSTANCE.aWSAccessKey = conf.getString("aws.access.key");
                    INSTANCE.aWSSecretKey = conf.getString("aws.secret.key");
                }
            }

            String baseDirName = conf.getString("jophiel.baseDataDir");

            if (INSTANCE.useAWS) {
                INSTANCE.aWSAvatarBucketName = conf.getString("aws.avatar.bucket.name");
                INSTANCE.aWSAvatarBucketRegion = Region.fromValue(conf.getString("aws.avatar.bucket.region.id"));
                INSTANCE.awsAvatarCloudFrontURL = conf.getString("aws.avatar.cloudFront.url");
            }

            File baseDir = new File(baseDirName);
            if (!baseDir.isDirectory()) {
                throw new RuntimeException("jophiel.baseDataDir: " + baseDirName + " does not exist");
            }

            try {
                INSTANCE.avatarDir = new File(baseDir, "avatar");
                FileUtils.forceMkdir(INSTANCE.avatarDir);
            } catch (IOException e) {
                throw new RuntimeException("Cannot create folder inside " + baseDir.getAbsolutePath());
            }

        }
        return INSTANCE;
    }

    private static void verifyConfigurationDev(Configuration configuration) {
        List<String> requiredKeys =  Lists.newArrayList(
              "jophiel.baseDataDir",
              "aws.use"
        );

        INSTANCE.useAWS = false;
        if ((configuration.getBoolean("aws.use") != null) && (configuration.getBoolean("aws.use"))) {
            INSTANCE.useAWS = true;
            requiredKeys.add("aws.access.key");
            requiredKeys.add("aws.secret.key");
            requiredKeys.add("aws.avatar.bucket.name");
            requiredKeys.add("aws.avatar.bucket.region.id");
        }

        for (String key : requiredKeys) {
            if (configuration.getString(key) == null) {
                throw new RuntimeException("Missing " + key + " property in conf/application.conf");
            }
        }
    }

    private static void verifyConfigurationProd(Configuration configuration) {
        List<String> requiredKeys = Lists.newArrayList(
              "jophiel.baseDataDir",
              "aws.use"
        );

        if ((configuration.getBoolean("aws.use") != null) && (configuration.getBoolean("aws.use"))) {
            INSTANCE.useAWS = true;
            requiredKeys.add("aws.avatar.bucket.name");
            requiredKeys.add("aws.avatar.bucket.region.id");
        }

        for (String key : requiredKeys) {
            if (configuration.getString(key) == null) {
                throw new RuntimeException("Missing " + key + " property in conf/application.conf");
            }
        }
    }
}
