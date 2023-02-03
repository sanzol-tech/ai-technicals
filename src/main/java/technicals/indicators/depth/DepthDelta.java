package technicals.indicators.depth;

import java.util.List;

import technicals.model.OrderBookEntry;

/**
 * Calculate delta between ask and bid at a given depth
 */
public class DepthDelta
{
	private double askSumQty;
	private double askSumQuoted;
	private double bidSumQty;
	private double bidSumQuoted;
	private double delta;
	private double deltaPercent;
	private double deltaQuoted;
	private double deltaQuotedPercent;

	public double getAskSumQty()
	{
		return askSumQty;
	}

	public double getAskSumQuoted()
	{
		return askSumQuoted;
	}

	public double getBidSumQty()
	{
		return bidSumQty;
	}

	public double getBidSumQuoted()
	{
		return bidSumQuoted;
	}

	public double getDelta()
	{
		return delta;
	}

	public double getDeltaPercent()
	{
		return deltaPercent;
	}

	public double getDeltaQuoted()
	{
		return deltaQuoted;
	}

	public double getDeltaQuotedPercent()
	{
		return deltaQuotedPercent;
	}

	public static DepthDelta getInstance()
	{
		return new DepthDelta();
	}

	public void calculate(List<OrderBookEntry> lstAsks, List<OrderBookEntry> lstBids)
	{
		calculate(lstAsks, lstBids, 0.1);
	}

	public void calculate(List<OrderBookEntry> lstAsks, List<OrderBookEntry> lstBids, Double depthPercent)
	{
		double pp = (lstAsks.get(0).getPrice().doubleValue() + lstBids.get(0).getPrice().doubleValue()) / 2;

		double askMaxPrice = pp * (depthPercent != null ? (1 + depthPercent) : 5);
		double askMinPrice = pp * (depthPercent != null ? (1 - depthPercent) : 0.2);

		askSumQty = 0.0;
		askSumQuoted = 0.0;
		bidSumQty = 0.0;
		bidSumQuoted = 0.0;

		for (OrderBookEntry entry : lstAsks)
		{
			if (entry.getPrice().doubleValue() > askMaxPrice)
			{
				break;
			}
			askSumQty += entry.getQty().doubleValue();
			askSumQuoted += entry.getUsd().doubleValue();
		}

		for (OrderBookEntry entry : lstBids)
		{
			if (entry.getPrice().doubleValue() < askMinPrice)
			{
				break;
			}
			bidSumQty += entry.getQty().doubleValue();
			bidSumQuoted += entry.getUsd().doubleValue();
		}

		delta = askSumQty - bidSumQty;
		deltaPercent = ((bidSumQty - askSumQty) / (askSumQty + bidSumQty)) * 100;
		deltaQuoted = askSumQuoted - bidSumQuoted;
		deltaQuotedPercent = ((bidSumQuoted - askSumQuoted) / (askSumQuoted + bidSumQuoted)) * 100;
	}

}
