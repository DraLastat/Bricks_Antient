# Bricks_Antient

> 中文使用指引 -[目录](#目录)

> English version README -[Contents](#contents)


## 目录

- [项目介绍](#项目介绍)
- [如何安装](#如何安装)
- [如何使用](#如何使用)


## 项目介绍
Android应用自动化测试平台，基于Appium，通过实时获取手机界面图像，进而获取当前页面的控件信息来进行用例创建。用例可以如搭积木般拼接，组合成的用例也可以再次编辑，提高操作灵活性。
### 基本原理
经过拆分任何一个APP的操作，都是元素+动作的形式，通过最小元的拆解方法，可以实现灵活组合，于是有以下定义：
- 元素 + 动作 = 模块
- 模块 + 验证方法 = 事件
- 事件集 = 用例

### 平台特性
- 易用： 界面可视化，将主要精力用于用例设计
- 高效： 积木式搭建，随时拆装，随时重定义
- 可拓展： 将所有操作细化到最小单元，为自动创建用例提供最好的地基

## 如何安装
基本环境：
- Java 8
- Node.js 8.11.4(或者升级至最新)，[Node.js下载地址](https://nodejs.org/zh-cn/download/)
- Appium 1.8.1(更新版本未做测试)
- Android SDK

通过以下方式安装Appium 1.8.1：
```sh
$ npm install -g appium@1.8.1
```
如果是在国内进行安装，可能会遇到部分库无法下载导致安装失败，可以尝试使用cnpm镜像
```sh
$ npm install -g cnpm --registry=https://registry.npm.taobao.org
```
然后执行
```sh
$ cnpm install -g appium@1.8.1
```
最后将项目代码clone至本地，执行 [Main_Entry.java](https://github.com/DraLastat/Bricks_Antient/blob/master/Bricks_Antient/src/com/bricks/Main_Entry.java)即可运行Bricks平台。

## 如何使用




## Contents

- [Background](#background)
- [Install](#install)
- [Usage](#usage)


## Background


## Install

Basic environment needed：
- Java 8
- Node.js 8.11.4(或者升级至最新),[Node.js download link](https://nodejs.org/download/)
- Appium 1.8.1(更新版本未做测试)
- Android SDK

Install Appium 1.8.1：
```sh
$ npm install -g appium@1.8.1
```
Clone this project to your local enviroment, run the [Main_Entry.java](https://github.com/DraLastat/Bricks_Antient/blob/master/Bricks_Antient/src/com/bricks/Main_Entry.java) to active the platform.

### Any optional sections

## Usage

```
```

Note: The `license` badge image link at the top of this file should be updated with the correct `:user` and `:repo`.

### Any optional sections



### Any optional sections

## More optional sections


