package com.lokiy.cloud.middleware.emq.constant;

/**
 * @author Lokiy
 * @date 2020/5/20 16:07
 * @description: 消息
 */
public interface WildcardConst {

    /**
     * 不包含统配符
     */
    int LEVEL_WITHOUT = 0;

    /**
     * 通配符匹配到其中的某一层，即使用+通配符
     */
    int LEVEL_WITH_RANK = 1;

    /**
     * 匹配任何,即使用#通配符
     */
    int LEVEL_WITH_ANY = 2;
}
