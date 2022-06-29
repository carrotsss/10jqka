package com.insigma.handler.sod.handler;

import java.util.Map;

public interface ArchiveHandle {
    Map handle(ClearingResultMessge clearingResultMessge, ClearConfig clearConfig);
}
