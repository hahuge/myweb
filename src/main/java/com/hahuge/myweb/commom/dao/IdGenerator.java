package com.hahuge.myweb.commom.dao;

import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.hahuge.myweb.commom.dao.snowflake.IdWorker;
import com.hahuge.myweb.commom.utils.PropsUtil;


public class IdGenerator {

    protected static final Logger LOG = LoggerFactory.getLogger(IdGenerator.class);


    public static String uuid32() {
        return CharMatcher.is('-').removeFrom(UUID.randomUUID().toString());
    }

    public static String uuid34() {
        return UUID.randomUUID().toString();
    }

    public static long nextLongId() {
        return IdWorkerLoader.get().nextId();
    }



    private static class IdWorkerLoader {
        static {
            init();
        }
        public static IdWorker get() {
            return idWorker;
        }
        private static IdWorker idWorker;

        private static synchronized void init() {
            if(null!=idWorker){
                return;
            }
            IdWorkerConfig config = loadConfig(IdWorkerConfig.config_file);
            try {
                idWorker = new IdWorker(config.workerid, config.datacenterid);
            } catch (Exception e) {
                LOG.error("create idworker error. exit system.");
                System.exit(-1);
            }
        }

        private static int load_time_limit = 0;// 递归次数，只能递归一次

        private static IdWorkerConfig loadConfig(String fileName) {
            Properties properties = PropsUtil.loadProps(fileName);

            long workerid = PropsUtil.getInt(properties, "idworker.workerid");
            long datacenterid = PropsUtil.getInt(properties, "idworker.datacenterid");
            String file = PropsUtil.getString(properties, "idworker.config.file");

            IdWorkerConfig config = new IdWorkerConfig(workerid, datacenterid, file);
            if (StringUtils.isNotBlank(file)) {
                if (load_time_limit > 1) {
                    return config;
                }
                load_time_limit++;
                config = loadConfig(file);

            }
            return config;
        }

        static class IdWorkerConfig {

            static final String config_file = "idworker_config";

            String fileName;
            long workerid;
            long datacenterid;

            public IdWorkerConfig(long workerid, long datacenterid, String fileName) {
                this.workerid = workerid;
                this.datacenterid = datacenterid;
                this.fileName = fileName;
            }

            @Override
            public String toString() {
                return new ToStringBuilder(this)
                        .append("fileName", fileName)
                        .append("workerid", workerid)
                        .append("datacenterid", datacenterid)
                        .toString();
            }
        }
    }


    public static void main(String[] args) {

//        System.out.println(IdGenerator.uuid32());
//        System.out.println(IdGenerator.uuid32());
//        System.out.println(IdGenerator.uuid32());
//        System.out.println(IdGenerator.uuid32());
//        System.out.println(IdGenerator.uuid32());
//
//        System.out.println(IdGenerator.uuid34());
//        System.out.println(IdGenerator.uuid34());
//        System.out.println(IdGenerator.uuid34());
//        System.out.println(IdGenerator.uuid34());
        System.out.println(IdGenerator.uuid34());

        System.out.println(IdGenerator.nextLongId());
    }

}
