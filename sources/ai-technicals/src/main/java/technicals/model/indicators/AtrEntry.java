package technicals.model.indicators;

import technicals.model.TechCandle;

public class AtrEntry extends TechCandle
{
	private double tr;
	private double atr;

	public AtrEntry()
	{
		//
	}

	public AtrEntry(TechCandle candle)
	{
		openTime = candle.getOpenTime();
		openPrice = candle.getOpenPrice();
		highPrice = candle.getHighPrice();
		lowPrice = candle.getLowPrice();
		closePrice = candle.getClosePrice();
		volume = candle.getVolume();
	}

	public double getTr()
	{
		return tr;
	}

	public void setTr(double tr)
	{
		this.tr = tr;
	}

	public double getAtr()
	{
		return atr;
	}

	public void setAtr(double atr)
	{
		this.atr = atr;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%f\t\t%f\t%f", openTime, closePrice, tr, atr);
	}

}
