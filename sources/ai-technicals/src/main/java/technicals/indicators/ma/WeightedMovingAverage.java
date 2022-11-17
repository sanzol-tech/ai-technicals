package technicals.indicators.ma;

import org.decimal4j.util.DoubleRounder;

import technicals.config.Labels;
import technicals.model.Candle;
import technicals.model.indicators.IndicatorEntry;

/**
 * WMA - Weighted Moving Average
 */
public class WeightedMovingAverage
{

	public static double[] calculate(double[] values, int periods)
	{
		if (values.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = values.length - periods + 1;
		double[] results = new double[len];

		for (int i = 0; i < len; i++)
		{
			results[i] = DoubleRounder.round(calcAvg(values, i, periods), 4);
		}

		return results;
	}

	public static IndicatorEntry[] calculate(Candle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		IndicatorEntry[] wmaEntries = new IndicatorEntry[len];

		for (int i = 0; i < len; i++)
		{
			wmaEntries[i] = new IndicatorEntry(candles[i + periods - 1]);
			wmaEntries[i].setValue(DoubleRounder.round(calcAvg(candles, i, periods), 4));
		}

		return wmaEntries;
	}

	private static double calcAvg(double[] values, int startIndex, int periods)
	{
		double sum = 0;
		for (int i = 0; i < periods; i++)
		{
			sum += values[startIndex + i] * (periods - i);
		}
		return sum / ((periods * (periods + 1)) / 2);
	}

	private static double calcAvg(Candle[] candles, int startIndex, int periods)
	{
		double sum = 0;
		for (int i = 0; i < periods; i++)
		{
			sum += candles[startIndex + i].getDefaultPrice() * (periods - i);
		}
		return sum / ((periods * (periods + 1)) / 2);
	}

}
