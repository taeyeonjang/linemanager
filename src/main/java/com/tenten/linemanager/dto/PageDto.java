package com.tenten.linemanager.dto;

import com.tenten.linemanager.domain.ProcessLog;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PageDto<T> {
    private List<T> list;
    private long count;
    private long totalPages;
}
