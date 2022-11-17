package technicals.indicators.oscillator;

import org.decimal4j.util.DoubleRounder;

import technicals.config.Labels;
import technicals.model.Candle;
import technicals.model.oscillator.AroonEntry;
import technicals.util.CandleUtils;

/**
 * Aroon Oscillator
 */
public class Aroon
{

	public static AroonEntry[] calculate(Candle[] candles)
	{
		return calculate(candles, 14);
	}

	public static AroonEntry[] calculate(Candle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		int len = candles.length - periods + 1;
		AroonEntry[] aroonEntries = new AroonEntry[len];

		for (int i = 0; i < len; i++)
		{
			aroonEntries[i] = new AroonEntry(candles[i + periods - 1]);

			// aroon Up
			int highestHighIndex = CandleUtils.highestHighIndex(candles, i, i + periods - 1);
			int sinceHigh = (i + periods - 1) - highestHighIndex;
			double aroonUp = DoubleRounder.round(100 * (((double) periods - (double) sinceHigh) / (double) periods), 2);
			aroonEntries[i].setAroonUp(aroonUp);

			// aroon Down
			int lowestLowIndex = CandleUtils.lowestLowIndex(candles, i, i + periods - 1);
			int sinceLow = (i + periods - 1) - lowestLowIndex;
			double aroonDown = DoubleRounder.round(100 * (((double) periods - (double) sinceLow) / (double) periods), 2);
			aroonEntries[i].setAroonDown(aroonDown);

			// aroon Oscillator
			aroonEntries[i].setOscillator(DoubleRounder.round(aroonUp - aroonDown, 2));
		}

		return aroonEntries;
	}

}