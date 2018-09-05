KoinChart获取k线接口


    - 请求方式：GET
    - url：`https://openapi.dragonex.im/api/v1/market/kline/`
    - 传入值：

        | 字段名 | 数据类型 | 说明 |
        | --- | --- | --- |
        | symbol_id | int | 交易对ID |
        | st | int | 起始时间，从当前时间开始时可不传或传0，否则传unix时间戳(纳秒) |
        | direction | int | 查询方向：`1`-从起始时间往后查，`2`-从起始时间往前查，默认2 |
        | count | int | 查询条数，最大100，默认10 |
        | kline_type | int | k线类型：`1`-1min线， `2`-5min线， `3`-15min线， `4`-30min线， `5`-60min线， `6`-1day线.默认`1` |

    - 返回值data信息：

        | 字段名 | 数据类型 | 说明 |
        | --- | --- | --- |
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
    - 示例

        ```json
        {
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
                    "1124.8238",
                    "11.1694",
                    "11.1726",
                    "11.1693",
                    "11.1726",
                    "0.0000",
                    1524233100,
                    "1124.8238",
                    "100.7055"
                ],
                [
                    "112.5442",
                    "11.1833",
                    "11.1833",
                    "11.1796",
                    "11.1796",
                    "0.0000",
                    1524233160,
                    "112.5442",
                    "10.0652"
                ]
            ]
        }
        ```






>  数据来源网络,如有侵权,请联系作者删除  

>  作者:会唱情歌的小猴子  
>  email:kekemixiu@126.com  
>  github:https://github.com/xkdaq  
