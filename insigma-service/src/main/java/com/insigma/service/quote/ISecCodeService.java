package com.insigma.service.quote;

import com.insigma.bean.quote.Security;

public interface ISecCodeService {
    String getIfindIdBySymbol(String symbol, String market);

    Security getSecurityByIfindId(String ifindId);

    Security getSecurityBySymbolAndMarket(String symbol, String market);

    Security getSecurityBySymbolAndStart(String symbol, String state);
}
