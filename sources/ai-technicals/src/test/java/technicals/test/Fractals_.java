package technicals.test;

import java.util.Arrays;
import java.util.List;

import exchanges.binance.BinanceApiClient;
import exchanges.binance.BinanceCandle;
import exchanges.binance.BinanceCandleUtils;
import exchanges.binance.BinanceIntervalType;
import technicals.indicators.pp.Fractals;
import technicals.model.Candle;
import technicals.model.indicators.FractalEntry;

public class Fractals_
{

	public static void main(String[] args) throws Exception
	{
		// List<KucoinCandle> lstKucoinCandle = KucoinApiClient.getKlines("BTC-USDT", KucoinIntervalType._1d, 30);
		// Candle[] candles = KucoinCandleUtils.toCandleArray(lstKucoinCandle);

		// List<BybitCandle> lstBybitCandle = BybitApiClient.getKlines("BTCUSDT", BybitIntervalType._1d, 30);
		// Candle[] candles = BybitCandleUtils.toCandleArray(lstBybitCandle);

		List<BinanceCandle> lstBinanceCandles = BinanceApiClient.getKlines("BTCUSDT", BinanceIntervalType._1d, 30);
		Candle[] candles = BinanceCandleUtils.toCandleArray(lstBinanceCandles);

		FractalEntry[] entries = Fractals.calculate(candles, 2);

		Arrays.stream(entries).forEach(s -> System.out.println(s == null ? "null" : s));
	}

}
