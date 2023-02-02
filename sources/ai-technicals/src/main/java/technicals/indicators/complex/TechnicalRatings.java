package technicals.indicators.complex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import technicals.config.Labels;
import technicals.indicators.ma.ExponentialMovingAverage;
import technicals.indicators.ma.SimpleMovingAverage;
import technicals.indicators.ma.VWMovingAverage;
import technicals.indicators.misc.Ichimoku;
import technicals.indicators.oscillator.AverageDirectionalIndex;
import technicals.indicators.oscillator.AwesomeOscillator;
import technicals.indicators.oscillator.BullBearPower;
import technicals.indicators.oscillator.CommodityChannelIndex;
import technicals.indicators.oscillator.MACD;
import technicals.indicators.oscillator.RelativeStrengthIndex;
import technicals.indicators.oscillator.Stochastic;
import technicals.indicators.oscillator.StochasticRSI;
import technicals.indicators.oscillator.UltimateOscillator;
import technicals.indicators.oscillator.WilliamsR;
import technicals.indicators.volatility.AverageTrueRange;
import technicals.model.TechCandle;
import technicals.model.indicators.AtrEntry;
import technicals.model.indicators.IchimokuEntry;
import technicals.model.indicators.IndicatorEntry;
import technicals.model.oscillator.AdxEntry;
import technicals.model.oscillator.MACDEntry;
import technicals.model.oscillator.RsiEntry;
import technicals.model.oscillator.StochRsiEntry;
import technicals.model.oscillator.StochasticEntry;
import technicals.model.oscillator.WilliamsREntry;
import technicals.util.CandleUtils;

public class TechnicalRatings
{
	public enum RatingStatus
	{
		STRONG_SELL, SELL, NEUTRAL, BUY, STRONG_BUY
	};

	public static final int UP_TREND = 1;
	public static final int DOWN_TREND = -1;
	public static final int NEUTRAL_TREND = 0;
	
	private static final int[] MA_PERIODS = { 10, 20, 30, 50, 100, 200 };
	// public static final int[] MA_PERIODS = { 13, 21, 34, 55, 89, 144, 233 };

	private int pricePrecision;

	private TechCandle[] candles;
	private TechCandle candleMerged;

	private double atr;

	private double[] sma = new double[MA_PERIODS.length];
	private int[] smaTrend = new int[MA_PERIODS.length];
	private double[] ema = new double[MA_PERIODS.length];
	private int[] emaTrend = new int[MA_PERIODS.length];
	private double vwma;
	private int vwmaTrend;

	private double maRatingSum;
	private double maRatingCount;
	private RatingStatus maRatingStatus; 
	private int trend;

	private double rsi;
	private int rsiStatus;
	private double stoch;
	private int stochStatus;
	private double cci;
	private int cciStatus;
	private double adx;
	private int adxStatus;
	private double ao;
	private int aoStatus;
	private double macd;
	private int macdStatus;
	private double stochRsi;
	private int stochRsiStatus;
	private double williamsR;
	private int williamsRStatus;
	private double bbp;
	private int bbpStatus;
	private double uo;
	private int uoStatus;

	private double oscRatingSum;
	private double oscRatingCount;
	private RatingStatus oscRatingStatus; 
	
	private RatingStatus ratingStatus; 

	// ---- CONSTRUCTOR ----------------------------------------------------------------
	
	public TechnicalRatings(int pricePrecision)
	{
		this.pricePrecision = pricePrecision;
	}
	
	// ---- PROPERTIES -----------------------------------------------------------------

	public TechCandle[] getCandles()
	{
		return candles;
	}

	public TechCandle getCandleMerged()
	{
		return candleMerged;
	}

	public double getAtr()
	{
		return atr;
	}

	public double[] getSma()
	{
		return sma;
	}

	public int[] getSmaTrend()
	{
		return smaTrend;
	}

	public double[] getEma()
	{
		return ema;
	}

	public int[] getEmaTrend()
	{
		return emaTrend;
	}

	public double getVwma()
	{
		return vwma;
	}

	public int getVwmaTrend()
	{
		return vwmaTrend;
	}

	public RatingStatus getMaRatingStatus()
	{
		return maRatingStatus;
	}

	public int getTrend()
	{
		return trend;
	}

	public double getRsi()
	{
		return rsi;
	}

	public int getRsiStatus()
	{
		return rsiStatus;
	}

	public double getStoch()
	{
		return stoch;
	}

	public int getStochStatus()
	{
		return stochStatus;
	}

	public double getCci()
	{
		return cci;
	}

	public int getCciStatus()
	{
		return cciStatus;
	}

	public double getAdx()
	{
		return adx;
	}

	public int getAdxStatus()
	{
		return adxStatus;
	}

