package com.example.ap_ment.mapper;

import com.example.ap_ment.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapperManager {
    private final List<IMapper> mappers;
    public <V,T> T map(V source){
        for(IMapper mapper : mappers){
            if(mapper.supports(source))return mapper.map(source);
        }
        throw new BadRequestException("Unsupported type");
    }
}
