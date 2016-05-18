# 1. 架构思路

## 架构图
![架构图](http://7xread.com1.z0.glb.clouddn.com/6e2ad677-7d73-4ff1-92f4-b6eebd95ad9a)

## server 
  1. 负责维护和管理client连接。
  2. 提供统计和发放任务。
  
## client
  1. 负责爬取任务点的信息。
  2. 任务job的控制和信息收集agent
  
## storage
  1. 负责储存任务信息。
  2. 负责储存爬取信息。
  
#Quick Start
	1. 启动server端，io.github.liuzm.distribute.server.ServerMain 利用dubbo提供RPC远程供web端远程控制管理。目前存在server单点的问题。
	2. 启动manager-server，web端基本的服务。
	3. 启动客户端，与服务端进行连接（长连接管理）目前的心跳连接管理还不完善。