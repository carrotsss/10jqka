package com.insigma.config.thread;

import com.icbc.dds.api.pojo.MetricsItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractMetrics {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMetrics.class);

	// 定时任务执行器
	private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

	// 统计并发
	private final ConcurrentMap<String, AtomicInteger> concurrents = new ConcurrentHashMap<String, AtomicInteger>();

	// 全局并发，可以从另外一个维度来了解当前的最大线程使用量  @author kfzx-Chengn 20200625
	private static final AtomicInteger ACTIVE = new AtomicInteger();

	// 统计监控数据
	private final ConcurrentMap<String, AtomicReference<long[]>> metricsMap = new ConcurrentHashMap<String, AtomicReference<long[]>>();

	private ScheduledFuture<?> furure;

	// 监控指标数组长度
	private static final int LENGTH = 6;

	/**
	 * 初始化化定期上报监控数据的任务
	 * 
	 * @param intervalTime
	 *            上报间隔
	 */
	public void startSendSchedule(long intervalTime) {
		furure = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				sendMetrics();
			}
		}, intervalTime, intervalTime, TimeUnit.MILLISECONDS);
	}

	/**
	 * 收集统计数据并调用子类的发送接口
	 */
	private void sendMetrics() {
		// 收集统计信息
		try {
			if (metricsMap.size() == 0) {
				return;
			}
			Map<String, MetricsItem> metricsItems = new HashMap<String, MetricsItem>();
			for (Map.Entry<String, AtomicReference<long[]>> entry : metricsMap.entrySet()) {
				AtomicReference<long[]> reference = metricsMap.remove(entry.getKey());
				if (reference != null) {
					// TODO: zhouwz 修改成 reference.getAndSet(newValue)
					long[] numbers = reference.get();
					if (numbers != null) {
						MetricsItem item = new MetricsItem();
						item.setMaxElapsed(numbers[0]);// 最大耗时
						item.setTotalElapsed(numbers[1]);// 总计耗时
						item.setSuccessCount(numbers[2]);// 成功次数
						item.setFailureCount(numbers[3]);// 失败次数
						item.setMaxConcurrent(numbers[4]);// 最大并发
						item.setMaxACTIVE(numbers[5]);// 服务器最大全局并发
						metricsItems.put(entry.getKey(), item);
					}
				}
			}
			if (metricsItems.size() == 0) {
				return;
			}
			send(metricsItems);
		} catch (Throwable t) { // 防御性容错
			LOGGER.error("Unexpected error occur at send statistic, cause: {}", t.getMessage(), t);
		}
	}

	/**
	 * 停止上报服务
	 */
	public void stopSendSchedule() {
		if (furure != null) {
			furure.cancel(true);
		}
		scheduledExecutorService.shutdownNow();
	}

	/**
	 * 发送监控数据
	 */
	protected abstract void send(Map<String, MetricsItem> metricsItems);

	/**
	 * 记录本次请求的调用结果
	 * 
	 * @param metricsKey
	 *            当前统计所使用的key
	 * @param start
	 *            请求开始时间
	 * @param concurrent
	 *            当前并发数
	 * @param isSuccess
	 *            请求是否成功
	 */
	public void collect(String metricsKey, long start, long concurrent, boolean isSuccess, long active) {
		this.collect(metricsKey, start, System.currentTimeMillis(), concurrent, isSuccess, active);
	}

	public void collect(String metricsKey, long start, long end, long concurrent, boolean isSuccess,long active) {
		// 耗时
		long elapsed = end - start;
		long successCount = isSuccess ? 1 : 0;
		long failureCount = !isSuccess ? 1 : 0;

		AtomicReference<long[]> reference = metricsMap.get(metricsKey);
		if (reference == null) {
			metricsMap.putIfAbsent(metricsKey, new AtomicReference<long[]>());
			reference = metricsMap.get(metricsKey);
		}
		long[] current;
		long[] update = new long[LENGTH];
		// 在高并发下，如果这里如果再赋值时发现reference对象中的值已经和current值不同，则需要重新获取current值做处理。
		do {
			current = reference.get();
			if (current == null) {
				update[0] = elapsed;// 最大耗时
				update[1] = elapsed;// 总计耗时
				update[2] = successCount;// 成功次数
				update[3] = failureCount;// 失败次数
				update[4] = concurrent;// 最大并发
				update[5] = active;// 全局并发

			} else {
				update[0] = current[0] > elapsed ? current[0] : elapsed;
				update[1] = current[1] + elapsed;
				update[2] = current[2] + successCount;
				update[3] = current[3] + failureCount;
				update[4] = current[4] > concurrent ? current[4] : concurrent;
				update[5] = current[5] > active ? current[5] : active;
			}
		} while (!reference.compareAndSet(current, update));

		// 日志中打印当前最大并发 和全局最大并发
		LOGGER.debug("concurrent is: " + concurrent);
		LOGGER.debug("active is: " + active);
	}


	/**
	 * 根据metricsKey获取当前的并发数统计
	 * 
	 * @param metricsKey
	 *            当前统计所使用的key
	 * @return AtomicInteger并发数
	 */
	public AtomicInteger getConcurrent(String metricsKey) {
		AtomicInteger concurrent = concurrents.get(metricsKey);
		if (concurrent == null) {
			concurrents.putIfAbsent(metricsKey, new AtomicInteger());
			concurrent = concurrents.get(metricsKey);
		}
		return concurrent;
	}



	/**
	 * 获取服务器上全局并发数
	 * @return AtomicInteger全局并发数
	 */
	public static AtomicInteger getACTIVE() {
		return ACTIVE;
	}
}
