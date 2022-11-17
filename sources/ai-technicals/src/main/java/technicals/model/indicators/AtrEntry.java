package technicals.model.indicators;

import technicals.model.Candle;

public class AtrEntry extends Candle
{
	private double tr;
	private double atr;

	public AtrEntry()
	{
		//
	}

	public AtrEntry(Candle candle)
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
