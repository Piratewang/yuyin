package com.jiujun.voice.common.cache.instance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.jiujun.voice.common.cache.instance.iface.AspectCacheFace;
import com.jiujun.voice.common.threadpool.SysTimerHandle;
import com.jiujun.voice.common.utils.StringUtil;


/**
 * 内存缓存工具类
 * @author Coody
 *
 */
@Component
@SuppressWarnings("unchecked")
public class LocalCache implements AspectCacheFace{

	private static final ConcurrentHashMap<String, Object> CACHE_CONTAINER=new ConcurrentHashMap<>();
	static Object mutex = new Object();

	/**
	 * 增加缓存对象
	 * 
	 * @param key
	 * @param ce
	 * @param time
	 *            有效时间
	 */
	@Override
	public  void setCache(String key, Object value,
			Integer time) {
		CACHE_CONTAINER.put(key, new CacheWrapper(time,value));
		SysTimerHandle.execute(new TimeoutTimerTask(key,this), time * 1000);
	}
	/**
	 * 获取缓存KEY列表
	 * @return
	 */
	public static Set<String> getCacheKeys() {
		return CACHE_CONTAINER.keySet();
	}
	/**
	 * 模糊获取缓存KEY
	 * @param patton
	 * @return
	 */
	public List<String> getKeysFuzz(String patton){
		List<String> list=new ArrayList<String>();
		for (String key : CACHE_CONTAINER.keySet()) {
			if (StringUtil.isAntMatch(key, patton)) {
				list.add(key);
			}
		}
		if(StringUtil.isNullOrEmpty(list)){
			return null;
		}
		return list;
	}
	/**
	 * 增加缓存对象
	 * 
	 * @param key
	 * @param ce
	 * @param time
	 *            有效时间
	 */
	@Override
	public  void setCache(String key, Object value) {
			CACHE_CONTAINER.put(key, new CacheWrapper(value));
	}

	/**
	 * 获取缓存对象
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public <T> T getCache(String key) {
		CacheWrapper wrapper=(CacheWrapper) CACHE_CONTAINER.get(key);
		if(wrapper==null){
			return null;
		}
		return (T) wrapper.getValue();
	}

	/**
	 * 检查是否含有制定key的缓冲
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Boolean contains(String key) {
		return CACHE_CONTAINER.containsKey(key);
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 */
	@Override
	public void delCache(String key) {
		CACHE_CONTAINER.remove(key);
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 */
	public void delCacheFuzz(String key) {
		for (String tmpKey : CACHE_CONTAINER.keySet()) {
			if (tmpKey.contains(key)) {
				CACHE_CONTAINER.remove(tmpKey);
			}
		}
	}

	/**
	 * 获取缓存大小
	 * 
	 * @param key
	 */
	public int getCacheSize() {
		return CACHE_CONTAINER.size();
	}

	/**
	 * 清除全部缓存
	 */
	public void clearCache() {
		CACHE_CONTAINER.clear();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @projName：lottery
	 * @className：TimeoutTimerTask
	 * @description：清除超时缓存定时服务类
	 * @creater：Coody
	 * @creatTime：2014年5月7日上午9:34:39
	 * @alter：Coody
	 * @alterTime：2014年5月7日 上午9:34:39
	 * @remark：
	 * @version
	 */
	static class TimeoutTimerTask extends TimerTask {
		private String ceKey;
		private LocalCache cacheHandle;

		public TimeoutTimerTask(String key,LocalCache cacheHandle) {
			this.ceKey = key;
		}
		@Override
		public void run() {
			CacheWrapper cacheWrapper=(CacheWrapper) CACHE_CONTAINER.get(ceKey);
			if(cacheWrapper==null||cacheWrapper.getDate()==null){
				return;
			}
			if(System.currentTimeMillis()<cacheWrapper.getDate().getTime()){
				return;
			}
			cacheHandle.delCache(ceKey);
		}
	}

	private static class CacheWrapper{
		private Date date;
		private Object value;
		public CacheWrapper(int time,Object value){
			this.date=new Date(System.currentTimeMillis()+time*1000);
			this.value=value;
		}
		public CacheWrapper(Object value){
			this.value=value;
		}
		public Date getDate() {
			return date;
		}
		public Object getValue() {
			return value;
		}
	}
}
