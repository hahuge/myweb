package com.hahuge.myweb.commom.dao.snowflake;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 在分布式系统全局UID  twitter的snowflake方案
 *
 * 核心代码就是毫秒级时间   41位+机器ID 10位+毫秒内序列12位。   =63位
 * 0---0000000000 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---000000000000
 * 在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），
 * 接下来的41位为毫秒级时间，然后5位datacenter标识位，5位机器ID（并不算标识符，实际是为线程标识），
 * 然后12位该毫秒内的当前毫秒内的计数，加起来刚好64位，为一个Long型。
 *
 * 该项目地址为：https://github.com/twitter/snowflake是用Scala实现的。
 *
 * IdWorker
 *
 * @author songsp
 * @since 2015/12/25 09:55
 */
public class IdWorker {
    protected static final Logger LOG = LoggerFactory.getLogger(IdWorker.class);

    private long workerId;
    private long datacenterId;
    private long sequence = 0L;

    private final static long twepoch = 1497429169631L; //当前时间会减去改值，一个项目确定后不能修改

    private long workerIdBits = 5L;
    private long datacenterIdBits = 5L;
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private long sequenceBits = 12L;

    private long workerIdShift = sequenceBits;
    private long datacenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

    public IdWorker(long workerId, long datacenterId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        LOG.info(String.format("idworker started. datacenterid=%d, workerid=%d.", this.datacenterId, this.workerId));
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            LOG.error(String.format("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp));
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }




//测试=====================
    static class IdWorkThread implements Runnable {
        private Set<Long> set;
        private IdWorker idWorker;
        private long start;
        private long end ;
        private long amount =0;
        public IdWorkThread(Set<Long> set, IdWorker idWorker) {
            this.set = set;
            this.idWorker = idWorker;
        }

        @Override
        public void run() {
            start= System.currentTimeMillis();
            while (true) {
                long id = idWorker.nextId();
                amount++;
                end = System.currentTimeMillis();
                if (!set.add(id)) {
                    System.out.println("duplicate:" + id);
                }
            }
        }
        public String  getCost(){
            return  "cost:" + (end - start) + "ms, count="+amount ;
        }
    }

    public static void main(String[] args) {

        System.out.println(-1L ^ (-1L << 5L));

        System.out.println(1<<5);
        System.out.println( -1L ^ (-1L << 12));
        System.out.println( (-1L ^ (-1L << 12)) & 4096);
        System.out.println(-1 ^ (-1 <<2));

        boolean stop = true;
        if(stop){
            return;
        }
        final IdWorker idWorker1 = new IdWorker(0, 0);
        long start = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            System.out.println(idWorker1.nextId());
        }
        System.err.println(System.currentTimeMillis()-start);

        Set<Long> set = new HashSet<Long>();

        IdWorkThread w1 = new IdWorkThread(set, idWorker1);
        IdWorkThread w2 = new IdWorkThread(set, idWorker1);
        IdWorkThread w3 = new IdWorkThread(set, idWorker1);

        //Thread t1 = new Thread(new IdWorkThread(set, idWorker1));
        //Thread t2 = new Thread(new IdWorkThread(set, idWorker2));

        new Thread(w1);
        new Thread(w2);
        new Thread(w3);

        //t1.setDaemon(true);
        //t2.setDaemon(true);

//        t1.start();
//        t2.start();
//        t3.start();


        for(int i=0;i<10;i++){
            Thread t = new Thread(w1);
            t.start();
        }

        try {
            Thread.sleep(30000);

//            System.out.println(w1.getCost());
//            System.out.println(w2.getCost());
//            System.out.println(w3.getCost());


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
