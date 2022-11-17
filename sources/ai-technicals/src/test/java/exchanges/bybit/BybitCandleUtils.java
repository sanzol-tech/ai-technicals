package exchanges.bybit;

import java.util.List;

import technicals.model.Candle;

public class BybitCandleUtils
{

	public static Candle[] toCandleArray(List<BybitCandle> lstCandles)
	{
		Candle[] candle = new Candle[lstCandles.size()];
		for (int i = 0; i < lstCandles.size(); i++)
		{
			candle[i] = new Candle();
			candle[i].setOpenTime(lstCandles.get(i).getOpenTimeZoned());
			candle[i].setOpenPrice(lstCandles.get(i).getOpen().doubleValue());
			candle[i].setClosePrice(lstCandles.get(i).getClose().doubleValue());
			candle[i].setHighPrice(lstCandles.get(i).getHigh().doubleValue());
			candle[i].setLowPrice(lstCandles.get(i).getLow().doubleValue());
			candle[i].setVolume(lstCandles.get(i).getVolume().doubleValue());
		}
		return candle;
	}

}
