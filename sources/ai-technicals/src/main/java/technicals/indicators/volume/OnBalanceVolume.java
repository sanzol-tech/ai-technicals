package technicals.indicators.volume;

import technicals.config.Labels;
import technicals.model.Candle;
import technicals.model.indicators.IndicatorEntry;

/**
 * OBV - On Balance Volume
 */
public class OnBalanceVolume
{

	public static IndicatorEntry[] calculate(Candle[] candles)
	{
		if (candles.length < 2)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		IndicatorEntry[] entries = new IndicatorEntry[candles.length];

		entries[0] = new IndicatorEntry(candles[0]);
		entries[0].setValue(candles[0].getVolume());

		for (int i = 1; i < candles.length; i++)
		{
			Candle prev = candles[i - 1];
			Candle curr = candles[i];
			entries[i] = new IndicatorEntry(curr);

			if (curr.getClosePrice() > prev.getClosePrice())
			{
				entries[i].setValue(entries[i - 1].getValue() + curr.getVolume());
			}
			else if (curr.getClosePrice() < prev.getClosePrice())
			{
				entries[i].setValue(entries[i - 1].getValue() - curr.getVolume());
			}
			else
			{
				entries[i].setValue(entries[i - 1].getValue());
			}
		}

		return entries;
	}

}
