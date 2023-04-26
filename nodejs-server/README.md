# ts-react
fullstack bolierplate with shack.js | 基于 shack.js 的全栈模板

## structure | 文件结构

```
+-- apis # each exported function in this folder is a backend api | 此文件夹下导出的函数都被用作后台接口
+-- web # front end codes | 前端代码
|     +-- index.tsx # entry | 前端代码入口
|     +-- index.html # htnl template | 前端代 html 模板
|     +-- pages # components for pages | 页面组件文件夹
+-- commands # coommands | 命令文件夹
+-- dist # compiled codes for production usage | 编译后用于产线的代码
```

## usage | 使用

dev(hot-reload for frontend only) | 开发(热加载仅限前端代码)

```
npm run dev
```

production | 产线

```
npm run build
npm run start
```

docker-compose

```
docker-compose up
```

command | 命令行

```
npm run cli
```