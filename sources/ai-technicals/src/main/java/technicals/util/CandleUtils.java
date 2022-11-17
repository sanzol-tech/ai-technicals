package technicals.util;

import technicals.model.Candle;

public class CandleUtils
{

	public static double[] toDoubleArray(Candle[] candles)
	{
		double[] prices = new double[candles.length];
		for (int i = 0; i < candles.length; i++)
		{
			prices[i] = candles[i].getDefaultPrice();
		}
		return prices;
	}

	public static double sumPrice(Candle[] candles, int startIndex, int endIndex)
	{
		double sum = 0;
		for (int i = startIndex; i <= endIndex; i++)
		{
			sum += candles[i].getDefaultPrice();
		}
		return sum;
	}

	public static double avgPrice(Candle[] candles, int startIndex, int endIndex)
	{
		double sum = 0;
		for (int i = startIndex; i <= endIndex; i++)
		{
			sum += candles[i].getDefaultPrice();
		}
		return sum / (endIndex - startIndex + 1);
	}

	public static Candle[] slice(Candle[] candles, int startIndex, int endIndex)
	{
		Candle[] newCandles = new Candle[endIndex - startIndex + 1];
		for (int i = startIndex; i <= endIndex; i++)
		{
			newCandles[i - startIndex] = candles[i];
		}
		return newCandles;
	}

	public static int highestHighIndex(Candle[] candles, int startIndex, int endIndex)
	{
		double value = candles[startIndex].getHighPrice();
		int index = startIndex;

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getHighPrice() > value)
			{
				value = candles[i].getHighPrice();
				index = i;
			}
		}

		return index;
	}

	public static int lowestLowIndex(Candle[] candles, int startIndex, int endIndex)
	{
		double value = candles[startIndex].getLowPrice();
		int index = startIndex;

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getLowPrice() < value)
			{
				value = candles[i].getLowPrice();
				index = i;
			}
		}

		return index;
	}

	public static double highestHigh(Candle[] candles, int startIndex, int endIndex)
	{
		double value = candles[startIndex].getHighPrice();

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getHighPrice() > value)
			{
				value = candles[i].getHighPrice();
			}
		}

		return value;
	}

	public static double lowestLow(Candle[] candles, int startIndex, int endIndex)
	{
		double value = candles[startIndex].getLowPrice();

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getLowPrice() < value)
			{
				value = candles[i].getLowPrice();
			}
		}

		return value;
	}

	public static Candle mergeCandles(Candle[] candles, int startIndex, int endIndex)
	{
		double high = candles[startIndex].getHighPrice();
		double low = candles[startIndex].getHighPrice();

		for (int i = startIndex; i <= endIndex; i++)
		{
			if (candles[i].getHighPrice() > high)
			{
				high = candles[i].getHighPrice();
			}
			if (candles[i].getLowPrice() < low)
			{
				low = candles[i].getLowPrice();
			}
		}

		Candle newCandle = new Candle();
		newCandle.setOpenTime(candles[startIndex].getOpenTime());
		newCandle.setOpenPrice(candles[startIndex].getOpenPrice());
		newCandle.setHighPrice(high);
		newCandle.setLowPrice(low);
		newCandle.setClosePrice(candles[endIndex].getClosePrice());

		return newCandle;
	}

}
