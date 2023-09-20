package com.example.ap_ment.mapper;

public interface IMapper {
    <V, T> T map(V source);
    <V> boolean supports(V source);
}
