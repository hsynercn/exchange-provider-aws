package util;

import model.ExchangeRate;
import model.ExchangeRateList;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class GraphCalculator {

    public static ArrayList<ExchangeRate> traversal(ExchangeRateList exchangeRateList, String mainCurrencyCode) {
        HashMap<String, HashMap<String, ExchangeRate>> originGraphMap = new HashMap<>();

        HashSet<String> selfConnected = new HashSet<>();
        exchangeRateList.getList().forEach(exchangeRate -> {
            addGraphConnection(originGraphMap, exchangeRate);
            ExchangeRate reverseExchangeRate = getReverseExchangeRate(exchangeRate);
            addGraphConnection(originGraphMap, reverseExchangeRate);
            addSelfConnections(originGraphMap, selfConnected, exchangeRate);
        });

        HashMap<String, HashMap<String, ExchangeRate>> resultGraphMap = deepGraphCopy(originGraphMap);

        if (resultGraphMap.containsKey(mainCurrencyCode)) {
            HashSet<String> visitedNodes = new HashSet<>();
            Queue<String> queue = new LinkedList<>();
            BigDecimal multiplier = new BigDecimal(BigInteger.valueOf(1));
            queue.add(mainCurrencyCode);
            visitedNodes.add(mainCurrencyCode);
            while (queue.isEmpty() == false) {
                String nodeCode = queue.remove();
                resultGraphMap.get(nodeCode).forEach((connectedNode, exchangeRate) -> {
                    if (visitedNodes.contains(connectedNode) == false) {
                        ExchangeRate preRate = resultGraphMap.get(mainCurrencyCode).get(exchangeRate.getNumeratorEntityCode());
                        ExchangeRate directConnection = getDirectConnection(mainCurrencyCode, connectedNode, exchangeRate, preRate);
                        addGraphConnection(resultGraphMap, directConnection);
                        queue.add(connectedNode);
                        visitedNodes.add(connectedNode);
                    }
                });
            }
        }
        return new ArrayList<>(resultGraphMap.get(mainCurrencyCode).values());
    }

    private static ExchangeRate getDirectConnection(String mainCurrencyCode, String connectedNode, ExchangeRate exchangeRate, ExchangeRate preRate) {
        BigDecimal currentRate = preRate.getRate().multiply(exchangeRate.getRate());
        BigDecimal sourcePre = preRate.getRate().subtract(preRate.getDailyChange());
        BigDecimal targetPre = exchangeRate.getRate().subtract(exchangeRate.getDailyChange());
        BigDecimal previousRate = sourcePre.multiply(targetPre);
        BigDecimal change = currentRate.subtract(previousRate);
        return new ExchangeRate(
                mainCurrencyCode,
                connectedNode,
                currentRate,
                change,
                change.divide(currentRate, 10, RoundingMode.HALF_UP)
        );
    }

    private static HashMap<String, HashMap<String, ExchangeRate>> deepGraphCopy(HashMap<String, HashMap<String, ExchangeRate>> graph) {
        HashMap<String, HashMap<String, ExchangeRate>> copiedGraph = new HashMap<>();
        graph.forEach((source, stringExchangeRateHashMap) -> {
            copiedGraph.put(source, new HashMap<>());
            stringExchangeRateHashMap.forEach((target, exchangeRate) -> {
                ExchangeRate cloned = new ExchangeRate(
                        exchangeRate.getNumeratorEntityCode(),
                        exchangeRate.getDenominatorEntityCode(),
                        exchangeRate.getRate(),
                        exchangeRate.getDailyChange(),
                        exchangeRate.getDailyChangeRatio());
                copiedGraph.get(source).put(target, cloned);
            });
        });
        return copiedGraph;
    }

    private static void addSelfConnections(HashMap<String, HashMap<String, ExchangeRate>> graphMap, HashSet<String> selfConnected, ExchangeRate exchangeRate) {
        if (selfConnected.contains(exchangeRate.getNumeratorEntityCode()) == false) {
            addGraphConnection(graphMap, getSelfConnection(exchangeRate.getNumeratorEntityCode()));
        }
        if (selfConnected.contains(exchangeRate.getDenominatorEntityCode()) == false) {
            addGraphConnection(graphMap, getSelfConnection(exchangeRate.getDenominatorEntityCode()));
        }
    }

    private static ExchangeRate getSelfConnection(String entityCode) {
        ExchangeRate selfConnectedRate = new ExchangeRate(
                entityCode,
                entityCode,
                BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(0.0),
                BigDecimal.valueOf(0.0)
        );
        return selfConnectedRate;
    }

    private static void addGraphConnection(HashMap<String, HashMap<String, ExchangeRate>> originalGraphMap, ExchangeRate exchangeRate) {
        if (originalGraphMap.containsKey(exchangeRate.getNumeratorEntityCode()) == false) {
            originalGraphMap.put(exchangeRate.getNumeratorEntityCode(), new HashMap<>());
        }
        originalGraphMap.get(exchangeRate.getNumeratorEntityCode())
                .put(exchangeRate.getDenominatorEntityCode(), exchangeRate);
    }

    private static ExchangeRate getReverseExchangeRate(ExchangeRate exchangeRate) {
        BigDecimal reverseCurrentRate = new BigDecimal(1.0).divide(exchangeRate.getRate(), 10, RoundingMode.HALF_UP);
        BigDecimal reversePreviousRate = new BigDecimal(1.0).divide(exchangeRate.getRate().subtract(exchangeRate.getDailyChange()), 10, RoundingMode.HALF_UP);
        BigDecimal dailyChange = reverseCurrentRate.subtract(reversePreviousRate);
        BigDecimal dailyChangeRatio = reverseCurrentRate.subtract(reversePreviousRate).divide(reverseCurrentRate, 10, RoundingMode.HALF_UP);
        return new ExchangeRate(
                exchangeRate.getDenominatorEntityCode(),
                exchangeRate.getNumeratorEntityCode(),
                reverseCurrentRate,
                dailyChange,
                dailyChangeRatio);
    }
}