	public double getAo()
	{
		return ao;
	}

	public int getAoStatus()
	{
		return aoStatus;
	}

	public double getMacd()
	{
		return macd;
	}

	public int getMacdStatus()
	{
		return macdStatus;
	}

	public double getStochRsi()
	{
		return stochRsi;
	}

	public int getStochRsiStatus()
	{
		return stochRsiStatus;
	}

	public double getWilliamsR()
	{
		return williamsR;
	}

	public int getWilliamsRStatus()
	{
		return williamsRStatus;
	}

	public double getBbp()
	{
		return bbp;
	}

	public int getBbpStatus()
	{
		return bbpStatus;
	}

	public double getUo()
	{
		return uo;
	}

	public int getUoStatus()
	{
		return uoStatus;
	}

	public RatingStatus getOscRatingStatus()
	{
		return oscRatingStatus;
	}

	public RatingStatus getRatingStatus()
	{
		return ratingStatus;
	}

	// ---- Calc -----------------------------------------------------------------------

	public static int minCandlesLengh()
	{
		return MA_PERIODS[MA_PERIODS.length-1] + 2;
	}
	
	public void calculate(TechCandle[] candles) throws Exception
	{
		if (candles.length < minCandlesLengh())
		{
			throw new IllegalArgumentException(Labels.NOT_ENOUGH_VALUES);
		}

		this.candles = candles;

		// ---- Merge ------------------------------------------------
		candleMerged = CandleUtils.mergeCandles(candles, 0, candles.length - 1);

		// ---- Volatility -------------------------------------------
		AtrEntry[] atrEntries = AverageTrueRange.calculate(candles, 55);
		atr = atrEntries[atrEntries.length - 1].getAtr();

		// ---- Trend ------------------------------------------------
		calcMovingAverages();
		calcMARating();

		// ---- Oscillators ------------------------------------------
		calcOscillators();
		calcOscRating();
		
		// ---- Rating Status ----------------------------------------
		calcRating();
	}

	private void calcRating()
	{
		double rating = (maRatingSum + oscRatingSum) / (maRatingCount + oscRatingCount);

		if (rating < -0.5)
			ratingStatus = RatingStatus.STRONG_SELL;
		else if (rating > 0.5)
			ratingStatus = RatingStatus.STRONG_BUY;
		else if (rating < -0.1 && rating >= -0.5)
			ratingStatus = RatingStatus.SELL;
		else if (rating > 0.1 && rating <= 0.5)
			ratingStatus = RatingStatus.BUY;
		else
			ratingStatus = RatingStatus.NEUTRAL;
	}

	// ---- Trend ----------------------------------------------------------------------

	public void calcMovingAverages()
	{
		double closePrice = last(candles).getClosePrice();

		// SMA
		for (int i = 0; i < MA_PERIODS.length; i++)
		{
			IndicatorEntry[] smaEntries = SimpleMovingAverage.calculate(candles, MA_PERIODS[i]);
			sma[i] = last(smaEntries).getValue();
			smaTrend[i] = calcMAvgTrend(sma[i], closePrice);
		}

		// EMA
		for (int i = 0; i < MA_PERIODS.length; i++)
		{
			IndicatorEntry[] emaEntries = ExponentialMovingAverage.calculate(candles, MA_PERIODS[i]);
			ema[i] = last(emaEntries).getValue();
			emaTrend[i] = calcMAvgTrend(ema[i], closePrice);
		}

		// VWMA
		IndicatorEntry[] vwmaEntries = VWMovingAverage.calculate(candles, MA_PERIODS[1]);
		vwma = last(vwmaEntries).getValue();
		vwmaTrend = calcMAvgTrend(vwma, closePrice);

		// ICHIMOKU
		IchimokuEntry[] ichimokuEntries = Ichimoku.calculate(candles);
		double ichimokuBaseLine = last(ichimokuEntries).getBaseLine();

		/*
		if (getLeadingSpanA() > getLeadingSpanB() && 
			getClosePrice() > getLeadingSpanA() && 
			getClosePrice() < ichimokuBaseLine && 
			getClosePrice()[1] < getConversionLine() && 
			getClosePrice() > getConversionLine())

		if (getLeadingSpanB() > getLeadingSpanA() && 
			getClosePrice() < getLeadingSpanB() && 
			getClosePrice() > ichimokuBaseLine && 
			getClosePrice()[1] > getConversionLine() && 
			getClosePrice() < getConversionLine())
		*/

	}

	private int calcMAvgTrend(double avgPrice, double closePrice)
	{
		BigDecimal ma = BigDecimal.valueOf(avgPrice).setScale(pricePrecision - 1, RoundingMode.HALF_UP);

		return (ma.doubleValue() < closePrice) ? UP_TREND : (ma.doubleValue() > closePrice) ? DOWN_TREND : NEUTRAL_TREND;
	}

