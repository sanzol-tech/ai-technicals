package technicals.model.indicators;

import technicals.model.Candle;

public class IndicatorEntry extends Candle
{
	private double value;

	public IndicatorEntry()
	{
		//
	}

	public IndicatorEntry(Candle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f", openTime, closePrice, value);
	}

	// ---- STATICS -----------------------------------------------------------

	public static double[] toDoubleArray(IndicatorEntry[] entries)
	{
		double[] doubleValues = new double[entries.length];

		for (int i = 0; i < entries.length; i++)
		{
			doubleValues[i] = entries[i].getValue();
		}

		return doubleValues;
	}

}
