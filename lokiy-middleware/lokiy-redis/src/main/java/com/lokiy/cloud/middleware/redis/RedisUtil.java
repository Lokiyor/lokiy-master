package com.lokiy.cloud.middleware.redis;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Lokiy
 * @date 2020/3/19 13:41
 * @description: redis 帮助类
 */
@Component
public final class RedisUtil {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	//=============================================common=======================================================

	/**
	 * 指定缓存失效时间
	 * @param key 键
	 * @param time 时间(秒)
	 * @return 成功失败
	 */
	private Boolean expire(String key, long time) {
		try {
			if (time > 0) {
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据key 获取过期时间
	 * @param key  键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public Boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除缓存
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				redisTemplate.delete(key[0]);
			} else {
				redisTemplate.delete(CollectionUtils.arrayToList(key));
			}
		}
	}

	//=============================================String=======================================================

	/**
	 * 普通缓存获取
	 * @param key 键
	 * @return 值
	 */
	public Object get(String key) {
		return key == null ? null : redisTemplate.opsForValue().get(key);
	}

	/**
	 * 普通缓存放入
	 * @param key  键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public Boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 普通缓存放入并设置时间
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public Boolean set(String key, Object value, long time) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递增
	 * @param key 键
	 * @param delta 要增加几(大于0)
	 * @return 增后的数据
	 */
	public Long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 * @param key 键
	 * @param delta 要减少几(小于0)
	 * @return 减后的数据
	 */
	public Long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, -delta);
	}

	// ================================Map=================================
	/**
	 * HashGet
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public Object hget(String key, String item) {
		return redisTemplate.opsForHash().get(key, item);
	}


	/**
	 * 获取所有的hkey,hfield
	 * @param key 键
	 * @return map数据
	 */
	public Map<Object, Object> getMap(String key) {
		return redisTemplate.boundHashOps(key).entries();
	}

	/**
	 * 获取hashKey对应的所有键值
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Set<Object> hmkeys(String key) {
		return redisTemplate.opsForHash().keys(key);
	}
	/**
	 * 模糊获取keys
	 * @param key 键
	 */
	public Set<String> keys(String key) {
		return redisTemplate.keys(key);
	}
	/**
	 * 获取hashKey对应的所有键
	 * @param key 键
	 * @return 对应的所有键
	 */
	public Map<Object, Object> hmget(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * HashSet
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public Boolean hmset(String key, Map<String, Object> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * HashSet 并设置时间
	 * @param key 键
	 * @param map 对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public Boolean hmset(String key, Map<String, Object> map, long time) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public Boolean hset(String key, String item, Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 添加一个hashmap
	 * @param key 键
	 * @param map hash
	 */
	public void addMapList(String key, Map<?,?> map) {
		redisTemplate.boundHashOps(key).putAll(map);
	}

	/**
	 * 同hset
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 */
	public void addMap(String key, String item, Object value) {
		redisTemplate.boundHashOps(key).put(item, value);
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public Boolean hset(String key, String item, Object value, long time) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 * @param key 键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public void hdel(String key, Object... item) {
		redisTemplate.opsForHash().delete(key, item);
	}

	/**
	 * 判断hash表中是否有该项的值
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public Boolean hHasKey(String key, String item) {
		return redisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * @param key 键
	 * @param item 项
	 * @param by 要增加几(大于0)
	 * @return key的item增后的数据
	 */
	public Double hincr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * @param key 键
	 * @param item 项
	 * @param by 要增加几(大于0)
	 * @return key的item增后的数据
	 */
	public Long hincr(String key, String item, long by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 * @param key 键
	 * @param item 项
	 * @param by 要减少记(小于0)
	 * @return key的item减后的数据
	 */
	public Double hdecr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	/**
	 * hash递减
	 * @param key 键
	 * @param item 项
	 * @param by 要减少记(小于0)
	 * @return key的item减后的数据
	 */
	public Long hdecr(String key, String item, long by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}


	// =============================================set=================================================
	/**
	 * 根据key获取Set中的所有值
	 * @param key 键
	 * @return set集合数据
	 */
	public Set<Object> sGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 * @param key 键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public Boolean sHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public Long sSet(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 将set数据放入缓存
	 * @param key 键
	 * @param time 时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public Long sSetAndTime(String key, long time, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if (time > 0) { expire(key, time);}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 获取set缓存的长度
	 * @param key 键
	 * @return set长度
	 */
	public Long sGetSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 移除值为value的
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public Long setRemove(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().remove(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	// ===============================list=================================
	/**
	 * 获取list缓存的内容
	 * @param key 键
	 * @param start 开始
	 * @param end 结束 0 到 -1代表所有值
	 * @return list中的数据
	 */
	public List<Object> lGet(String key, long start, long end) {
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 * @param key 键
	 * @return list的长度
	 */
	public Long lGetListSize(String key) {
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}
	/**
	 * 取出最前面的元素
	 * @param key 键
	 * @return list中最前面的数据
	 */
	public Object lLeftPop(String key) {
		try {
			return redisTemplate.opsForList().leftPop(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 * @param key 键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return 根据索引获取的数据
	 */
	public Object lGetIndex(String key, long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @return list中放入数据
	 */
	public Boolean lSet(String key, Object value) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return list中放入延迟数据
	 */
	public Boolean lSet(String key, Object value, long time) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			if (time > 0) { expire(key, time);}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将整个list放入缓存
	 * @param key 键
	 * @param value 值
	 * @return list整个数据放入redis
	 */
	public Boolean lSet(String key, List<Object> value) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将整个list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return list整个放入redis 设置过期时间
	 */
	public Boolean lSet(String key, List<Object> value, long time) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) {expire(key, time);}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 * @param key 键
	 * @param index 索引
	 * @param value 值
	 * @return 根据list中的索引更新数据
	 */
	public Boolean lUpdateIndex(String key, long index, Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 * @param key 键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public Long lRemove(String key, long count, Object value) {
		try {
			return redisTemplate.opsForList().remove(key, count, value);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}



	//=================zset=============================

	/**
	 * 添加数据
	 *
	 * 添加方式：
	 * 1.创建一个set集合
	 * Set<ZSetOperations.TypedTuple<Object>> sets=new HashSet<>();
	 * 2.创建一个有序集合
	 ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>(value,排序的数值，越小越在前);
	 4.放入set集合
	 sets.add(objectTypedTuple1);
	 5.放入缓存
	 reidsImpl.Zadd("zSet", list);
	 * @param key 键
	 * @param tuples zset数据
	 */
	public void zAdd(String key,Set<ZSetOperations.TypedTuple<Object>> tuples) {
		redisTemplate.opsForZSet().add(key, tuples);
	}

	/**
	 * 放单个
	 * @param key 键
	 * @param value 值
	 * @param score 优先度
	 */
	public void zAdd(String key,Object value, double score) {
		redisTemplate.opsForZSet().add(key, value ,score);
	}

	/**
	 * 获取有序集合的成员数
	 * @param key 键
	 * @return 根据键获取的数据
	 */
	public Long zCard(String key) {
		return redisTemplate.opsForZSet().zCard(key);
	}

	/**
	 * 计算在有序集合中指定区间分数的成员数
	 * @param key 键
	 * @param min 最小排序分数
	 * @param max 最大排序分数
	 * @return 数量
	 */
	public Long zCount(String key,Double min,Double max) {
		return redisTemplate.opsForZSet().count(key, min, max);
	}

	/**
	 * 获取有序集合下标区间 start 至 end 的成员  分数值从小到大排列
	 * @param key 键
	 * @param start 开始下标
	 * @param end 结束下标
	 * @return 区间集合对象 从小到大
	 */
	public Set<Object> zRange(String key,int start,int end) {
		return redisTemplate.opsForZSet().range(key, start, end);
	}

	/**
	 * 获取有序集合下标区间 start 至 end 的成员  分数值从大到小排列
	 * @param key 键
	 * @param start 开始下标
	 * @param end 结束下标
	 * @return 区间集合对象 从大到小
	 */
	public Set<Object> reverseRange(String key,int start,int end) {
		return redisTemplate.opsForZSet().reverseRange(key, start, end);
	}

	/**
	 * 返回 分数在min至max之间的数据 按分数值递减(从大到小)的次序排列。
	 * @param key 键
	 * @param min 最小值
	 * @param max 最大值
	 * @return 集合 有限度从大到消排序
	 */
	public Set<Object> reverseRange(String key,Double min,Double max) {
		return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
	}

	/**
	 * 返回指定成员的下标
	 * @param key 键
	 * @param value 值
	 * @return 对应zset的下标
	 */
	public Long zRank(String key,Object value) {
		return redisTemplate.opsForZSet().rank(key, value);
	}

	/**
	 * 删除key的指定元素
	 * @param key 键
	 * @param values 值对象
	 * @return 返回删除的下标
	 */
	public Long zRemoveValue(String key,Object values) {
		return redisTemplate.opsForZSet().remove( key, values);
	}

	/**
	 * 移除下标从start至end的元素
	 * @param key 键
	 * @param start 开始坐标
	 * @param end 结束坐标
	 * @return 删除数量
	 */
	public Long zRemoveRange(String key,int start,int end) {
		return redisTemplate.opsForZSet().removeRange(key, start, end);
	}


	/**
	 * 移除分数从min至max的元素
	 * @param key 键
	 * @param min 最小优先度
	 * @param max 最大优先度
	 * @return 删除数量
	 */
	public Long zRemoveRangeByScore(String key,Double min,Double max) {
		return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}







	/**
	 * 获取同步锁
	 * @param lockKey 锁的名称
	 * @param expire 如果成功获取了锁，则给定锁的超时时间,单位：秒
	 * @return 获取返回true，否则false
	 */
	public Boolean getLock(String lockKey, Long expire) {
		try {
			if (Objects.equals(Boolean.TRUE, redisTemplate.opsForValue().setIfAbsent(lockKey, lockKey, expire, TimeUnit.SECONDS)) ) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}




	/*******************************以下为拓展方法*********************************/

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param hkey 项
	 * @param value 值
	 * @param time 时间(秒) 注意:该过期时间只有下次查询的时候做删除 或是定时任务清除
	 * @return true 成功 false失败
	 */
	public Boolean hsetEx(String key, String hkey, Object value, long time) {
		try {
			//记录存储过期对象
			long now = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
			RedisFieldExpireObject temp = new RedisFieldExpireObject(now+time, time, value);
			redisTemplate.opsForHash().put(key, hkey, temp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取具有过期时间的hash
	 * @param key 键 不能为null
	 * @param hkey 项 不能为null
	 * @return 值
	 */
	public Object hgetEx(String key, String hkey) {
		long now = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
		RedisFieldExpireObject temp = (RedisFieldExpireObject) redisTemplate.opsForHash().get(key, hkey);
		if(temp != null){
			if(now <= temp.getExpireTime()){
				//当前时间小于等于预过期时间 则返回
				return temp.getValue();
			}else {
				//当前时间大于预过期时间 则删除但钱key
				hdel(key, hkey);
			}
		}
		return null;
	}
	/**
     * 从hash中模糊搜索某key值并将k-v返回
     * @param key hash最外层的key
     * @param matchStr 内层key的matcher
     * @return k-v
     */
    public List<Entry<Object,Object>> hscan(String key, String matchStr) {
    	Cursor<Entry<Object, Object>> cursor = null;
    	try {
	    	ScanOptions match = ScanOptions.scanOptions().match(matchStr).build();
	    	cursor = redisTemplate.opsForHash().scan(key, match);
	    	List<Entry<Object,Object>> result = Lists.newArrayList();
	    	Entry<Object,Object> entry;
	    	while (cursor.hasNext()) {
	    		entry = cursor.next();
	    		result.add(entry);
	    	}
	    	return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	} finally {
    		if (null != cursor) {
    			try {
					cursor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
}