	private void calcMARating()
	{
		maRatingCount = (1 + (2 * MA_PERIODS.length));
		
		maRatingSum = vwmaTrend;
		for (int i = 0; i < MA_PERIODS.length; i++)
		{
			maRatingSum += smaTrend[i] + emaTrend[i];
		}
		double rating = maRatingSum / maRatingCount;  

		if (rating < -0.5)
			maRatingStatus = RatingStatus.STRONG_SELL;
		else if (rating > 0.5)
			maRatingStatus = RatingStatus.STRONG_BUY;
		else if (rating < -0.1 && rating >= -0.5)
			maRatingStatus = RatingStatus.SELL;
		else if (rating > 0.1 && rating <= 0.5)
			maRatingStatus = RatingStatus.BUY;
		else
			maRatingStatus = RatingStatus.NEUTRAL;

		// --- Trend ------------
		trend = (rating > 0.12) ? UP_TREND : (rating < -0.12) ? DOWN_TREND : NEUTRAL_TREND;
	}

	// ---------------------------------------------------------------------------------

	public void calcOscillators()
	{
	
		// RSI
		RsiEntry[] rsiEntries = RelativeStrengthIndex.calculate(candles, 14);
		rsi = last(rsiEntries).getRsi();
		if (rsi < 30 && prev1(rsiEntries).getRsi() < rsi)
			rsiStatus = 1;
		else if (rsi > 70 && prev1(rsiEntries).getRsi() > rsi)
			rsiStatus = -1;
		else
			rsiStatus = 0;

		// Stochastic
		StochasticEntry[] stochEntries = Stochastic.calculate(candles, 14, 1, 3);
		stoch = last(stochEntries).getD();
		if (last(stochEntries).getK() < 20 && last(stochEntries).getD() < 20 && last(stochEntries).getK() > last(stochEntries).getD() && prev1(stochEntries).getK() < prev1(stochEntries).getD())
			stochStatus = 1;
		else if (last(stochEntries).getK() > 80 && last(stochEntries).getD() > 80 && last(stochEntries).getK() < last(stochEntries).getD() && prev1(stochEntries).getK() > prev1(stochEntries).getD())
			stochStatus = -1;
		else
			stochStatus = 0;

		// CCI
		IndicatorEntry[] cciEntries = CommodityChannelIndex.calculate(candles, 20);
		cci = last(cciEntries).getValue();
		if (cci < -100 && cci > prev1(cciEntries).getValue())
			cciStatus = 1;
		else if (cci > 100 && cci < prev1(cciEntries).getValue())
			cciStatus = -1;
		else
			cciStatus = 0;

		// ADX
		AdxEntry[] adxEntries = AverageDirectionalIndex.calculate(candles, 14);
		adx = last(adxEntries).getAdx();
		if (adx > 20 && last(adxEntries).getPosDI() > last(adxEntries).getNegDI() && prev1(adxEntries).getPosDI() < prev1(adxEntries).getNegDI())
			adxStatus = 1;
		else if (adx > 20 && last(adxEntries).getPosDI() < last(adxEntries).getNegDI() && prev1(adxEntries).getPosDI() > prev1(adxEntries).getNegDI())
			adxStatus = -1;
		else
			adxStatus = 0;

		// Awesome Oscillator
		IndicatorEntry[] aoEntries = AwesomeOscillator.calculate(candles);
		ao = last(aoEntries).getValue();
		if (ao > 0 && prev1(aoEntries).getValue() > 0 && ao > prev1(aoEntries).getValue() && prev2(aoEntries).getValue() > prev1(aoEntries).getValue())
			aoStatus = 1;
		else if (ao < 0 && prev1(aoEntries).getValue() < 0 && ao < prev1(aoEntries).getValue() && prev2(aoEntries).getValue() < prev1(aoEntries).getValue())
			aoStatus = -1;
		else
			aoStatus = 0;

		// MACD
		MACDEntry[] macdEntries = MACD.calculate(candles, 12, 26, 9);
		macd = last(macdEntries).getMacd();
		if (macd > last(macdEntries).getSignal())
			macdStatus = 1;
		else if (macd < last(macdEntries).getSignal())
			macdStatus = -1;
		else
			macdStatus = 0;

		// Stochastic RSI
		StochRsiEntry[] stochRsiEntries = StochasticRSI.calculate(candles, 14, 14, 3, 3);
		stochRsi = last(stochRsiEntries).getD();
		if (trend == DOWN_TREND && last(stochRsiEntries).getK() < 20 && last(stochRsiEntries).getD() < 20 && last(stochRsiEntries).getK() > last(stochRsiEntries).getD() && prev1(stochRsiEntries).getK() < prev1(stochRsiEntries).getD())
			stochRsiStatus = 1;
		else if (trend == UP_TREND && last(stochRsiEntries).getK() > 80 && last(stochRsiEntries).getD() > 80 && last(stochRsiEntries).getK() < last(stochRsiEntries).getD() && prev1(stochRsiEntries).getK() > prev1(stochRsiEntries).getD())
			stochRsiStatus = -1;
		else
			stochRsiStatus = 0;

		// Williams R
		WilliamsREntry[] williamsREntries = WilliamsR.calculate(candles, 14);
		williamsR = last(williamsREntries).getR();
		if (williamsR < -80 && williamsR > prev1(williamsREntries).getR())
			williamsRStatus = 1;
		else if (williamsR > -20 && williamsR < prev1(williamsREntries).getR())
			williamsRStatus = -1;
		else
			williamsRStatus = 0;

		// Bull Bear Power
		IndicatorEntry[] bbpEntries = BullBearPower.calculate(candles, 13);
		bbp = last(bbpEntries).getValue();
		if (trend == UP_TREND && bbp < 0 && bbp > prev1(bbpEntries).getValue())
			bbpStatus = 1;
		else if (trend == DOWN_TREND && bbp > 0 && bbp < prev1(bbpEntries).getValue())
			bbpStatus = -1;
		else
			bbpStatus = 0;

		// UO
		IndicatorEntry[] uoEntries = UltimateOscillator.calculate(candles, 7, 14, 28);
		uo = last(uoEntries).getValue();
		if (uo > 70)
			uoStatus = 1;
		else if (uo < 30)
			uoStatus = -1;
		else
			uoStatus = 0;

	}

