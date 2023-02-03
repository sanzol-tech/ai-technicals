package technicals.indicators.depth;

import java.math.BigDecimal;
import java.util.List;

import technicals.model.OrderBookEntry;

/**
 * Calculate the weighted average price at a given distance for asks and bids 
 */
public class DepthWeightedPoints
{
	private BigDecimal r1;
	private BigDecimal s1;
	private BigDecimal r2;
	private BigDecimal s2;
	private BigDecimal r3;
	private BigDecimal s3;

	public BigDecimal getR1() {
		return r1;
	}

	public BigDecimal getS1() {
		return s1;
	}

	public BigDecimal getR2() {
		return r2;
	}

	public BigDecimal getS2() {
		return s2;
	}

	public BigDecimal getR3() {
		return r3;
	}

	public BigDecimal getS3() {
		return s3;
	}

	public static DepthWeightedPoints getInstance()
	{
		return new DepthWeightedPoints();
	}

	public void calculate(List<OrderBookEntry> lstAsks, List<OrderBookEntry> lstBids, double depthPercent1, double depthPercent2, double depthPercent3)
	{
		double pp = (lstAsks.get(0).getPrice().doubleValue() + lstBids.get(0).getPrice().doubleValue()) / 2;

		double askPriceTo1 = pp * (1 + depthPercent1);
		double bidPriceTo1 = pp * (1 - depthPercent1);
		r1 = weightedAverageAsks(lstAsks, pp, askPriceTo1);
		s1 = weightedAverageBids(lstBids, pp, bidPriceTo1);

		double askPriceTo2 = pp * (1 + depthPercent2);
		double bidPriceTo2 = pp * (1 - depthPercent2);
		r2 = weightedAverageAsks(lstAsks, pp, askPriceTo2);
		s2 = weightedAverageBids(lstBids, pp, bidPriceTo2);

		double askPriceTo3 = pp * (1 + depthPercent3);
		double bidPriceTo3 = pp * (1 - depthPercent3);
		r3 = weightedAverageAsks(lstAsks, pp, askPriceTo3);
		s3 = weightedAverageBids(lstBids, pp, bidPriceTo3);
	}

	private static BigDecimal weightedAverageAsks(List<OrderBookEntry> lstAsks, double priceA, double priceB)
	{
		double sumProd = 0.0;
		double sumQty = 0.0;

		for (OrderBookEntry entry : lstAsks)
		{
			if (entry.getPrice().doubleValue() <= priceA)
			{
				continue;
			}
			if (entry.getPrice().doubleValue() > priceB)
			{
				break;
			}
			sumProd += entry.getPrice().doubleValue() * entry.getQty().doubleValue();
			sumQty += entry.getQty().doubleValue();
		}

		return BigDecimal.valueOf(sumProd / sumQty);
	}

	private static BigDecimal weightedAverageBids(List<OrderBookEntry> lstBids, double priceA, double priceB)
	{
		double sumProd = 0.0;
		double sumQty = 0.0;

		for (OrderBookEntry entry : lstBids)
		{
			if (entry.getPrice().doubleValue() >= priceA)
			{
				continue;
			}
			if (entry.getPrice().doubleValue() < priceB)
			{
				break;
			}
			sumProd += entry.getPrice().doubleValue() * entry.getQty().doubleValue();
			sumQty += entry.getQty().doubleValue();
		}

		return BigDecimal.valueOf(sumProd / sumQty);
	}

}
