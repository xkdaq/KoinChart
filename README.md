KoinChart:基于MPChartLib开发股票分时图、K线图

最近制作一个炒币的app,类似于炒股的那种,网上资料很多很杂,最后使用github上面的MPAndroidChart库基本实现了功能。

一、介绍

MPAndroidChart库:https://github.com/PhilJay/MPAndroidChart

![](https://camo.githubusercontent.com/469a2460e3ae032f18db106cfae67adeea99e8ba/68747470733a2f2f7261772e6769746875622e636f6d2f5068696c4a61792f4d5043686172742f6d61737465722f64657369676e2f666561747572655f677261706869635f736d616c6c65722e706e67)

1.1支持图形

- Line Chart 折线图
- Bar Chart 直方图
- Pie Chart 饼图
- Bubble Chart 气泡图
- Candle Stick Chart 蜡烛图（用于展示金融数据时常称为K线图）
- Radar Chart 雷达图
- Cubic Line Chart 立方折线图
- Stacked Bar Chart 堆积图

1.2MPAndroid使用

- Project level build.gradle

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
- App level build.gradle

```
dependencies {
    implementation 'com.github.PhilJay:MPAndroidChart:v3.0.3'
}
```
二、基础实现效果图  

<img src="https://coding.net/u/xkdaq/p/KoinChart/git/raw/master/app/img/xk_kline_01.png" width="560" height="315" />

<img src="https://coding.net/u/xkdaq/p/KoinChart/git/raw/master/app/img/xk_kline_02.png" width="560" height="315" />

<img src="https://coding.net/u/xkdaq/p/KoinChart/git/raw/master/app/img/xk_kline_highlight_01.png" width="560" height="315" /> 

<img src="https://coding.net/u/xkdaq/p/KoinChart/git/raw/master/app/img/xk_kline_highlight_02.png" width="560" height="315" /> 

三、数据接口

**简要描述：**

- 获取k线数据

**请求URL：** 
- ` https://openapi.dragonex.io/api/v1/market/kline/ `
  
**请求方式：**
- GET

**参数：**

| 字段名  | 数据类型  | 说明  |
| ------------ | ------------ | ------------ |
| symbol_id  | int  | 交易对ID  |
| st | int  | 起始时间，从当前时间开始时可不传或传0，否则传unix时间戳(纳秒)  |
| direction  | int  | 查询方向：`1`-从起始时间往后查，`2`-从起始时间往前查，默认2  |
| count  | int  | 查询条数，最大100，默认10  |
| kline_type  | int  | k线类型：`1`-1min线， `2`-5min线， `3`-15min线， `4`-30min线， `5`-60min线， `6`-1day线.默认`1`  |


 **返回示例**

- 返回值data信息：  

| 字段名 | 数据类型 | 说明 |
| ----- | ----- | ---- |
| columns | [] | 下述列表每个位置的数据代表的意义 |
| list | [] | kline数据 |

- list信息：

| 字段名 | 数据类型 | 说明 |
| --- | --- | --- |
| amount | string | 交易额 |
| close_price | string | 收盘价 |
| max_price | string | 最高价 |
| min_price | string | 最低价 |
| open_price | string | 开盘价 |
| pre_close_price | string | 上一个收盘价 |
| timestamp | int | 秒级时间戳 |
| usdt_amount | string | 对应的USDT交易额 |
| volume | string | 交易量 |

```
{
    "ok": true,
    "code": 1,
    "msg": "",
    "data": {
        "columns": [
            "amount",
            "close_price",
            "max_price",
            "min_price",
            "open_price",
            "pre_close_price",
            "timestamp",
            "usdt_amount",
            "volume"
        ],
        "lists": [
            [
                "28413.7359",
                "289.0131",
                "290.7236",
                "288.9502",
                "289.9977",
                "0.0000",
                1536075900,
                "28413.7359",
                "98.0704"
            ],
            [
                "17430.8759",
                "289.1027",
                "290.3000",
                "288.9529",
                "289.0229",
                "0.0000",
                1536076800,
                "17430.8759",
                "60.3029"
            ],
            ......
            [
                "3812.4130",
                "254.9381",
                "255.6408",
                "254.9335",
                "254.9436",
                "0.0000",
                1536165000,
                "3812.4130",
                "14.9540"
            ]
        ]
    }
}

```

四、说明

- 参考MPChartLib  
github: https://github.com/PhilJay/MPAndroidChart

- KoinChart    
csdn:   
[基于MPAndroidChart库制作K线图(一) —— 基础图](https://blog.csdn.net/qq_31743309/article/details/82425959)  
[基于MPAndroidChart库制作K线图(二) —— 自定义x、y轴](https://blog.csdn.net/qq_31743309/article/details/82461714)  
[基于MPAndroidChart库制作K线图(三) —— 手势高亮联动](https://blog.csdn.net/qq_31743309/article/details/82466078)  
简书: https://www.jianshu.com/p/5526e90d5d3e  
github: https://github.com/xkdaq/KoinChart  
coding: https://coding.net/u/xkdaq/p/KoinChart/git  

五、声明  
>  接口数据来源网络,如有侵权,请联系作者删除

>  作者:会唱情歌的小猴子  
>  email:kekemixiu@126.com  
