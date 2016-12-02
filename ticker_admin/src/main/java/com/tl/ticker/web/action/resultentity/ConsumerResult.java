package com.tl.ticker.web.action.resultentity;

import com.tl.rpc.consumer.CONSUMERSTATUS;
import com.tl.rpc.consumer.Consumer;

import java.util.Date;

/**
 * Created by pangjian on 16-12-2.
 */
public class ConsumerResult {

    public String id;

    public String mobile;

    public Date createTime;

    public long balance;

    public String status;

    public String statusText;

    public String refereeId;

    public static ConsumerResult fromConsuemr(Consumer consumer){
        ConsumerResult result = new ConsumerResult();
        result.id = consumer.getId();
        result.mobile = consumer.getMobile();
        result.createTime = new Date(consumer.getCreateTime());
        result.balance = consumer.getBalance();
        result.status = consumer.getStatus().name();
        result.statusText = consumer.getStatus().name().equals(CONSUMERSTATUS.EFFECTIVE.name())?"有效":"无效";
        result.refereeId = consumer.getRefereeId();
        return result;
    }

}
