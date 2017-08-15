package io.mybear.tracker.command;

import io.mybear.common.ErrorNo;
import io.mybear.common.constants.CommonConstant;
import io.mybear.net2.tracker.TrackerConnection;
import io.mybear.net2.tracker.TrackerMessage;
import io.mybear.tracker.TrackerProto;
import io.mybear.tracker.types.FdfsStorageJoinBody;
import io.mybear.tracker.types.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageJoinCommand extends TrackerCommand {

    private static final Logger logger = LoggerFactory.getLogger(StorageJoinCommand.class);

    // TODO:具体数值待定
    public static final int STORAGE_JOIN_BODY_LENGTH = 348;

    @Override
    public void handle(TrackerConnection conn, TrackerMessage message) {
        logger.debug("deal with storage join");

        byte state = 0;
        if (message.getPkgLen() < STORAGE_JOIN_BODY_LENGTH) {
            logger.error(
                    "cmd={}, client ip: {}, package size {} is not correct,  expect length >= {}"
                    , message.getCmd(), conn.getHost(), message.getPkgLen(),
                    STORAGE_JOIN_BODY_LENGTH);
            state = ErrorNo.EINVAL;
            message.setStatus(state);
            return;
        }

        FdfsStorageJoinBody joinBody = new FdfsStorageJoinBody();
        joinBody.setTrackerCount(message.readLong());
        if (joinBody.getTrackerCount() <= 0
                || joinBody.getTrackerCount() > CommonConstant.FDFS_MAX_TRACKERS) {
            logger.error("cmd={}, client ip: {}, tracker count {} is invalid, it <= 0 or > {}"
                    , message.getCmd(), conn.getHost(), joinBody.getTrackerCount(),
                    CommonConstant.FDFS_MAX_TRACKERS);
            state = ErrorNo.EINVAL;
            message.setStatus(state);
            return;
        }

        if(message.getPkgLen() != STORAGE_JOIN_BODY_LENGTH + joinBody.getTrackerCount() *
                TrackerProto.FDFS_PROTO_IP_PORT_SIZE){
            logger.error("cmd={}, client ip: {}, package size {} is not correct, expect length {}"
                    , message.getCmd(), conn.getHost(), message.getPkgLen(),
                    joinBody.getTrackerCount() * TrackerProto.FDFS_PROTO_IP_PORT_SIZE);
            state = ErrorNo.EINVAL;
            message.setStatus(state);
            return;
        }

        message.readByte(joinBody.getGroupName(), joinBody.getGroupName().length);
        if((state = TrackerProto.fdfsValidateGroupName(joinBody.getGroupName())) != 0){
            logger.error("client ip: {}, invalid group_name: {}"
                    , conn.getHost(), new String(joinBody.getGroupName()));
            message.setStatus(state);
            return;
        }

        joinBody.setStoragePort(message.readLong());
        if(joinBody.getStoragePort() <= 0){
            logger.error("client ip: {}, invalid port: {}"
                    , conn.getHost(), joinBody.getStoragePort());
            state = ErrorNo.EINVAL;
            message.setStatus(state);
            return;
        }

        joinBody.setStorageHttpPort(message.readLong());
        if(joinBody.getStorageHttpPort() <= 0){
            logger.error("client ip: {}, invalid http port: {}"
                    , conn.getHost(), joinBody.getStorageHttpPort());
            state = ErrorNo.EINVAL;
            message.setStatus(state);
            return;
        }

        joinBody.setStorePathCount(message.readLong());
        if(joinBody.getStorePathCount() <= 0){
            logger.error("client ip: {}, invalid store_path_count: {}"
                    , conn.getHost(), joinBody.getStorePathCount());
            state = ErrorNo.EINVAL;
            message.setStatus(state);
            return;
        }

        joinBody.setSubdirCountPerPath(message.readLong());
        if(joinBody.getSubdirCountPerPath() <= 0){
            logger.error("client ip: {}, invalid subdir_count_per_path: {}"
                    , conn.getHost(), joinBody.getSubdirCountPerPath());
            state = ErrorNo.EINVAL;
            message.setStatus(state);
            return;
        }

        byte[] ipPortData = new byte[TrackerProto.FDFS_PROTO_IP_PORT_SIZE];
        for(int i = 0; i < joinBody.getTrackerCount(); i++){
            message.readByte(ipPortData, ipPortData.length);

            TrackerServer trackerServer = new TrackerServer();
            int index = 0;
            while(ipPortData[i] != ':'){
                trackerServer.getIpAddr()[index] = ipPortData[i];
                index++;
            }
            if(index > ipPortData.length){
                logger.error("client ip: {}, invalid tracker server ip and port: {}"
                        , conn.getHost(), new String(ipPortData));
                state = ErrorNo.EINVAL;
                message.setStatus(state);
                return;
            }

            int end = index;
            while(ipPortData[end] != '\0'){
                end++;
            }

            trackerServer.setPort(Integer.valueOf(new String(ipPortData, index, end-index)));
            joinBody.getTrackerServers()[i] = trackerServer;
        }

        joinBody.setUploadPriority(message.readLong());
        joinBody.getJoinTime().setTime(message.readLong());
        joinBody.getUpTime().setTime(message.readLong());
        message.readByte(joinBody.getVersion(), joinBody.getVersion().length);
        message.readByte(joinBody.getDomainName(), joinBody.getDomainName().length);
        joinBody.setInitFlag(message.readByte());
        joinBody.setState(message.readByte());
    }
}
