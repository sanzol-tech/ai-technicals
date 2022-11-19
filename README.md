# ai-technicals
Library for financial technical analysis with multiple technical indicators

### Moving Average
- SMA - Simple Moving Average
- WMA - Weighted Moving Average
- EMA - Exponential Moving Average
- VWMA - Volume-Weighted Moving Average

### Oscillators
- Aroon
- ADX - Average Directional Index
- BBP - Bull Bear Power
- CCI - Commodity Channel Index
- MACD - Moving Average Convergence / Divergence
- Momentum
- ROC - Rate Of Change
- RSI - Relative Strength Index
- Stochastic
- Stochastic RSI
- UO - Ultimate Oscillator
- Williams %R

### Volatility
- Average True Range
- Bollinger
- Keltner Channel
- Price Channel
- Standard Deviation

### Volume
- A/D - Accumulation / Distribution
- MFI - Money Flow Index
- OBV - On Balance Volume
- VWAP - Volume Weighted Average Price

### Pivot Points
- Classic Pivot Points
- Camarilla Pivot Points
- Demarks Pivot Points
- Fibonacci Pivot Points
- Woodie Pivot Points

## How to use the library

### Example 1:

| Indicator | Exchange | Pair | Interval |
| --- | --- | --- | --- |
| Bollinger | Binance | BTCUSDT | 1 day |

```
List<BinanceCandle> lstBinanceCandles = BinanceApiClient.getKlines("BTCUSDT", BinanceIntervalType._1d, 30);
Candle[] candles = BinanceCandleUtils.toCandleArray(lstBinanceCandles);
BollingerEntry[] entries = Bollinger.calculate(candles, 20, 2);
Arrays.stream(entries).forEach(s -> System.out.println(s == null ? "null" : s));
```

### Example 2:

| Indicator | Exchange | Pair | Interval |
| --- | --- | --- | --- |
| Stochastic | Kucoin | BTC-USDT | 1 day |

```
List<KucoinCandle> lstKucoinCandle = KucoinApiClient.getKlines("BTC-USDT", KucoinIntervalType._1d, 30);
Candle[] candles = KucoinCandleUtils.toCandleArray(lstKucoinCandle);
StochasticEntry[] entries = Stochastic.calculate(candles, 14, 1, 3);
Arrays.stream(entries).forEach(s -> System.out.println(s == null ? "null" : s));
```

