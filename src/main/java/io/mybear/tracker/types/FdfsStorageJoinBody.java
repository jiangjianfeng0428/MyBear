package io.mybear.tracker.types;

import io.mybear.common.constants.CommonConstant;
import java.util.Date;

/**
 * Created by 258662572@qq.com on 2017/7/27.
 */
public class FdfsStorageJoinBody {
    private long storagePort;

    private long storageHttpPort;

    private long storePathCount;

    private long subdirCountPerPath;

    private long uploadPriority;

    private Date joinTime = new Date(0);

    private Date upTime = new Date(0);

    private byte[] version = new byte[CommonConstant.FDFS_VERSION_SIZE];

    private byte[] groupName = new byte[CommonConstant.FDFS_GROUP_NAME_MAX_LEN];

    private byte[] domainName = new byte[CommonConstant.FDFS_DOMAIN_NAME_MAX_SIZE];

    private byte initFlag;

    private byte state;

    private long trackerCount;

    private TrackerServer[] trackerServers = new TrackerServer[CommonConstant.FDFS_MAX_TRACKERS];

    public long getStoragePort() {
        return storagePort;
    }

    public void setStoragePort(long storagePort) {
        this.storagePort = storagePort;
    }

    public long getStorageHttpPort() {
        return storageHttpPort;
    }

    public void setStorageHttpPort(long storageHttpPort) {
        this.storageHttpPort = storageHttpPort;
    }

    public long getStorePathCount() {
        return storePathCount;
    }

    public void setStorePathCount(long storePathCount) {
        this.storePathCount = storePathCount;
    }

    public long getSubdirCountPerPath() {
        return subdirCountPerPath;
    }

    public void setSubdirCountPerPath(long subdirCountPerPath) {
        this.subdirCountPerPath = subdirCountPerPath;
    }

    public long getUploadPriority() {
        return uploadPriority;
    }

    public void setUploadPriority(long uploadPriority) {
        this.uploadPriority = uploadPriority;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public Date getUpTime() {
        return upTime;
    }

    public byte[] getVersion() {
        return version;
    }

    public byte[] getGroupName() {
        return groupName;
    }

    public byte[] getDomainName() {
        return domainName;
    }

    public byte getInitFlag() {
        return initFlag;
    }

    public void setInitFlag(byte initFlag) {
        this.initFlag = initFlag;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public long getTrackerCount() {
        return trackerCount;
    }

    public void setTrackerCount(long trackerCount) {
        this.trackerCount = trackerCount;
    }

    public TrackerServer[] getTrackerServers() {
        return trackerServers;
    }
}
