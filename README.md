TCP NET ENGINE STEP BY STEP

适用范围: windows, macos, linux <br/>
服务器架构: 高并发, 异步事件驱动的NIO框架, Reactor线程模型。<br/>
服务器网络模型: select(windows/linux), IOCP(windows), epoll(linux) <br/>
组件: 事件对象, 内存池, 对象池, 日志系统 <br/>
其他: 粘包拆包, 心跳检测, 消息统计, 计时器, c++11, 用于异步定时/定量发送的异步服务(CRCTaskServer), 结构化数据传输(适用于C/C++), 字节流数据传输(适用于所有), 几乎不使用第三方库 <br/>
标签中含有每一步的版本 <br/>
