package com.nepxion.skeleton.transport;

/**
 * <p>Title: Nepxion Skeleton</p>
 * <p>Description: Nepxion Skeleton For Freemarker</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.skeleton.exception.SkeletonException;
import com.nepxion.skeleton.property.SkeletonProperties;
import com.nepxion.skeleton.util.FileUtil;
import com.nepxion.skeleton.util.SkeletonUtil;
import com.nepxion.skeleton.util.ZipUtil;

public abstract class SkeletonDataTransport {
    private static final Logger LOG = LoggerFactory.getLogger(SkeletonDataTransport.class);

    public byte[] download(String generatePath, String directoryName, SkeletonProperties skeletonProperties) {
        if (StringUtils.isEmpty(generatePath)) {
            generatePath = SkeletonUtil.getTempGeneratePath();
        }

        if (StringUtils.isEmpty(directoryName)) {
            throw new SkeletonException("Directory name is null or empty");
        }

        try {
            String path = SkeletonUtil.getCanonicalPath(generatePath, directoryName, skeletonProperties);

            generate(path, skeletonProperties);

            String zipFilePath = ZipUtil.zip(path, null);
            File zipFile = new File(zipFilePath);

            LOG.info("Download skeleton file for " + directoryName + ".zip is executed");

            return FileUtil.getBytes(zipFile);
        } catch (Exception e) {
            throw new SkeletonException(e.getMessage(), e);
        } finally {
            File directory = new File(generatePath);

            FileUtil.forceDeleteDirectory(directory, 5);
        }
    }

    public abstract void generate(String path, SkeletonProperties skeletonProperties) throws Exception;
}