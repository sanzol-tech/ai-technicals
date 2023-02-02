package technicals.indicators.volatility;

import technicals.config.Labels;
import technicals.model.TechCandle;
import technicals.model.indicators.AtrEntry;

/**
 * ATR - Average True Range
 */
public class AverageTrueRange
{

	public static AtrEntry[] calculate(TechCandle[] candles)
	{
		return calculate(candles, 20);
	}

	public static AtrEntry[] calculate(TechCandle[] candles, int periods)
	{
		if (candles.length < periods)
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		AtrEntry[] entries = new AtrEntry[candles.length];

		for (int i = 0; i < candles.length; i++)
		{
			TechCandle current = candles[i];
			entries[i] = new AtrEntry(current);

			double tr = trueRange(candles, i);
			entries[i].setTr(tr);

			if (i == 0)
			{
				entries[0].setAtr(tr);
			}
			else
			{
				double priorATR = entries[i - 1].getAtr();
				double atr = ((priorATR * (periods - 1)) + tr) / periods;
				entries[i].setAtr(atr);
			}
		}

		return entries;
	}

	public static double trueRange(TechCandle[] candles, int index)
	{
		double tr = candles[index].getHighPrice() - candles[index].getLowPrice();

		if (index > 0)
		{
			double tr2 = Math.abs(candles[index].getHighPrice() - candles[index - 1].getClosePrice());
			double tr3 = Math.abs(candles[index].getLowPrice() - candles[index - 1].getClosePrice());

			tr = Math.max(Math.max(tr, tr2), tr3);
		}

		return tr;
	}

}
