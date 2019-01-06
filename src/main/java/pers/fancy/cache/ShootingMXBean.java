package pers.fancy.cache;

import java.util.Map;


/**
 * 终端用户访问加速节点时，如果该节点有缓存住了要被访问的数据时就叫做命中，如果没有的话需要回原服务器取，
 * 就是没有命中。取数据的过程与用户访问是同步进行的，所以即使是重新取的新数据，用户也不会感觉到有延时。
 * 命中率=命中数/（命中数+没有命中数）， 缓存命中率是判断加速效果好坏的重要因素之一。
 *
 * 缓存太小，造成频繁的LRU，也会降低命中率，缓存的有效期太短也会造成缓存命中率下降。
 *
 * @author
 */
public interface ShootingMXBean {

    void reqIncr(String pattern, int count);

    void hitIncr(String pattern, int count);

    Map<String, ShootingDO> getShooting();

    void reset(String pattern);

    void resetAll();

    class ShootingDO {

        private long hit;

        private long required;

        private String rate;

        public static ShootingDO newInstance(long hit, long required) {
            double rate = (required == 0 ? 0.0 : hit * 100.0 / required);
            String rateStr = String.format("%.1f%s", rate, "%");

            return new ShootingDO(hit, required, rateStr);
        }

        public static ShootingDO mergeShootingDO(ShootingDO do1, ShootingDO do2) {
            long hit = do1.getHit() + do2.getHit();
            long required = do1.getRequired() + do2.getRequired();

            return newInstance(hit, required);
        }

        private ShootingDO(long hit, long required, String rate) {
            this.hit = hit;
            this.required = required;
            this.rate = rate;
        }

        public long getHit() {
            return hit;
        }

        public long getRequired() {
            return required;
        }

        public String getRate() {
            return rate;
        }
    }

    default String summaryName() {
        return "zh".equalsIgnoreCase(System.getProperty("user.language")) ? "全局" : "summary";
    }
}
