## server和client 通信管理
	
	利用RocketMQ的远程通信实现，进行简单处理，主要加入了自定义的nodeId（机器注册在zookeeper的UUID）。
	
### 1. 初始化注册
	注册时在zookeeper上生成注册信息和机器node的基本信息，server端主要是服务地址，port等。供客户端初始化时连接时使用。

### 2. 底层通信协议的实现: 前期主要实现  （命令）和 （任务）
	目前只使用ack心跳，job的CRUD，task分配的基本操作命令。任务目前主要由客户端自己维护。