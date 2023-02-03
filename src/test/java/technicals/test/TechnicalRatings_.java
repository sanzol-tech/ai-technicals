package technicals.test;

import java.util.List;

import exchanges.binance.BinanceApiClient;
import exchanges.binance.BinanceCandle;
import exchanges.binance.BinanceCandleUtils;
import exchanges.binance.BinanceIntervalType;
import technicals.indicators.complex.TechnicalRatings;
import technicals.model.TechCandle;

public class TechnicalRatings_
{

	public static void main(String[] args) throws Exception
	{
		List<BinanceCandle> lstBinanceCandles = BinanceApiClient.getKlines("ADAUSDT", BinanceIntervalType._1d, TechnicalRatings.minCandlesLengh());
		TechCandle[] candles = BinanceCandleUtils.toCandleArray(lstBinanceCandles);

		int pricePrecision = 4;

		long t1 = System.currentTimeMillis();
		TechnicalRatings tech = new TechnicalRatings(pricePrecision);
		tech.calculate(candles);
		long t2 = System.currentTimeMillis();

		System.out.println(t2 - t1 + " msecs\n");
		System.out.println(tech.toString());
	}

}
