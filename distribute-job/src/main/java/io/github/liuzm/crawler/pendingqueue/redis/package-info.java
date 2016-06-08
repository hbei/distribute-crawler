/**
 * 后期可以扩展为 job 对应下的待处理任务 {job}:{type}:...
 * HashMap:
 * <table border="1">
 * 		<tr><td>key</td><td>value</td><td>desc</td></tr>
 * 		<tr><td>type</td><td>String</td><td>该储存的类型</td></tr>  
 * 		<tr><td>{type}:Queue</td><td>LinkedList<T></td><td>类型待处理的队列</td></tr>
 * 		<tr><td>{type}:success</td><td>double</td><td>成功数</td></tr>
 * 		<tr><td>{type}:count</td><td>double</td><td>总数</td></tr>
 * 		<tr><td>{type}:failure</td><td>double</td><td>失败数</td></tr>
 * 		<tr><td>{type}:ignored</td><td>double</td><td>忽略数</td></tr>
 * </table>
 * 		
 * distribute urls manage
 */
package io.github.liuzm.crawler.pendingqueue.redis;
