# httpserver 
****
基于javase实现的简易版httpserver
* websocket 网络编程
* 多线程
* 反射
* xml解析
## response
*  动态添加内容print
*  累加字节数的长度
*  根据状态码拼接响应头协议
*  根据状态码统一推送出去
## request
* 通过分解字符串获取method URL和请求参数POST请求参数可能在请求体中还存在
* 通过Map封装请求参数两个方法考虑一个参数多个值和中文
## Dispatcher
* 加入了多线程，可以同时处理多个请求，使用的是短连接
