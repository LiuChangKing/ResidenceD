# ResidenceD

ResidenceD 是在 [Residence](https://www.spigotmc.org/resources/residence.11480/) 插件基础上进行定制开发的 Bukkit/Spigot 服务器区域保护插件。项目内置多种配置文件和语言文件，能够让玩家在服务器中自行创建和管理自己的领地。本文档将介绍插件主要特性、编译方法及基本的目录结构。

## 功能概述

- **领地保护**：支持玩家在游戏内创建立体的 "Residence" 区域，通过各类指令或 GUI 管理区域的权限与行为限制。
- **子区域与组系统**：同一领地可划分多个子区域，并可依据 `groups.yml` 配置不同玩家组的权限、最大领地数量及大小限制。
- **多插件兼容**：插件在 `plugin.yml` 中声明依赖 `CMILib`，并可与 `Vault`、`CrackShot`、`Multiverse-Core`、`MultiWorld`、`dynmap`、`PlaceholderAPI`、`Slimefun`、`CS-CoreLib` 等插件联动。
- **语言与配置**：`src/main/resources/Language` 目录提供中文和英文语言文件，`flags.yml` 与 `groups.yml` 则定义默认权限、世界规则与玩家组限制。

## 编译与构建

本项目使用 Gradle 进行构建，Java 版本要求为 17。若系统中已安装 `gradle`，可在项目根目录执行：

```bash
gradle build
```

构建后生成的 JAR 文件位于 `build/libs/` 目录。将该文件放入服务器的 `plugins` 目录即可加载插件。

> 注意：第一次执行 `gradle build` 需要从网络下载依赖项，若仓库被阻止访问可能导致构建失败。

## 项目结构

```
ResidenceD/
├── build.gradle        Gradle 构建脚本
├── gradle/             Gradle Wrapper 配置
├── libs/               预置的依赖库（CMILib、Dynmap 等）
├── src/
│   └── main/
│       ├── java/       插件核心源码
│       └── resources/  配置文件与语言文件
└── README.md           项目说明文档
```

源码位于 `src/main/java/com/bekvon/bukkit/residence` 包下，除常规的命令和事件监听外，还包含与 BigDoors、Slimefun 等插件的兼容模块。

## 快速开始

1. 确保服务器安装对应版本的 Spigot 或 Paper，并已放入插件依赖（如 CMILib、Vault）。
2. 将构建好的 `Residence.jar` 复制到 `plugins` 目录后启动服务器。
3. 启动后在 `plugins/Residence/` 会生成默认的 `config.yml`、`flags.yml` 和 `groups.yml`，可按需修改。
4. 使用 `/res ?` 查看全部指令，或通过 GUI 管理领地权限等功能。

## 许可证

源代码在 `src/main/resources/LICENSE` 中声明的许可证下发布，使用前请仔细阅读并遵守相关条款。

