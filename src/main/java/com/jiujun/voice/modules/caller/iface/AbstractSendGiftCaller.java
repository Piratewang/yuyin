package com.jiujun.voice.modules.caller.iface;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSON;
import com.jiujun.voice.common.container.BeanContainer;
import com.jiujun.voice.common.logger.util.LogUtil;
import com.jiujun.voice.common.threadpool.ThreadBlockHandle;
import com.jiujun.voice.common.utils.PrintException;
import com.jiujun.voice.common.utils.StringUtil;
import com.jiujun.voice.modules.caller.entity.SendGiftEntity;

/**
 * 赠送礼物调度器
 * 
 * @author Coody
 *
 */
public abstract class AbstractSendGiftCaller implements VoiceCaller<SendGiftEntity>, InitializingBean {

	private static List<AbstractSendGiftCaller> callers = new ArrayList<AbstractSendGiftCaller>();

	@Override
	public void afterPropertiesSet() throws Exception {
		callers.add(this);
	}

	public static void trigger(SendGiftEntity entity) {
		if (StringUtil.isNullOrEmpty(callers)) {
			return;
		}
		ThreadBlockHandle blockThreadPool = new ThreadBlockHandle(callers.size(), 60);
		for (AbstractSendGiftCaller caller : callers) {
			blockThreadPool.pushTask(new Runnable() {
				@Override
				public void run() {
					try {
						LogUtil.logger.info("送礼触发任务执行开始>>" + caller.getClass() + ">>" + JSON.toJSONString(entity));
						long time1 = System.currentTimeMillis();
						AbstractSendGiftCaller realCaller=BeanContainer.getBean(caller.getClass());
						realCaller.execute(entity);
						LogUtil.logger.info("送礼触发任务执行完毕>>" + caller.getClass() + ">>+speedTime:"
								+ (System.currentTimeMillis() - time1) + ">>" + JSON.toJSONString(entity));
					} catch (Exception e) {
						LogUtil.logger.info("送礼触发任务执行异常>>" + caller.getClass() + ">>" + JSON.toJSONString(entity) + "\n"
								+ PrintException.getErrorStack(e));
					}
				}
			});
		}
		
		blockThreadPool.execute();
	}

}