	private void calcOscRating()
	{
		oscRatingSum = rsiStatus + stochStatus + cciStatus + adxStatus + aoStatus + macdStatus + stochRsiStatus + williamsRStatus + bbpStatus + uoStatus;
		oscRatingCount = 10;
		double rating = oscRatingSum / oscRatingCount;  

		if (rating < -0.5)
			oscRatingStatus = RatingStatus.STRONG_SELL;
		else if (rating > 0.5)
			oscRatingStatus = RatingStatus.STRONG_BUY;
		else if (rating < -0.1 && rating >= -0.5)
			oscRatingStatus = RatingStatus.SELL;
		else if (rating > 0.1 && rating <= 0.5)
			oscRatingStatus = RatingStatus.BUY;
		else
			oscRatingStatus = RatingStatus.NEUTRAL;
	}

	// ---------------------------------------------------------------------------------

	private static <T> T last(T[] t)
	{
		return t[t.length - 1];
	}

	private static <T> T prev1(T[] t)
	{
		return t[t.length - 2];
	}

	private static <T> T prev2(T[] t)
	{
		return t[t.length - 3];
	}

	// ---------------------------------------------------------------------------------

	@Override
	public String toString()
	{
		return "atr=" + atr + " atr%=" + atr / candles[candles.length - 1].getClosePrice()
				+ "\nsma=" + Arrays.toString(sma) + "\nsmaTrend=" + Arrays.toString(smaTrend) 
				+ "\nema=" + Arrays.toString(ema) + "\nemaTrend=" + Arrays.toString(emaTrend)
				+ "\nvwma=" + vwma + "\nvwmaTrend=" + vwmaTrend + "\nmaRatingSum=" + maRatingSum + "\nmaRatingCount=" + maRatingCount
				+ "\nmaRatingStatus=" + maRatingStatus + "\ntrend=" + trend + "\nrsi=" + rsi + "\nrsiStatus=" + rsiStatus + "\nstoch=" + stoch + "\nstochStatus=" + stochStatus
				+ "\ncci=" + cci + "\ncciStatus=" + cciStatus + "\nadx=" + adx + "\nadxStatus=" + adxStatus + "\nao=" + ao + "\naoStatus=" + aoStatus + "\nmacd=" + macd + "\nmacdStatus=" + macdStatus + "\nstochRsi="
				+ stochRsi + "\nstochRsiStatus=" + stochRsiStatus + "\nwilliamsR=" + williamsR + "\nwilliamsRStatus=" + williamsRStatus + "\nbbp=" + bbp + "\nbbpStatus="
				+ bbpStatus + "\nuo=" + uo + "\nuoStatus=" + uoStatus + "\noscRatingSum=" + oscRatingSum + "\noscRatingCount=" + oscRatingCount + "\noscRatingStatus="
				+ oscRatingStatus + "\nratingStatus=" + ratingStatus;
	}

}
