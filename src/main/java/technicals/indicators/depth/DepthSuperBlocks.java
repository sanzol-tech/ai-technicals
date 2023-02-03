package technicals.indicators.depth;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import technicals.model.OrderBookEntry;

/**
 * Search for the n-prices with the largest number of orders 
 */
public class DepthSuperBlocks 
{
	private List<OrderBookEntry> lstSuperAsks;
	private List<OrderBookEntry> lstSuperBids;

	public List<OrderBookEntry> getLstSuperAsks()
	{
		return lstSuperAsks;
	}

	public List<OrderBookEntry> getLstSuperBids()
	{
		return lstSuperBids;
	}

	public static DepthSuperBlocks getInstance()
	{
		return new DepthSuperBlocks();
	}

	public void calculate(List<OrderBookEntry> lstAsks, List<OrderBookEntry> lstBids, int maxSize)
	{
		lstSuperAsks = searchSuperAskBlocks(lstAsks, maxSize);
		lstSuperBids = searchSuperBidBlocks(lstBids, maxSize);
	}

	private List<OrderBookEntry> searchSuperAskBlocks(List<OrderBookEntry> lstAsks, long maxSize)
	{
		double maxPrice = lstAsks.get(0).getPrice().multiply(BigDecimal.valueOf(5)).doubleValue();

		List<OrderBookEntry> list = new ArrayList<OrderBookEntry>();

		for (OrderBookEntry e : lstAsks)
		{
			if (e.getPrice().doubleValue() > maxPrice)
			{
				break;
			}
			list.add(new OrderBookEntry(e.getPrice(), e.getQty()));
		}

		Collections.sort(list, Comparator.comparing(OrderBookEntry::getQty).reversed());
		list = list.stream().limit(maxSize).collect(Collectors.toList());
		Collections.sort(list, Comparator.comparing(OrderBookEntry::getPrice));

		return list;
	}

	private List<OrderBookEntry> searchSuperBidBlocks(List<OrderBookEntry> lstBids, long maxSize)
	{
		double minPrice = lstBids.get(0).getPrice().multiply(BigDecimal.valueOf(0.2)).doubleValue();

		List<OrderBookEntry> list = new ArrayList<OrderBookEntry>();

		for (OrderBookEntry e : lstBids)
		{
			if (e.getPrice().doubleValue() < minPrice)
			{
				break;
			}
			list.add(new OrderBookEntry(e.getPrice(), e.getQty()));
		}

		Collections.sort(list, Comparator.comparing(OrderBookEntry::getQty).reversed());
		list = list.stream().limit(maxSize).collect(Collectors.toList());
		Collections.sort(list, Comparator.comparing(OrderBookEntry::getPrice).reversed());

		return list;
	}

}
