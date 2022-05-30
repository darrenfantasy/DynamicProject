# DynamicProject
基于爱奇艺的 Qigsaw 项目，去实现插件化（动态化）
因为在摸索 Qigsaw遇到一些坑，耽误了一些时间，所以用此项目可以更快的了解如何去集成

遇到的坑
1.在gradle.properties里 QIGSAW_BUILD 要配置为 true，才能进入 qigsaw模式，否则打包的一直是aab模式
2.Qigsaw Error: MultiDex or R8 task is missing，需要使用 1.4.1-hotfix02 版本 （https://github.com/iqiyi/Qigsaw/issues/37）
