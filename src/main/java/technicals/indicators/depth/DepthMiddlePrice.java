package technicals.indicators.depth;

import java.util.List;

import technicals.model.OrderBookEntry;

/**
 * Calculate the weighted average price of the union between ask and bid at a given depth
 */
public class DepthMiddlePrice
{
	public static double calculate(List<OrderBookEntry> lstAsks, List<OrderBookEntry> lstBids)
	{
		return calculate(lstAsks, lstBids, 0.1, false);
	}

	public static double calculate(List<OrderBookEntry> lstAsks, List<OrderBookEntry> lstBids, double depthPercent, boolean quoted)
	{
		double pp = (lstAsks.get(0).getPrice().doubleValue() + lstBids.get(0).getPrice().doubleValue()) / 2;
		double askMaxPrice = pp * (1 + depthPercent);
		double askMinPrice = pp * (1 - depthPercent);

		double askSumProd = 0.0;
		double askSumQty = 0.0;
		double bidSumProd = 0.0;
		double bidSumQty = 0.0;

		for (OrderBookEntry entry : lstAsks)
		{
			if (entry.getPrice().doubleValue() > askMaxPrice)
			{
				break;
			}
			double qty = (quoted) ? entry.getUsd().doubleValue() : entry.getQty().doubleValue();
			askSumProd += entry.getPrice().doubleValue() * qty;
			askSumQty += qty;
		}

		for (OrderBookEntry entry : lstBids)
		{
			if (entry.getPrice().doubleValue() < askMinPrice)
			{
				break;
			}
			double qty = (quoted) ? entry.getUsd().doubleValue() : entry.getQty().doubleValue();
			bidSumProd += entry.getPrice().doubleValue() * qty;
			bidSumQty += qty;
		}

		double weightedPoint = (askSumProd + bidSumProd) / (askSumQty + bidSumQty);

		return weightedPoint;
	}

}
